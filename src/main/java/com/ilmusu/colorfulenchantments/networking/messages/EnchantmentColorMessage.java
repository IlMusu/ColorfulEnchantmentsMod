package com.ilmusu.colorfulenchantments.networking.messages;

import com.ilmusu.colorfulenchantments.items.EnchantedColoredBookHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.awt.*;

public class EnchantmentColorMessage extends _Message
{
    private Identifier enchantment;
    private Color color;

    public EnchantmentColorMessage()
    {
        super("enchantment_color");
    }

    public EnchantmentColorMessage(Identifier enchantment, Color color)
    {
        this();
        this.enchantment = enchantment;
        this.color = color;
    }

    @Override
    public PacketByteBuf encode(PacketByteBuf buf)
    {
        buf.writeString(this.enchantment.toString());
        buf.writeInt(color.getRGB());
        return buf;
    }

    @Override
    public void decode(PacketByteBuf buf)
    {
        this.enchantment = new Identifier(buf.readString());
        this.color = new Color(buf.readInt());
    }

    @Override
    public void handle(PlayerEntity player)
    {
        Enchantment enchantment = Registries.ENCHANTMENT.get(this.enchantment);
        EnchantedColoredBookHelper.overrideBookColor(enchantment, this.color);
    }
}
