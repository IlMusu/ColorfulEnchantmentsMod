package com.ilmusu.colorfulenchantments.mixins.mixin;

import com.ilmusu.colorfulenchantments.registries.ModRenderLayers;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.ItemRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class EnchantedColoredBookMixin
{
    @Mixin(ItemRenderContext.class)
    public abstract static class ItemRenderContextOverrideShader
    {
        @Shadow private ItemStack itemStack;
        @Shadow private VertexConsumerProvider vertexConsumerProvider;

        @Inject(method = "selectVertexConsumer", at = @At("HEAD"), cancellable = true)
        public void overrideFabricConsumer(RenderLayer layerIn, CallbackInfoReturnable<VertexConsumer> cir)
        {
            if(!this.itemStack.hasGlint() || this.itemStack.getItem() != Items.ENCHANTED_BOOK)
                return;

            // Returning the correct glint RenderLayer to render
            RenderLayer coloredGlintLayer = ModRenderLayers.getWhiteGlintDirect();
            VertexConsumerProvider provider = this.vertexConsumerProvider;
            cir.setReturnValue(VertexConsumers.union(provider.getBuffer(coloredGlintLayer), provider.getBuffer(layerIn)));
        }
    }
}
