package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.callbacks.BookModelsRegisterCallback;
import com.ilmusu.colorfulenchantments.items.EnchantedColoredBookHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class ModItems
{
    public static void register()
    {
    }

    @Environment(EnvType.CLIENT)
    public static void registerColors()
    {
        ColorProviderRegistry.ITEM.register(EnchantedColoredBookHelper::getTint, Items.ENCHANTED_BOOK);
    }

    @Environment(EnvType.CLIENT)
    public static void registerModels()
    {
        BookModelsRegisterCallback.EVENT.register(() -> List.of(
            Resources.ENCHANTED_COLORED_BOOK_IDENTIFIER,
            Resources.CURSED_COLORED_BOOK_IDENTIFIER
        ));
    }

    @Environment(EnvType.CLIENT)
    public static void registerModelOverrides()
    {
        ModelPredicateProviderRegistry.register(Items.ENCHANTED_BOOK, Resources.identifier("enchantments"), (stack, world, entity, id) -> {
            NbtList enchantments = EnchantedBookItem.getEnchantmentNbt(stack);
            return MathHelper.clamp(enchantments.size(), 1.0F, 4.0F) / 4.0F;
        });
    }
}
