package com.ilmusu.colorfulenchantments.client.models;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public interface ItemBakedModel extends BakedModel, FabricBakedModel
{
    @Override
    default boolean isVanillaAdapter()
    {
        // False in order for the Fabric API to recognize this model as a custom one
        // and enable the emitter stack-dependant functions
        return false;
    }

    @Override
    default List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random)
    {
        // This is not called since the isVanillaAdapter() is false
        return List.of();
    }

    @Override
    default void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context)
    {
        // Not used, since this is an item
    }

    @Override
    default boolean useAmbientOcclusion()
    {
        return false;
    }

    @Override
    default boolean hasDepth()
    {
        return false;
    }

    @Override
    default boolean isSideLit()
    {
        // This is an item, so return false to render like an item
        return false;
    }

    @Override
    default boolean isBuiltin()
    {
        return false;
    }
}
