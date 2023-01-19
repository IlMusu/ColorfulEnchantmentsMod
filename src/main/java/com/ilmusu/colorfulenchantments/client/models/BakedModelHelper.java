package com.ilmusu.colorfulenchantments.client.models;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.*;

import java.util.*;
import java.util.stream.Collectors;

public class BakedModelHelper
{
    enum BorderOperation { MAIN, MAIN_INVERSE, LAYER }

    @SuppressWarnings("DataFlowIssue")
    public static Mesh createItemMesh(Sprite main, Sprite... layers)
    {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        putSprite(emitter, 0, main, Direction.NORTH);
        putSprite(emitter, 0, main, Direction.SOUTH);
        putSpriteBorders(emitter, 0, BorderOperation.MAIN, main, layers);

        return builder.build();
    }

    @SuppressWarnings("DataFlowIssue")
    public static Mesh createItemInverseMesh(Sprite main, Sprite empty, Sprite... layers)
    {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        for(int i=0; i<layers.length; ++i)
        {
            putSprite(emitter, 0, empty, Direction.NORTH);
            putSprite(emitter, 0, empty, Direction.SOUTH);
        }
        putSpriteBorders(emitter, 0, BorderOperation.MAIN_INVERSE, main, layers);

        return builder.build();
    }

    @SuppressWarnings("DataFlowIssue")
    public static Mesh createItemLayer(Sprite main, Sprite layer, int layerIndex)
    {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        putSprite(emitter, layerIndex, layer, Direction.NORTH);
        putSprite(emitter, layerIndex, layer, Direction.SOUTH);
        putSpriteBorders(emitter, layerIndex, BorderOperation.LAYER, main, layer);

        return builder.build();
    }

    protected static void putSpriteBorders(QuadEmitter emitter, int layer, BorderOperation operation, Sprite main, Sprite... layers)
    {
        for(int x=0; x<16; ++x)
            for(int y=0; y<16; ++y)
                putPixelBorders(emitter, layer, x, y, operation, main, layers);
    }

    // This method takes a sprite and the position of a pixel in that sprite and generates the necessary pixels
    // on the border of the item. It is possible to specify a mask to avoid generating overlapping borders:
    // The border is generated only if the masks would generate a border in the same position
    protected static void putPixelBorders(QuadEmitter emitter, int layer, int x, int y, BorderOperation operation, Sprite main, Sprite... layers)
    {
        List<Direction> transpDirs = List.of();
        Sprite sprite = null;
        if(operation == BorderOperation.MAIN)
        {
            transpDirs = sidesForMain(x, y, main, layers);
            sprite = main;
        }
        else if(operation == BorderOperation.MAIN_INVERSE)
        {
            transpDirs = sidesForInverseMain(x, y, main, layers);
            sprite = main;
        }
        else if(operation == BorderOperation.LAYER)
        {
            transpDirs = sidesForLayer(x, y, main, layers[0]);
            sprite = layers[0];
        }

        Vec2f uv = new Vec2f(x+0.5F, y+0.5F);
        float px = x+0.5F;
        float py = 15-y+0.5F;

        for(Direction dir : transpDirs)
        {
            switch (dir)
            {
                case UP   -> putPixel(emitter, sprite, dir, new Vec3d(px, py+0.5F, 8), uv);
                case DOWN -> putPixel(emitter, sprite, dir, new Vec3d(px, py-0.5, 8), uv);
                case WEST -> putPixel(emitter, sprite, dir, new Vec3d(px-0.5, py, 8), uv);
                case EAST -> putPixel(emitter, sprite, dir, new Vec3d(px+0.5, py, 8), uv);
            }
            emitter.colorIndex(layer);
            emitter.emit();
        }
    }

    private static List<Direction> sidesForLayer(int x, int y, Sprite main, Sprite layer)
    {
        // The directions considering only the layer
        List<Direction> transpLayer = getTransparentSides(layer, x, y);
        // The directions considering only the main
        List<Direction> maskedMain = getTransparentSides(main, x, y);
        // Removing the pixels that are inside the main sprite
        transpLayer.removeIf((dir) -> !maskedMain.contains(dir));

        return transpLayer;
    }

    private static List<Direction> sidesForMain(int x, int y, Sprite main, Sprite... layers)
    {
        // The directions considering only the main sprite
        List<Direction> transpMain = getTransparentSides(main, x, y);
        // The directions that would be covered by the layers
        Set<Direction> transpLayer = Arrays.stream(layers)
                .parallel()
                .map((layer) -> getTransparentSides(layer, x, y))
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toSet());
        // Removing all the directions covered by the layers
        transpMain.removeIf(transpLayer::contains);

