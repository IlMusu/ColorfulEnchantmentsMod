package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.client.models.items.EnchantedBookOverrides;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;

import java.awt.*;

public class ModEnchantments
{
    public static void registerColors()
    {
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.PROTECTION, new Color(178, 184, 9));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.FIRE_PROTECTION, new Color(176, 61, 16));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.FEATHER_FALLING, new Color(161, 157, 156));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.BLAST_PROTECTION, new Color(99, 96, 96));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.PROJECTILE_PROTECTION, new Color(59, 55, 55));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.RESPIRATION, new Color(8, 194, 157));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.AQUA_AFFINITY, new Color(7, 157, 184));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.THORNS, new Color(16, 99, 24));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.DEPTH_STRIDER, new Color(17, 113, 166));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.FROST_WALKER, new Color(73, 231, 235));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.SOUL_SPEED, new Color(106, 82, 68));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.SWIFT_SNEAK, new Color(81, 105, 103));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.SHARPNESS, new Color(128, 27, 27));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.SHARPNESS, new Color(23, 64, 17));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.SHARPNESS, new Color(37, 156, 20));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.KNOCKBACK, new Color(26, 25, 25));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.FIRE_ASPECT, new Color(219, 24, 24));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.LOOTING, new Color(209, 209, 13));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.SWEEPING, new Color(189, 189, 185));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.EFFICIENCY, new Color(122, 38, 128));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.SILK_TOUCH, new Color(202, 145, 207));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.UNBREAKING, new Color(132, 127, 133));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.FORTUNE, new Color(44, 148, 44));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.POWER, new Color(230, 188, 146));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.PUNCH, new Color(227, 124, 27));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.FLAME, new Color(219, 57, 29));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.INFINITY, new Color(61, 42, 82));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.LUCK_OF_THE_SEA, new Color(11, 38, 94));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.LURE, new Color(64, 57, 44));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.LOYALTY, new Color(97, 52, 102));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.IMPALING, new Color(201, 66, 111));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.RIPTIDE, new Color(95, 104, 107));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.CHANNELING, new Color(43, 74, 50));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.MULTISHOT, new Color(79, 102, 219));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.QUICK_CHARGE, new Color(74, 70, 69));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.PIERCING, new Color(168, 42, 40));
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.MENDING, new Color(166, 237, 83));

        EnchantedBookOverrides.overrideEnchantmentBookModel(Enchantments.BINDING_CURSE, Resources.CURSED_COLORED_BOOK_IDENTIFIER);
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.BINDING_CURSE, new Color(31, 3, 3));

        EnchantedBookOverrides.overrideEnchantmentBookModel(Enchantments.VANISHING_CURSE, Resources.CURSED_COLORED_BOOK_IDENTIFIER);
        EnchantedBookOverrides.overrideEnchantmentBookColor(Enchantments.VANISHING_CURSE, new Color(31, 3, 3));
    }
}
