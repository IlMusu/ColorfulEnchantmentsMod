package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.client.callbacks.RegisterBookModelsCallback;
import com.ilmusu.colorfulenchantments.client.models.items.ModelResourceOverrider;
import com.ilmusu.colorfulenchantments.items.EnchantedColoredBookHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ModItems
{
    public static void register()
    {
    }

    @Environment(EnvType.CLIENT)
    public static void registerModels()
    {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(ModelResourceOverrider::new);

        RegisterBookModelsCallback.EVENT.register(() -> List.of(
            Resources.ENCHANTED_COLORED_BOOK_IDENTIFIER,
            Resources.CURSED_COLORED_BOOK_IDENTIFIER
        ));
    }

    @Environment(EnvType.CLIENT)
    public static void registerColors()
    {
        // Registering the item colors for the enchanted book in order to have different color variations
        ColorProviderRegistry.ITEM.register(EnchantedColoredBookHelper::getTint, Items.ENCHANTED_BOOK);
    }
}
