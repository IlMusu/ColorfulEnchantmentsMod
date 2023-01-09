package com.ilmusu.colorfulenchantments.client.models.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class EnchantedBookOverrideModel implements UnbakedModel, BakedModel
{
    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader)
    {

    }

    @Override
    public Collection<Identifier> getModelDependencies()
    {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId)
    {
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean isBuiltin()
    {
        return false;
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return false;
    }

    @Override
    public boolean hasDepth()
    {
        return false;
    }

    @Override
    public boolean isSideLit()
    {
        return false;
    }

    @Override
    public Sprite getParticleSprite()
    {
        return null;
    }

    @Override
    public ModelTransformation getTransformation()
    {
        return ModelTransformation.NONE;
    }

    @Override
    public ModelOverrideList getOverrides()
    {
        // Loads the overrides, which is the actual model in this case
        // This is done in order to have multiple models for the same item
        return EnchantedBookOverrides.INSTANCE;
    }
}
