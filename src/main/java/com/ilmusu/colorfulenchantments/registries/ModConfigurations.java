package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.Configuration;
import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.utils.ModUtils;

public class ModConfigurations
{
    private static Configuration ENCHANTMENT_COLORS_CONFIG;

    public static Configuration getEnchantmentColors()
    {
        return ENCHANTMENT_COLORS_CONFIG;
    }

    public static void register()
    {
        ENCHANTMENT_COLORS_CONFIG = new Configuration(Resources.MOD_ID, "enchantments_colors", ModConfigurations::colorStringToIntString);
    }

    private static String colorStringToIntString(String name, String value)
    {
        return Integer.toString(ModUtils.colorFromString(value).getRGB());
    }
}
