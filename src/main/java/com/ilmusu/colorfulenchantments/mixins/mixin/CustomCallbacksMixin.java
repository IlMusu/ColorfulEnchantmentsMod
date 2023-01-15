package com.ilmusu.colorfulenchantments.mixins.mixin;

import com.ilmusu.colorfulenchantments.callbacks.BookModelsRegisterCallback;
import com.ilmusu.colorfulenchantments.callbacks.BufferBuilderCreateCallback;
import com.ilmusu.colorfulenchantments.callbacks.ShaderProgramsLoadingCallback;
import com.ilmusu.colorfulenchantments.callbacks.WorldRendererLayersCallback;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public abstract class CustomCallbacksMixin
{
    @Mixin(GameRenderer.class)
    public static abstract class GameRendererCallbacks
    {
        @Inject(method = "loadShaders", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 53, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
        public void registerCustomShadersPrograms(ResourceManager manager, CallbackInfo ci, List<?> list, List<Pair<Shader, Consumer<Shader>>> list2) throws IOException
        {
            // Gathering all the custom shaders and adding them to the list of shaders
            list2.addAll(ShaderProgramsLoadingCallback.AFTER.invoker().handler(manager));
        }
    }

    @Mixin(ModelLoader.class)
    public abstract static class ModelLoaderCallbacks
    {
        @Shadow
        protected abstract void addModel(ModelIdentifier modelId);

        @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 3))
        public void overrideEnchantedBookModels(ResourceManager resourceManager, BlockColors blockColors, Profiler profiler, int i, CallbackInfo ci)
        {
            // The custom models for the EnchantmentBook are not automatically loaded since they are not linked to any
            // actual item. Therefore, it is necessary to manually add them the models loading logic.

            // It is assumed that the files describing the custom models follow the same structure of item models.
            // Which mean that they are located in the same folder of the item models :
            //   resources/assets/[MOD_ID]/models/item/[CUSTOM_BOOK_MODEL].json
            // Therefore, it is possible to identify a model using only an Identifier:
            //   new Identifier(MOD_ID, CUSTOM_MODEL)

            // To load the custom model, it is necessary to create a ModelIdentifier: this EVENT gathers all the
            // Identifiers which represent the custom models and transforms them into a ModelIdentifier.
            List<ModelIdentifier> models =  BookModelsRegisterCallback.EVENT.invoker().handler().stream().map(
                    (identifier) -> new ModelIdentifier(identifier, "inventory")
            ).toList();

            // Finally, it is possible to load all the custom models into the models loading logic.
            for(ModelIdentifier model : models)
                this.addModel(model);
        }
    }

    @Mixin(BufferBuilderStorage.class)
    public abstract static class BufferBuilderStorageCallbacks
    {
        @Shadow
        private static void assignBufferBuilder(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> builderStorage, RenderLayer layer)
        {
        }

        @Inject(method = "method_22999", at = @At("TAIL"))
        public void addBufferBuildersForCustomRenderLayers(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map, CallbackInfo ci)
        {
            // Gathering all the RenderLayers that require a custom BufferBuilder
            BufferBuilderCreateCallback.AFTER.invoker().handler().forEach((layer) ->
                    assignBufferBuilder(map, layer)
            );
        }
    }

    @Mixin(WorldRenderer.class)
    public abstract static class WorldRendererCallbacks
    {
        @Shadow @Final private BufferBuilderStorage bufferBuilders;

        @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw(Lnet/minecraft/client/render/RenderLayer;)V", ordinal = 20, shift = At.Shift.AFTER))
        public void addBufferBuildersForCustomRenderLayers(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci)
        {
            // Gathering all the RenderLayers that require rendering and drawing them
            VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
            WorldRendererLayersCallback.AFTER_DIRECT_GLINT.invoker().handler().forEach(immediate::draw);
        }
    }
}