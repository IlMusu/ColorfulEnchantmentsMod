package com.ilmusu.colorfulenchantments.items;

import com.ilmusu.colorfulenchantments.client.models.items.EnchantedBookOverrides;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

public class EnchantedColoredBookHelper
{
    @Environment(EnvType.CLIENT)
    public static int getTint(ItemStack stack, int modelLayer)
    {
        return switch (modelLayer)
        {
            default -> 0xFFFFFF;
            case 1 -> EnchantedBookOverrides.getEnchantmentColor(stack);
        };
    }
}
