package com.ilmusu.colorfulenchantments.mixins.mixin;

import com.ilmusu.colorfulenchantments.registries.ModRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class EnchantedColoredBookMixin
{
    @Mixin(ItemRenderer.class)
    public abstract static class OverrideEnchantedBookGlintShader
    {
        private static ItemStack enchantedBookStack;

        @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"))
        public void overrideDefaultGlintShaderHook(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci)
        {
            if(stack.hasGlint() && stack.getItem() == Items.ENCHANTED_BOOK)
                OverrideEnchantedBookGlintShader.enchantedBookStack = stack;
        }

        @Inject(method = "getDirectItemGlintConsumer", at = @At("HEAD"), cancellable = true)
        private static void overrideDefaultGlintShader(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, CallbackInfoReturnable<VertexConsumer> cir)
        {
            // Overriding the glint color only if the ItemStack book was previously set
            if(OverrideEnchantedBookGlintShader.enchantedBookStack == null)
                return;
            // Returning the correct glint RenderLayer to render
            OverrideEnchantedBookGlintShader.enchantedBookStack = null;
            RenderLayer coloredGlintLayer = ModRenderLayers.getWhiteGlintDirect();
            cir.setReturnValue(VertexConsumers.union(provider.getBuffer(coloredGlintLayer), provider.getBuffer(layer)));
        }
    }
}
