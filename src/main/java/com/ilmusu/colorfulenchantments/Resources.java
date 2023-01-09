package com.ilmusu.colorfulenchantments;

import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resources
{
    public static final String MOD_ID = "colorfulenchantments";
    public static final String MOD_NAME = "Colorful Enchantments Mod";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ModelIdentifier ENCHANTED_COLORED_BOOK_IDENTIFIER = new ModelIdentifier(Resources.identifier("enchanted_colored_book"), "inventory");

    public static Identifier identifier(String string)
    {
        return new Identifier(MOD_ID, string);
    }
}
