package com.ilmusu.colorfulenchantments.mixins.mixin;

import com.ilmusu.colorfulenchantments.callbacks.BufferBuilderCreateCallback;
import com.ilmusu.colorfulenchantments.callbacks.ShaderProgramsLoadingCallback;
import com.ilmusu.colorfulenchantments.callbacks.WorldRendererLayersCallback;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceFactory;
import org.joml.Matrix4f;
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
        @Inject(method = "loadPrograms", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 54, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
        public void registerCustomShadersPrograms(ResourceFactory factory, CallbackInfo ci, List<?> list, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) throws IOException
        {
            // Gathering all the custom shaders and adding them to the list of shaders
            list2.addAll(ShaderProgramsLoadingCallback.AFTER.invoker().handler(factory));
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