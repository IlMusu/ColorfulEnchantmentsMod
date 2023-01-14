package com.ilmusu.colorfulenchantments.utils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ModUtils
{
    public static String colorToString(Color color)
    {
        return "["+color.getRed()+","+color.getGreen()+","+color.getBlue()+"]";
    }

    public static Color colorFromString(String string)
    {
        string = string.trim().substring(1, string.length()-1);
        List<Integer> colors = Arrays.stream(string.split(",")).map((component) ->
                Integer.parseInt(component.trim())
        ).toList();
        return new Color(colors.get(0),colors.get(1),colors.get(2));
    }
}
