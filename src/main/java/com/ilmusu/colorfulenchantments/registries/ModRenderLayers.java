package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.client.callbacks.ShaderProgramsLoadingCallback;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Util;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModRenderLayers extends RenderLayer
{
    private static net.minecraft.client.gl.ShaderProgram colored_glint_direct_program;

    private static final Function<Integer, RenderLayer> COLORED_GLINT_DIRECT = Util.memoize((color) ->
            RenderLayer.of("colored_glint_direct",
                VertexFormats.POSITION_TEXTURE,
                VertexFormat.DrawMode.QUADS,
                64,
                MultiPhaseParameters.builder()
                        .program(new ColoredShaderProgram(() -> colored_glint_direct_program, color))
                        .texture(new RenderPhase.Texture(Resources.COLORED_GLINT_IDENTIFIER, true, false))
                        .texturing(GLINT_TEXTURING)
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                        .writeMaskState(COLOR_MASK)
                        .cull(DISABLE_CULLING)
                        .depthTest(EQUAL_DEPTH_TEST)
                        .build(false)
            )
    );

    public ModRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
    {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static void register()
    {
        ShaderProgramsLoadingCallback.AFTER.register((manager) ->
                List.of(
                        Pair.of(
                                new net.minecraft.client.gl.ShaderProgram(
                                        manager,
                                        "rendertype_colored_glint_direct",
                                        VertexFormats.POSITION_TEXTURE),
                                shader -> colored_glint_direct_program = shader
                        )
                ));
    }

    public static RenderLayer getColoredGlintDirect(int color)
    {
        // Creating the layer
        RenderLayer layer = COLORED_GLINT_DIRECT.apply(color);
        // Adding a new buffer for the layer
        BufferBuilderStorage storage = MinecraftClient.getInstance().getBufferBuilders();
        storage.entityBuilders.putIfAbsent(layer, new BufferBuilder(layer.getExpectedBufferSize()));
        return layer;
    }

    @Environment(EnvType.CLIENT)
    protected static class ColoredShaderProgram extends ShaderProgram
    {
        protected final float[] color = new float[4];

        public ColoredShaderProgram(Supplier<net.minecraft.client.gl.ShaderProgram> supplier, int colorCode)
        {
            super(supplier);
            // Converting the color code to color components
            this.color[0] = ((colorCode >> 16) & 0xFF) / 255F;
            this.color[1] = ((colorCode >> 8) & 0xFF) / 255F;
            this.color[2] = ((colorCode) & 0xFF) / 255F;
            this.color[3] = ((colorCode >> 24) & 0xFF) / 255F;
        }

        @Override
        public void startDrawing()
        {
            super.startDrawing();
            RenderSystem.setShaderColor(color[0], color[1], color[2], color[3]);
        }

        @Override
        public void endDrawing()
        {
            super.endDrawing();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
