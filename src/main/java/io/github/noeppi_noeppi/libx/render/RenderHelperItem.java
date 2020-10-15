package io.github.noeppi_noeppi.libx.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Helper to deal with item rendering.
 */
public class RenderHelperItem {

    /**
     * Renders an item tinted in the given color.
     */
    public static void renderItemTinted(ItemStack stack, ItemCameraTransforms.TransformType transformType, int light, int overlay, MatrixStack matrixStack, IRenderTypeBuffer buffer, float r, float g, float b) {
        if (!stack.isEmpty()) {
            boolean isGui = transformType == ItemCameraTransforms.TransformType.GUI;
            boolean isFixed = isGui || transformType == ItemCameraTransforms.TransformType.GROUND || transformType == ItemCameraTransforms.TransformType.FIXED;

            IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, null, null);
            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, false);

            matrixStack.push();
            matrixStack.translate(-0.5D, -0.5D, -0.5D);

            if (!model.isBuiltInRenderer() && (stack.getItem() != Items.TRIDENT || isFixed)) {
                RenderType type = RenderTypeLookup.func_239219_a_(stack, true);
                if (isGui && Objects.equals(type, Atlases.getTranslucentCullBlockType())) {
                    type = Atlases.getTranslucentCullBlockType();
                }

                IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(buffer, type, true, stack.hasEffect());
                renderTintedModel(model, light, overlay, matrixStack, ivertexbuilder, r, g, b);
            } else {
                //noinspection deprecation
                GlStateManager.color4f(r, g, b, 1);
                stack.getItem().getItemStackTileEntityRenderer().func_239207_a_(stack, transformType, matrixStack, buffer, light, overlay);
                //noinspection deprecation
                GlStateManager.color4f(1, 1, 1, 1);
            }

            matrixStack.pop();
        }
    }

    private static void renderTintedModel(IBakedModel model, int light, int overlay, MatrixStack matrixStack, IVertexBuilder buffer, float r, float g, float b) {
        Random random = new Random();

        for (Direction direction : Direction.values()) {
            random.setSeed(42);
            //noinspection deprecation
            renderTintedQuads(matrixStack, buffer, model.getQuads(null, direction, random), light, overlay, r, g, b);
        }

        random.setSeed(42);
        //noinspection deprecation
        renderTintedQuads(matrixStack, buffer, model.getQuads(null, null, random), light, overlay, r, g, b);
    }

    private static void renderTintedQuads(MatrixStack matrixStack, IVertexBuilder buffer, List<BakedQuad> quads, int light, int overlay, float r, float g, float b) {
        MatrixStack.Entry entry = matrixStack.getLast();

        for (BakedQuad bakedquad : quads) {
            buffer.addVertexData(entry, bakedquad, r, g, b, light, overlay, true);
        }
    }

    /**
     * Renders an item into a gui. This allows to set the size of the item and whether the
     * amount should be included.
     */
    public static void renderItemGui(MatrixStack matrixStack, IRenderTypeBuffer buffer, ItemStack stack, int x, int y, int size, boolean includeAmount) {
        if (!stack.isEmpty()) {
            IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, null, Minecraft.getInstance().player);

            matrixStack.push();
            Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
            //noinspection ConstantConditions
            Minecraft.getInstance().getTextureManager().getTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);

            //noinspection deprecation
            RenderSystem.enableAlphaTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            //noinspection deprecation
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            matrixStack.translate(x, y, 50);
            matrixStack.scale(size / 16f, size / 16f, 1);
            matrixStack.translate(8.0F, 8.0F, 0.0F);
            matrixStack.scale(1.0F, -1.0F, 1.0F);
            matrixStack.scale(16.0F, 16.0F, 16.0F);

            if (!model.isSideLit()) {
                net.minecraft.client.renderer.RenderHelper.setupGuiFlatDiffuseLighting();
            }

            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GUI, false, matrixStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, model);
            ((IRenderTypeBuffer.Impl) buffer).finish();

            RenderSystem.enableDepthTest();

            if (!model.isSideLit()) {
                net.minecraft.client.renderer.RenderHelper.setupGui3DDiffuseLighting();
            }

            //noinspection deprecation
            RenderSystem.disableAlphaTest();
            //noinspection deprecation
            RenderSystem.disableRescaleNormal();

            matrixStack.pop();

            if (includeAmount && stack.getCount() > 1) {
                matrixStack.push();
                matrixStack.translate(x, y, 90);

                FontRenderer fr = Minecraft.getInstance().fontRenderer;
                String text = Integer.toString(stack.getCount());
                fr.renderString(text, (float)(17 - fr.getStringWidth(text)), 9, 16777215, true, matrixStack.getLast().getMatrix(), buffer, false, 0, 15728880);

                matrixStack.pop();
            }
        }
    }
}