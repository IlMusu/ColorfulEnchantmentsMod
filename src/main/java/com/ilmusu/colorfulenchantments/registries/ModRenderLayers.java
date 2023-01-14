package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.callbacks.BufferBuilderCreateCallback;
import com.ilmusu.colorfulenchantments.callbacks.ShaderProgramsLoadingCallback;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class ModRenderLayers extends RenderLayer
{
    private static net.minecraft.client.gl.ShaderProgram direct_white_glint_program;

    private static final RenderLayer DIRECT_WHITE_GLINT =
        RenderLayer.of("direct_white_glint",
            VertexFormats.POSITION_TEXTURE,
            VertexFormat.DrawMode.QUADS,
            256,
            MultiPhaseParameters.builder()
                .program(new ColoredShaderProgram(
                        () -> direct_white_glint_program,
                        new Color(0.25F, 0.25F, 0.25F, 0.25F).getRGB())
                )
                .texture(new RenderPhase.Texture(Resources.COLORED_GLINT_IDENTIFIER, true, false))
                .texturing(GLINT_TEXTURING)
                .transparency(RenderPhase.GLINT_TRANSPARENCY)
                .writeMaskState(RenderPhase.COLOR_MASK)
                .cull(DISABLE_CULLING)
                .depthTest(EQUAL_DEPTH_TEST)
                .build(false)
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
                        "rendertype_white_glint_direct",
                        VertexFormats.POSITION_TEXTURE),
                    shader -> direct_white_glint_program = shader
                )
            )
        );

        BufferBuilderCreateCallback.AFTER.register(() ->
            List.of(
                    getWhiteGlintDirect()
            )
        );
    }

    public static RenderLayer getWhiteGlintDirect()
    {
        return DIRECT_WHITE_GLINT;
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
