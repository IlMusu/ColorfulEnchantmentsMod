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

    // This map contains the models that need to be used for a specific enchantment
    // If the enchantment id is not present in the map, the default model will be used
    private static final Map<Identifier, ModelIdentifier> BOOK_MODELS = new HashMap<>();
    private static final ModelIdentifier DEFAULT_BOOK_MODEL = toModelIdentifier(Resources.ENCHANTED_COLORED_BOOK_IDENTIFIER);
    // This map contains the colors that need to be used for a specific enchantment
    private static final Map<Identifier, Integer> BOOK_COLORS = new HashMap<>();
    private static final Integer DEFAULT_BOOK_COLOR = new Color(197, 19, 57).getRGB();


    public static void overrideEnchantmentBookModel(Enchantment enchantment, Identifier model)
    {
        BOOK_MODELS.put(Registries.ENCHANTMENT.getId(enchantment), toModelIdentifier(model));
    }

    public static void overrideEnchantmentBookColor(Enchantment enchantment, Color color)
    {
        BOOK_COLORS.put(Registries.ENCHANTMENT.getId(enchantment), color.getRGB());
    }

    public static void overrideEnchantmentBook(Enchantment enchantment, Identifier model, Color color)
    {
        overrideEnchantmentBookModel(enchantment, toModelIdentifier(model));
        overrideEnchantmentBookColor(enchantment, color);
    }

    public static int getEnchantmentColor(ItemStack stack)
    {
        Identifier identifier = getFirstEnchantment(stack);
        // Defaulting to base red if the color does not exist
        if(identifier == null || !BOOK_COLORS.containsKey(identifier))
            return DEFAULT_BOOK_COLOR;
        // Returning the found enchantment color
        return BOOK_COLORS.get(identifier);
    }

    @Nullable
    @Override
    public BakedModel apply(BakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed)
    {
        if(stack.getItem() != Items.ENCHANTED_BOOK)
            return model;
        // Gets the first enchantment from the stack, only that is considered for the coloring
        Identifier enchantmentIdentifier = getFirstEnchantment(stack);
        if(enchantmentIdentifier == null)
            return model;
        // Obtaining the correct model identifier for the enchantment
        ModelIdentifier bookModelIdentifier = DEFAULT_BOOK_MODEL;
        if(BOOK_MODELS.containsKey(enchantmentIdentifier))
            bookModelIdentifier = EnchantedBookOverrides.BOOK_MODELS.get(enchantmentIdentifier);
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

    protected static ModelIdentifier toModelIdentifier(Identifier identifier)
    {
        return new ModelIdentifier(identifier, "inventory");
    }
}
