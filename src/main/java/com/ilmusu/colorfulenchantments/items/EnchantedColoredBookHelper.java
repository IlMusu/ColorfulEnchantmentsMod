package com.ilmusu.colorfulenchantments.items;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.registries.ModConfigurations;
import com.ilmusu.colorfulenchantments.utils.ModUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EnchantedColoredBookHelper
{
    // This map contains the data related to the specific enchantments
    // If the enchantment id is not present in the map, the default model will be used
    private static final Map<Identifier, ColoredEnchantmentData> COLORED_ENCHANTMENTS_DATA = new HashMap<>();

    @Environment(EnvType.CLIENT)
    public static int getTint(ItemStack stack, int modelLayer)
    {
        if(modelLayer == 0)
            return 0xFFFFFFFF;
        return getBookColor(stack, modelLayer-1);
    }

    protected static ColoredEnchantmentData getEnchantmentData(ItemStack stack, int index)
    {
        Identifier identifier = getEnchantmentIdentifier(stack, index);
        if(identifier == null)
            return null;
        return COLORED_ENCHANTMENTS_DATA.getOrDefault(identifier, null);
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
        return Registries.ENCHANTMENT.get(identifier);
    }

    public static BakedModel getBookModel(ItemStack stack)
    {
        boolean hasCurse = EnchantmentHelper.get(stack).keySet().stream().anyMatch((Enchantment::isCursed));
        ModelIdentifier newModel = hasCurse ? ColoredEnchantmentData.CURSE_MODEL : ColoredEnchantmentData.DEFAULT_MODEL;
        // Returning the BakedModel identifier by the ModelIdentifier
        BakedModelManager manager = MinecraftClient.getInstance().getItemRenderer().getModels().getModelManager();
        return manager.getModel(newModel);
    }

    public static void registerBookColor(Enchantment enchantment, Color color)
    {
        Identifier enchantmentID = Registries.ENCHANTMENT.getId(enchantment);

        // The color must be validated by the configuration
        color = getConfigurationColor(enchantment, color);

        ColoredEnchantmentData data = COLORED_ENCHANTMENTS_DATA.getOrDefault(enchantmentID, new ColoredEnchantmentData());
        data.setLaceColor(color);
        COLORED_ENCHANTMENTS_DATA.put(Registries.ENCHANTMENT.getId(enchantment), data);
    }

    public static void overrideBookColor(Enchantment enchantment, Color color)
    {
        Identifier enchantmentID = Registries.ENCHANTMENT.getId(enchantment);

        // Overriding the current configuration color
        setConfigurationColor(enchantment, color);

        ColoredEnchantmentData data = COLORED_ENCHANTMENTS_DATA.getOrDefault(enchantmentID, new ColoredEnchantmentData());
        data.setLaceColor(color);
        COLORED_ENCHANTMENTS_DATA.put(Registries.ENCHANTMENT.getId(enchantment), data);
    }

    public static int getBookColor(ItemStack stack, int index)
    {
        ColoredEnchantmentData data = getEnchantmentData(stack, index);

        // If the data for the enchantment do not exist, registering a new one
        if(data == null || !data.colorInitialized)
        {
            // Gets the first enchantment from the book
            Enchantment enchantment = getEnchantment(stack, index);
            if(enchantment == null)
                return ColoredEnchantmentData.DEFAULT_LACE_COLOR;
            // Registers a random color and then returns it
            registerBookColor(enchantment, randomColor());
            data = getEnchantmentData(stack, index);
        }

        // Returning the found enchantment color
        assert data != null;
        return data.getLaceColor();
    }

    private static Color getConfigurationColor(Enchantment enchantment, Color color)
    {
        Identifier enchantmentID = Registries.ENCHANTMENT.getId(enchantment);

        // The color is set to the configuration if not already existing, these are set by the player
        String enchantmentStr = String.valueOf(enchantmentID);
        String colorStr = ModUtils.colorToString(color);
        ModConfigurations.getEnchantmentColors().setNonExistentConfiguration(enchantmentStr, colorStr);
        // Getting the color from the configuration which will be the actual color to use
        return ModUtils.colorFromString(ModConfigurations.getEnchantmentColors().getConfiguration(enchantmentStr));
    }

    private static void setConfigurationColor(Enchantment enchantment, Color color)
    {
        Identifier enchantmentID = Registries.ENCHANTMENT.getId(enchantment);

        // The color is set to the configuration if not already existing, these are set by the player
        String enchantmentStr = String.valueOf(enchantmentID);
        String colorStr = ModUtils.colorToString(color);
        ModConfigurations.getEnchantmentColors().setConfiguration(enchantmentStr, colorStr);
    }

    private static Color randomColor()
    {
        Random rand = new Random();
        return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    }

    protected static class ColoredEnchantmentData
    {
        protected static final ModelIdentifier DEFAULT_MODEL = toModelIdentifier(Resources.ENCHANTED_COLORED_BOOK_IDENTIFIER);
        protected static final ModelIdentifier CURSE_MODEL = toModelIdentifier(Resources.CURSED_COLORED_BOOK_IDENTIFIER);
        protected static final int DEFAULT_LACE_COLOR = new Color(197, 19, 57).getRGB();

        protected boolean colorInitialized = false;
        protected int laceColor;


        public ColoredEnchantmentData()
        {

        }

        public void setLaceColor(Color laceColor)
        {
            this.colorInitialized = true;
            this.laceColor = laceColor.getRGB();
        }

        public int getLaceColor()
        {
            if(!this.colorInitialized)
                return DEFAULT_LACE_COLOR;
            return this.laceColor;
        }

        public static ModelIdentifier toModelIdentifier(Identifier identifier)
        {
            return new ModelIdentifier(identifier, "inventory");
        }
    }
}
