package com.ilmusu.colorfulenchantments.mixins.mixin;

import com.ilmusu.colorfulenchantments.client.callbacks.BookModelsRegisterCallback;
import com.ilmusu.colorfulenchantments.client.callbacks.ShaderProgramsLoadingCallback;
import com.ilmusu.colorfulenchantments.client.models.items.EnchantedBookOverrides;
import com.ilmusu.colorfulenchantments.registries.ModRenderLayers;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class CustomCallbacksMixin
{
    @Mixin(GameRenderer.class)
    public static abstract class GameRendererCallbacks
    {
        @Inject(method = "loadPrograms", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 54, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
        public void registerCustomShadersPrograms(ResourceFactory factory, CallbackInfo ci, List<?> list, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) throws IOException
        {
            // Adding to the list of shaders the custom shaders
            list2.addAll(ShaderProgramsLoadingCallback.AFTER.invoker().handler(factory));
        }
    }

    @Mixin(ModelLoader.class)
    public abstract static class ModelLoaderCallbacks
    {
        @Shadow
        protected abstract void addModel(ModelIdentifier modelId);

        @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 3))
        public void overrideBookModels(BlockColors blockColors, Profiler profiler, Map<?,?> jsonUnbakedModels, Map<?,?> blockStates, CallbackInfo ci)
        {
            // Obtaining the models as the item model resources and mapping them to ModelIdentifier
            List<ModelIdentifier> models =  BookModelsRegisterCallback.EVENT.invoker().handler().stream().map(
                    (model) -> new ModelIdentifier(model, "inventory")
            ).toList();
            // Loading all the custom enchanted book models
            for(ModelIdentifier model : models)
                this.addModel(model);
        }
    }

    @Mixin(ItemRenderer.class)
    public abstract static class OverrideEnchantedBookGlintShader
    {
        private static ItemStack stack;

        @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"))
        public void getDirectItemColoredGlintConsumerHook(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci)
        {
            OverrideEnchantedBookGlintShader.stack = stack;
        }

        @Inject(method = "getDirectItemGlintConsumer", at = @At("HEAD"), cancellable = true)
        private static void getDirectItemColoredGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint, CallbackInfoReturnable<VertexConsumer> cir)
        {
            if(glint)
            {
                if(!EnchantedBookOverrides.shouldOverrideGlintColor(stack))
                    return;

                // Creating the render layer for the glint with the specified color
                int glintColor = EnchantedBookOverrides.getGlintColor(stack);
                RenderLayer coloredGlintLayer = ModRenderLayers.getColoredGlintDirect(glintColor);
                // Creating the vertex consumer combining the item rendering and the glint
                cir.setReturnValue(VertexConsumers.union(provider.getBuffer(coloredGlintLayer), provider.getBuffer(layer)));
            }
        }
    }
}