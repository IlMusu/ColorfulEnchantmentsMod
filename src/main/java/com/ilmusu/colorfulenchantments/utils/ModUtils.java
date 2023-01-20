package com.ilmusu.colorfulenchantments.utils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ModUtils
{
    public static boolean isInRGBArrayFormat(String value)
    {
        value = value.trim();
        if(value.charAt(0) != '[' || value.charAt(value.length()-1) != ']')
            return false;
        return value.substring(1, value.length()-1).split(",").length == 3;
    }

    public static String toRGBArrayString(Color color)
    {
        return "["+color.getRed()+","+color.getGreen()+","+color.getBlue()+"]";
    }

    public static Color fromRGBArrayString(String string)
    {
        string = string.trim().substring(1, string.length()-1);
        List<Integer> colors = Arrays.stream(string.split(",")).map((component) ->
                safeParseInt(component.trim())
        ).toList();
        return new Color(colors.get(0),colors.get(1),colors.get(2));
    }

    public static int safeParseInt(String value)
    {
        try {
            return Integer.parseInt(value);
        }catch (NumberFormatException exception){
            return 0;
        }
    }
}
