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
        ENCHANTMENT_COLORS_CONFIG = new Configuration(Resources.MOD_ID, "enchantments_colors", ModConfigurations::fixColor);
    }

    private static String fixColor(String name, String value)
    {
        if(ModUtils.isInRGBArrayFormat(value))
            return Integer.toString(ModUtils.fromRGBArrayString(value).getRGB());
        return Integer.toString(ModUtils.safeParseInt(value));
    }
}
