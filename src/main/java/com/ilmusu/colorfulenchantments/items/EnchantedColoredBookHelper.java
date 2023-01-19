package com.ilmusu.colorfulenchantments.items;

import com.ilmusu.colorfulenchantments.registries.ModConfigurations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Random;
import java.util.function.Supplier;

public class EnchantedColoredBookHelper
{
    protected static final int DEFAULT_LACE_COLOR = new Color(197, 19, 57).getRGB();
    protected static final int DEFAULT_PIN_COLOR =  new Color(224, 175, 27).getRGB();
    protected static final int DEFAULT_PIN_CURSE_COLOR = new Color(133, 22, 15).getRGB();
    protected static final int DEFAULT_PIN_TREASURE_COLOR = new Color(37, 117, 15).getRGB();

    @Environment(EnvType.CLIENT)
    public static int getTint(ItemStack stack, int layer)
    {
        if(layer == 0)
            return 0xFFFFFFFF;
        if(layer <= 4)
            return getLaceColor(stack, getEnchantmentIndexFromLayer(stack, layer));
        else
            return getPinColor(stack, getEnchantmentIndexFromLayer(stack, layer-4));
    }

    // Returns the index of the laces that must be rendered on the enchanted book
    public static boolean[] getLacesMask(ItemStack stack)
    {
        return switch (EnchantmentHelper.get(stack).size())
        {
            case 1  -> new boolean[] { false, false, true,  false };
            case 2  -> new boolean[] { false, true,  false, true  };
            case 3  -> new boolean[] { true,  true,  true,  false };
            case 4  -> new boolean[] { true,  true,  true,  true  };
            default -> new boolean[] { false, false, false, false };
        };
    }

    private static int getEnchantmentIndexFromLayer(ItemStack stack, int layer)
    {
        return switch (EnchantmentHelper.get(stack).size())
        {
            case 1 -> 0;
            case 2 -> layer == 2 ? 0 : 1;
            case 3, 4 -> layer-1;
            default -> -1;
        };
    }


    protected static @Nullable Identifier getEnchantmentIdentifier(ItemStack stack, int index)
    {
        // Gets the correct enchantment list from the stack
        NbtList list = stack.isOf(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantmentNbt(stack) : stack.getEnchantments();
        // Check if there is at least one enchantment, otherwise cannot return the first one
        if(list.size() <= index)
            return null;
        // Returning the identifier of the first enchantment
        return EnchantmentHelper.getIdFromNbt((NbtCompound)list.get(index));
    }

    protected static @Nullable Enchantment getEnchantment(ItemStack stack, int index)
    {
        Identifier identifier = getEnchantmentIdentifier(stack, index);
        if(identifier == null)
            return null;
        return Registry.ENCHANTMENT.get(identifier);
    }

    public static void registerLaceColor(Enchantment enchantment, Color color)
    {
        // Registering a default configuration
        String enchantmentStr = String.valueOf(Registry.ENCHANTMENT.getId(enchantment));
        String colorStr = Integer.toString(color.getRGB());
        ModConfigurations.getEnchantmentColors().setDefaultConfiguration(enchantmentStr, colorStr);
    }

    public static void overrideLaceColor(Enchantment enchantment, Color color)
    {
        // Overriding the current configuration
        String enchantmentStr = String.valueOf(Registry.ENCHANTMENT.getId(enchantment));
        String colorStr = Integer.toString(color.getRGB());
        ModConfigurations.getEnchantmentColors().setConfiguration(enchantmentStr, colorStr);
    }

    public static void resetLaceColor(Enchantment enchantment)
    {
        String enchantmentStr = String.valueOf(Registry.ENCHANTMENT.getId(enchantment));
        ModConfigurations.getEnchantmentColors().resetConfiguration(enchantmentStr);
    }

    protected static int getLaceColor(ItemStack stack, int index)
    {
        String enchantmentStr = String.valueOf(getEnchantmentIdentifier(stack, index));
        Supplier<String> random = EnchantedColoredBookHelper::randomLaceColor;
        String color = ModConfigurations.getEnchantmentColors().getConfigurationOrSet(enchantmentStr, random);
        return Integer.parseInt(color);
    }

    public static int getPinColor(ItemStack stack, int index)
    {
        Enchantment enchantment = getEnchantment(stack, index);
        if(enchantment == null)
            return DEFAULT_PIN_COLOR;
        if(enchantment.isCursed())
            return DEFAULT_PIN_CURSE_COLOR;
        if(enchantment.isTreasure())
            return DEFAULT_PIN_TREASURE_COLOR;
        return DEFAULT_PIN_COLOR;
    }

    public static boolean isCursed(ItemStack stack)
    {
        return EnchantmentHelper.get(stack).keySet().stream().anyMatch((Enchantment::isCursed));
    }

    private static String randomLaceColor()
    {
        Random rand = new Random();
        float hue = rand.nextFloat();
        float saturation = rand.nextFloat(0.25F, 0.75F);
        float brightness = rand.nextFloat(0.25F, 0.75F);
        Color color = Color.getHSBColor(hue, saturation, brightness);

        return Integer.toString(color.getRGB());
    }
}
