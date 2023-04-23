package org.moddingx.libx.impl.config.gui.screen.widget;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.moddingx.libx.render.RenderHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class TextWidget extends AbstractWidget {

    private final Screen screen;
    private final List<Component> tooltip;
    
    public TextWidget(Screen screen, int x, int y, int width, int height, Component text, List<? extends Component> tooltip) {
        super(x, y, width, height, text);
        this.screen = screen;
        this.tooltip = ImmutableList.copyOf(tooltip);
    }

    @Override
    public void renderWidget(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        RenderHelper.resetColor();
        //noinspection IntegerDivisionInFloatingPointContext
        Minecraft.getInstance().font.drawShadow(poseStack, this.getMessage(), this.getX(), this.getY() + ((this.height - 8) / 2), 0xFFFFFF);
        if (this.isHovered && !this.tooltip.isEmpty()) {
            this.screen.renderComponentTooltip(poseStack, this.tooltip, mouseX, mouseY);
        }
    }

    @Override
    public void playDownSound(@Nonnull SoundManager manager) {
        //
    }

    @Override
    public void updateWidgetNarration(@Nonnull NarrationElementOutput output) {
        output.add(NarratedElementType.HINT, this.tooltip.toArray(Component[]::new));
    }
}
