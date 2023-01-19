package com.ilmusu.colorfulenchantments.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public static WritableRaster getPixels(Identifier shape)
    {
        BufferedImage image = null;
        try
        {
            Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(shape);
            image = ImageIO.read(resource.orElseThrow().getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        assert image != null;
        return image.getRaster();
    }
}
