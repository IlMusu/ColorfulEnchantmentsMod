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
    public ModelResourceOverrider(ResourceManager manager)
    {

    }

    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext context)
    {
        if(isResourceFor(identifier, Items.ENCHANTED_BOOK))
            return new EnchantedBookOverrideModel();
        return null;
    }

    protected static boolean isResourceFor(Identifier identifier, Item item)
    {
        Identifier i = Registries.ITEM.getId(item);
        return Objects.equals(identifier, new Identifier(i.getNamespace(), "item/"+i.getPath()));
    }
}
