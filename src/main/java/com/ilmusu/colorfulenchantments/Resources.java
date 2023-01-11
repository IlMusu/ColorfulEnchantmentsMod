package com.ilmusu.colorfulenchantments;

import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.data.client.Model;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resources
{
    public static final String MOD_ID = "colorfulenchantments";
    public static final String MOD_NAME = "Colorful Enchantments Mod";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Identifier ENCHANTED_COLORED_BOOK_IDENTIFIER = Resources.identifier("enchanted_colored_book");
    public static final Identifier CURSED_COLORED_BOOK_IDENTIFIER = Resources.identifier("cursed_colored_book");
    public static final Identifier COLORED_GLINT_IDENTIFIER = new Identifier(Resources.MOD_ID, "textures/misc/colored_glint.png");


    public static Identifier identifier(String string)
    {
        return new Identifier(MOD_ID, string);
    }
}