        return transpMain;
    }

    private static List<Direction> sidesForInverseMain(int x, int y, Sprite main, Sprite... layers)
    {
        // The directions considering only the main sprite
        List<Direction> transpMain = getTransparentSides(main, x, y);
        // The directions that would be covered by the layers
        Set<Direction> transpLayer = Arrays.stream(layers)
                .parallel()
                .map((layer) -> getTransparentSides(layer, x, y))
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toSet());
        // Removing all the directions covered by the layers
        transpMain.removeIf((dir) -> !transpLayer.contains(dir));

        return transpMain;
    }

    // Returns a list of the neighbours directions to cover
    private static List<Direction> getTransparentSides(Sprite sprite, int x, int y)
    {
        // If this pixel is transparent, do nothing
        if(sprite.isPixelTransparent(0, x, y))
            return new ArrayList<>();

        List<Direction> dirs = new ArrayList<>();
        int[][] toCheckPos = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };
        Direction[] toCheckDir = { Direction.EAST, Direction.UP, Direction.WEST, Direction.DOWN };

        for(int i=0; i<toCheckPos.length; ++i)
        {
            int[] side = toCheckPos[i];
            // The pixel must be contained in the texture
            if(!checkBounds(x + side[0], y - side[1]))
                dirs.add(toCheckDir[i]);
            else if(sprite.isPixelTransparent(0, x + side[0], y - side[1]))
                dirs.add(toCheckDir[i]);
        }

        return dirs;
    }

    private static void putSprite(QuadEmitter emitter, int layer, Sprite sprite, Direction normal)
    {
        if(normal == Direction.NORTH)
        {
            putQuad(emitter, sprite, normal, new Vec3d(8, 8, 7.5), new Vec2f(16, 16), new Vector4f(0, 16, 0, 16));
            emitter.spriteBake(0, sprite, MutableQuadView.BAKE_FLIP_U);
        }
        if(normal == Direction.SOUTH)
        {
            putQuad(emitter, sprite, normal, new Vec3d(8, 8, 8.5), new Vec2f(16, 16), new Vector4f(0, 16, 0, 16));
            emitter.spriteBake(0, sprite, MutableQuadView.BAKE_ROTATE_NONE);
        }
        emitter.colorIndex(layer);
        emitter.emit();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static void putPixel(QuadEmitter emitter, Sprite sprite, Direction normal, Vec3d pos, Vec2f uv)
    {
        putQuad(emitter, sprite, normal, pos, new Vec2f(1, 1), new Vector4f(uv.x, uv.x, uv.y, uv.y));
        emitter.spriteBake(0, sprite, MutableQuadView.BAKE_ROTATE_NONE);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static void putQuad(QuadEmitter emitter, Sprite sprite, Direction normal, Vec3d pos, Vec2f size, Vector4f uv)
    {
        final float PX = 1 / 16.0F;

        Vec3d n = new Vec3d(normal.getUnitVector());
        Vec3d b0 = new Vec3d(n.z, n.x, n.y);
        Vec3d b1 = n.crossProduct(b0);

        Vec3d e0 = b0.multiply(size.x).multiply(PX/2);
        Vec3d e1 = b1.multiply(size.y).multiply(PX/2);
        Vec3d s = pos.multiply(PX);

        Vec3f v0 = new Vec3f(s.subtract(e0).subtract(e1));
        Vec3f v1 = new Vec3f(s.add(e0).subtract(e1));
        Vec3f v2 = new Vec3f(s.add(e0).add(e1));
        Vec3f v3 = new Vec3f(s.subtract(e0).add(e1));

        putVertex(emitter, 0, v0, uv.getX(), uv.getW());
        putVertex(emitter, 1, v1, uv.getY(), uv.getW());
        putVertex(emitter, 2, v2, uv.getY(), uv.getZ());
        putVertex(emitter, 3, v3, uv.getX(), uv.getZ());

        emitter.spriteColor(0, -1, -1, -1, -1);
        emitter.nominalFace(normal);
    }

    private static void putVertex(QuadEmitter emitter, int index, Vec3f pos, float u, float v)
    {
        emitter.pos(index, pos);
        emitter.sprite(index, 0, u, v);
    }

    private static boolean checkBounds(int x, int y)
    {
        return x>=0 && x<=15 && y>=0 && y<=15;
    }
}
