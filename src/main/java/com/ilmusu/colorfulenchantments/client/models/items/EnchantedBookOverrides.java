package com.ilmusu.colorfulenchantments.client.models.items;

import com.ilmusu.colorfulenchantments.Resources;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.data.client.Model;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
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

public class EnchantedBookOverrides extends ModelOverrideList
{
    public static final EnchantedBookOverrides INSTANCE = new EnchantedBookOverrides();

    // This map contains the data related to the specific enchantments
    // If the enchantment id is not present in the map, the default model will be used
    private static final Map<Identifier, EnchantmentData> ENCHANTMENT_DATA = new HashMap<>();


    public static void overrideEnchantmentBookModel(Enchantment enchantment, Identifier model)
    {
        // Getting the existing data or a new one
        Identifier enchantmentID = Registries.ENCHANTMENT.getId(enchantment);
        EnchantmentData data = ENCHANTMENT_DATA.getOrDefault(enchantmentID, new EnchantmentData());
        // Setting the model to the data
        data.setModelIdentifier(model);
        // Resetting the data, now containing the model, to the map
        ENCHANTMENT_DATA.put(Registries.ENCHANTMENT.getId(enchantment), data);
    }

    public static void overrideEnchantmentBookColor(Enchantment enchantment, Color color)
    {
        // Getting the existing data or a new one
        Identifier enchantmentID = Registries.ENCHANTMENT.getId(enchantment);
        EnchantmentData data = ENCHANTMENT_DATA.getOrDefault(enchantmentID, new EnchantmentData());
        // Setting the model to the data
        data.setLaceColor(color);
        // Resetting the data, now containing the model, to the map
        ENCHANTMENT_DATA.put(Registries.ENCHANTMENT.getId(enchantment), data);
    }

    protected static EnchantmentData getEnchantmentData(ItemStack stack)
    {
        Identifier identifier = getFirstEnchantment(stack);
        if(identifier == null)
            return null;
        return ENCHANTMENT_DATA.getOrDefault(identifier, null);
    }

    public static int getLaceColor(ItemStack stack)
    {
        EnchantmentData data = getEnchantmentData(stack);
        if(data == null)
            return EnchantmentData.DEFAULT_LACE_COLOR;
        // Returning the found enchantment color
        return data.getLaceColor();
    }

    public static boolean shouldOverrideGlintColor(ItemStack stack)
    {
        // Defaulting to base color if the color does not exist
        EnchantmentData data = getEnchantmentData(stack);
        if(data == null)
            return false;
        return data.shouldOverrideGlintColor();
    }

    public static int getGlintColor(ItemStack stack)
    {
        if(stack.getItem() == Items.ENCHANTED_BOOK)
            return getBookGlintColor(stack);
        return getItemGlintColor(stack);
    }

    public static int getBookGlintColor(ItemStack stack)
    {
        Identifier identifier = getFirstEnchantment(stack);
        return ENCHANTMENT_DATA.get(identifier).bookGlintColor;
    }

    public static int getItemGlintColor(ItemStack stack)
    {
        Identifier identifier = getFirstEnchantment(stack);
        return ENCHANTMENT_DATA.get(identifier).itemGlintColor;
    }

    @Nullable
    @Override
    public BakedModel apply(BakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed)
    {
        if(stack.getItem() != Items.ENCHANTED_BOOK)
            return model;
        // Obtaining the correct model identifier for the enchantment
        ModelIdentifier bookModelIdentifier = EnchantmentData.DEFAULT_MODEL;
        EnchantmentData data = getEnchantmentData(stack);
        if(data != null)
            bookModelIdentifier = data.getModelIdentifier();
        // Returning the model from the identifier
        BakedModelManager manager = MinecraftClient.getInstance().getItemRenderer().getModels().getModelManager();
        return manager.getModel(bookModelIdentifier);
    }

    protected static @Nullable Identifier getFirstEnchantment(ItemStack stack)
    {
        // Gets the correct enchantment list from the stack
        NbtList list = stack.isOf(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantmentNbt(stack) : stack.getEnchantments();
        // Check if there is at least one enchantment, otherwise cannot return the first one
        if(list.size() == 0)
            return null;
        // Returning the identifier of the first enchantment
        return EnchantmentHelper.getIdFromNbt((NbtCompound)list.get(0));
    }


    protected static class EnchantmentData
    {
        protected static final ModelIdentifier DEFAULT_MODEL = toModelIdentifier(Resources.ENCHANTED_COLORED_BOOK_IDENTIFIER);
        protected static final int DEFAULT_LACE_COLOR = new Color(197, 19, 57).getRGB();

        protected boolean modelInitialized = false;
        protected ModelIdentifier modelIdentifier;

        protected boolean colorInitialized = false;
        protected int laceColor;
        protected int bookGlintColor;
        protected int itemGlintColor;


        public EnchantmentData()
        {

        }

        public void setModelIdentifier(Identifier identifier)
        {
            this.modelInitialized = true;
            this.modelIdentifier = toModelIdentifier(identifier);
        }

        public void setLaceColor(Color laceColor)
        {
            this.colorInitialized = true;
            this.laceColor = laceColor.getRGB();
            this.bookGlintColor = getBookGlintColor(laceColor);
            this.itemGlintColor = getItemGlintColor(laceColor);
        }

        public ModelIdentifier getModelIdentifier()
        {
            if(!this.modelInitialized)
                return DEFAULT_MODEL;
            return this.modelIdentifier;
        }

        public int getLaceColor()
        {
            if(!this.colorInitialized)
                return DEFAULT_LACE_COLOR;
            return this.laceColor;
        }

        public boolean shouldOverrideGlintColor()
        {
            return this.colorInitialized;
        }

        public static int getBookGlintColor(Color laceColor)
        {
            // Fix the brightness in order to see darker colors
            int glintColor = adjustBrightness(laceColor, 0.6F, 0.7F);
            // Resetting transparency and then setting it
            return (glintColor & (0x00FFFFFF)) | (50 << 24);
        }

        public static int getItemGlintColor(Color laceColor)
        {
            // Fix the brightness in order to see darker colors
            int glintColor = adjustBrightness(laceColor, 0.6F, 0.7F);
            // Resetting transparency and then setting it
            return (glintColor & (0x00FFFFFF)) | (200 << 24);
        }

        public static int adjustBrightness(Color color, float bMin, float bMax)
        {
            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            hsb[2] = (bMax - bMin) * hsb[2] + bMin;
            return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        }

        public static ModelIdentifier toModelIdentifier(Identifier identifier)
        {
            return new ModelIdentifier(identifier, "inventory");
        }
    }
}
