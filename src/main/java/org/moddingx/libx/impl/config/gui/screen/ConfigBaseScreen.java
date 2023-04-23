package org.moddingx.libx.impl.config.gui.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.moddingx.libx.render.RenderHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ConfigBaseScreen extends Screen {

    protected final Minecraft mc;

    @Nullable
    private final ConfigScreenManager manager;
    private final boolean hasSearchBar;

    @Nullable
    private EditBox searchBar;
    
    @Nullable
    private BasePanel panel;
    
    // While rendering the scrollable view, tooltips must be delayed
    // Because clipping is enabled, and they need to be rendered with
    // absolute coordinates as they should not be cut by the screen border.
    private final List<Pair<Matrix4f, Consumer<PoseStack>>> capturedTooltips = new LinkedList<>();
    private boolean isCapturingTooltips = false;
    private int currentScrollOffset = 0;

    protected ConfigBaseScreen(Component title, @Nullable ConfigScreenManager manager, boolean hasSearchBar) {
        super(title);
        this.mc = Minecraft.getInstance();
        this.manager = manager;
        this.hasSearchBar = hasSearchBar;
    }
    
    public int contentWidth() {
        return this.width - 12;
    }

    @Override
    protected void init() {
        if (this.manager != null) {
            Button back = Button.builder(Component.literal("\u2190 ").append(Component.translatable("libx.config.gui.back")), button -> this.manager.close())
                    .pos(5, 5)
                    .size(52, 20)
                    .build();
            this.addRenderableWidget(back);
        }

        if (this.hasSearchBar) {
            boolean shouldFocus = this.searchBar != null && this.searchBar.isFocused();
            boolean isActive = this.searchBar != null && this.getFocused() == this.searchBar;
            this.searchBar = new EditBox(this.mc.font, 20, 18 + this.mc.font.lineHeight, this.width - 40, 20, this.searchBar, Component.translatable("libx.config.gui.search.title"));
            this.searchBar.setMaxLength(32767);
            this.searchBar.setFocused(shouldFocus);
            this.addRenderableWidget(this.searchBar);
            if (isActive) {
                this.setFocused(this.searchBar);
            }
            // Responder must be set last, or we'll trigger it while configuring the search bar
            this.searchBar.setResponder(this::searchChange);
        } else {
            this.searchBar = null;
        }

        this.rebuild();
    }

    protected void rebuild() {
        if (this.panel != null) {
            this.removeWidget(this.panel);
        }
        
        ImmutableList.Builder<AbstractWidget> widgetBuilder = ImmutableList.builder();
        this.buildGui(widgetBuilder::add);
        List<AbstractWidget> widgets = widgetBuilder.build();

        int totalHeight = 10 + widgets.stream().map(w -> w.getY() + w.getHeight()).max(Comparator.naturalOrder()).orElse(0);
        int paddingTop = 18 + this.mc.font.lineHeight + (this.hasSearchBar ? 26 : 0);

        this.panel = new BasePanel(this.mc, this.width - 2, this.height - paddingTop, paddingTop, 1) {

            @Override
            protected int getContentHeight() {
                return totalHeight;
            }

            @Override
            public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                ConfigBaseScreen.this.isCapturingTooltips = true;
                poseStack.pushPose();
                super.render(poseStack, mouseX, mouseY, partialTicks);
                poseStack.popPose();
                ConfigBaseScreen.this.isCapturingTooltips = false;
                ConfigBaseScreen.this.capturedTooltips.forEach(pair -> {
                    poseStack.pushPose();
                    poseStack.setIdentity();
                    poseStack.mulPoseMatrix(pair.getLeft());
                    pair.getRight().accept(poseStack);
                    poseStack.popPose();
                });
                ConfigBaseScreen.this.capturedTooltips.clear();
            }

            @Override
            protected void drawPanel(PoseStack poseStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
                ConfigBaseScreen.this.currentScrollOffset = relativeY;
                poseStack.pushPose();
                poseStack.translate(0, relativeY, 0);
                for (AbstractWidget widget : widgets) {
                    widget.render(poseStack, mouseX, mouseY - relativeY, ConfigBaseScreen.this.mc.getDeltaFrameTime());
                }
                poseStack.popPose();
                ConfigBaseScreen.this.currentScrollOffset = 0;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                // Without this, the panel would process clicks from areas currently not on the screen
                if (mouseX >= this.left && mouseX <= this.left + this.width && mouseY >= this.top && mouseY <= this.top + this.height) {
                    return super.mouseClicked(mouseX, mouseY, button);
                } else {
                    return false;
                }
            }

            @Override
            protected boolean clickPanel(double mouseX, double mouseY, int button) {
                // Extra var required as we need to cal all listeners
                // so widgets can for example handle their loss of focus.
                boolean success = false;
                for (GuiEventListener widget : widgets) {
                    if (widget.mouseClicked(mouseX, mouseY, button)) {
                        this.setFocused(widget);
                        if (button == 0) {
                            this.setDragging(true);
                        }
                        success = true;
                    }
                }
                return success;
            }

            @Override
            public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
                if (!super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                    if (this.getFocused() != null && this.isDragging() && button == 0) {
                        return this.getFocused().mouseDragged(mouseX, mouseY - this.top + ((int) this.scrollDistance) - this.border, button, dragX, dragY);
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }

            @Override
            public boolean mouseReleased(double mouseX, double mouseY, int button) {
                if (!super.mouseReleased(mouseX, mouseY, button)) {
                    if (this.getFocused() != null) {
                        return this.getFocused().mouseReleased(mouseX, mouseY - this.top + ((int) this.scrollDistance) - this.border, button);
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        };
        this.addRenderableWidget(this.panel);
    }

    protected abstract void buildGui(Consumer<AbstractWidget> consumer);

    @Nullable
    public ConfigScreenManager getCurrentManager() {
        return this.manager;
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderDirtBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        RenderHelper.resetColor();
        //noinspection IntegerDivisionInFloatingPointContext
        this.mc.font.drawShadow(poseStack, this.getTitle(), (this.width - this.mc.font.width(this.getTitle())) / 2, 11, 0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int key, int i1, int i2) {
        if (key == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc() && this.manager != null) {
            this.manager.close();
            return true;
        } else {
            return super.keyPressed(key, i1, i2);
        }
    }

    public String searchTerm() {
        return this.searchBar == null ? "" : this.searchBar.getValue();
    }
    
    protected void searchChange(String term) {
        
    }

    private static abstract class BasePanel extends ScrollPanel implements NarratableEntry {

        public BasePanel(Minecraft mc, int width, int height, int top, int left) {
            super(mc, width, height, top, left);
        }

        @Nonnull
        @Override
        public NarrationPriority narrationPriority() {
            return NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(@Nonnull NarrationElementOutput output) {
            //
        }
    }

    private void captureTooltip(PoseStack.Pose pose, BiConsumer<PoseStack, Integer> action) {
        final int theOffset = this.currentScrollOffset;
        Matrix4f matrix = new Matrix4f(pose.pose());
        matrix.translate(0, -theOffset, 0);
        this.capturedTooltips.add(Pair.of(matrix, poseStack -> action.accept(poseStack, theOffset)));
    }

    @Override
    protected void renderTooltip(@Nonnull PoseStack poseStack, @Nonnull ItemStack stack, int mouseX, int mouseY) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderTooltip(poseStack0, stack, mouseX, mouseY + scrollOff));
        } else {
            super.renderTooltip(poseStack, stack, mouseX, mouseY);
        }
    }

    @Override
    public void renderTooltip(@Nonnull PoseStack poseStack, @Nonnull List<Component> components, @Nonnull Optional<TooltipComponent> special, int x, int y, @Nonnull ItemStack stack) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderTooltip(poseStack0, components, special, x, y + scrollOff, stack));
        } else {
            super.renderTooltip(poseStack, components, special, x, y, stack);
        }
    }

    @Override
    public void renderTooltip(@Nonnull PoseStack poseStack, @Nonnull List<Component> components, @Nonnull Optional<TooltipComponent> special, int x, int y, @Nullable Font font) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderTooltip(poseStack0, components, special, x, y + scrollOff, font));
        } else {
            super.renderTooltip(poseStack, components, special, x, y, font);
        }
    }

    @Override
    public void renderTooltip(@Nonnull PoseStack poseStack, @Nonnull List<Component> components, @Nonnull Optional<TooltipComponent> special, int x, int y, @Nullable Font font, @Nonnull ItemStack stack) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderTooltip(poseStack0, components, special, x, y + scrollOff, font, stack));
        } else {
            super.renderTooltip(poseStack, components, special, x, y, font, stack);
        }
    }

    @Override
    public void renderTooltip(@Nonnull PoseStack poseStack, @Nonnull List<Component> components, @Nonnull Optional<TooltipComponent> special, int mouseX, int mouseY) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderTooltip(poseStack0, components, special, mouseX, mouseY + scrollOff));
        } else {
            super.renderTooltip(poseStack, components, special, mouseX, mouseY);
        }
    }

    @Override
    public void renderTooltip(@Nonnull PoseStack poseStack, @Nonnull Component component, int mouseX, int mouseY) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderTooltip(poseStack0, component, mouseX, mouseY + scrollOff));
        } else {
            super.renderTooltip(poseStack, component, mouseX, mouseY);
        }
    }

    @Override
    public void renderComponentTooltip(@Nonnull PoseStack poseStack, @Nonnull List<Component> components, int mouseX, int mouseY) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderComponentTooltip(poseStack0, components, mouseX, mouseY + scrollOff));
        } else {
            super.renderComponentTooltip(poseStack, components, mouseX, mouseY);
        }
    }

    @Override
    public void renderComponentTooltip(@Nonnull PoseStack poseStack, @Nonnull List<? extends FormattedText> components, int mouseX, int mouseY, @Nonnull ItemStack stack) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderComponentTooltip(poseStack0, components, mouseX, mouseY + scrollOff, stack));
        } else {
            super.renderComponentTooltip(poseStack, components, mouseX, mouseY, stack);
        }
    }

    @Override
    public void renderComponentTooltip(@Nonnull PoseStack poseStack, @Nonnull List<? extends FormattedText> components, int mouseX, int mouseY, @Nullable Font font) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderComponentTooltip(poseStack0, components, mouseX, mouseY + scrollOff, font));
        } else {
            super.renderComponentTooltip(poseStack, components, mouseX, mouseY, font);
        }
    }

    @Override
    public void renderComponentTooltip(@Nonnull PoseStack poseStack, @Nonnull List<? extends FormattedText> components, int mouseX, int mouseY, @Nullable Font font, @Nonnull ItemStack stack) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderComponentTooltip(poseStack0, components, mouseX, mouseY + scrollOff, font, stack));
        } else {
            super.renderComponentTooltip(poseStack, components, mouseX, mouseY, font, stack);
        }
    }

    @Override
    public void renderTooltip(@Nonnull PoseStack poseStack, @Nonnull List<? extends FormattedCharSequence> components, int mouseX, int mouseY) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderTooltip(poseStack0, components, mouseX, mouseY + scrollOff));
        } else {
            super.renderTooltip(poseStack, components, mouseX, mouseY);
        }
    }

    @Override
    public void renderTooltip(@Nonnull PoseStack poseStack, @Nonnull List<? extends FormattedCharSequence> components, int x, int y, @Nonnull Font font) {
        if (this.isCapturingTooltips) {
            this.captureTooltip(poseStack.last(), (poseStack0, scrollOff) -> this.renderTooltip(poseStack0, components, x, y + scrollOff, font));
        } else {
            super.renderTooltip(poseStack, components, x, y, font);
        }
    }
}
