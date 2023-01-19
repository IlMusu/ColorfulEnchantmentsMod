package com.ilmusu.colorfulenchantments.client.models.items;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ModelResourceOverrider implements ModelResourceProvider
{
    public ModelResourceOverrider(ResourceManager resourceManager)
    {

    }

    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context)
    {
        if(isResourceFor(resourceId, Items.ENCHANTED_BOOK))
            return new EnchantedColoredBookModel();
        return null;
    }

    protected static boolean isResourceFor(Identifier resource, Item item)
    {
        Identifier i = Registries.ITEM.getId(item);
        return Objects.equals(resource, new Identifier(i.getNamespace(), "item/"+i.getPath()));
    }
}
