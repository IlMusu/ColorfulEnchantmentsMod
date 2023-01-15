package com.ilmusu.colorfulenchantments.commands;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.networking.messages.EnchantmentColorMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EnchantmentArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.awt.*;

public class EnchantmentColorCommand
{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean access)
    {
        dispatcher.register(CommandManager.literal(Resources.MOD_ID)
            .then(CommandManager.literal("enchantment")
                .then(CommandManager.argument("enchantment", EnchantmentArgumentType.enchantment())
                    .then(CommandManager.argument("red", IntegerArgumentType.integer(0, 255))
                        .then(CommandManager.argument("green", IntegerArgumentType.integer(0, 255))
                            .then(CommandManager.argument("blue", IntegerArgumentType.integer(0, 255))
                                .executes(context -> changeEnchantmentColor(
                                    context.getSource(),
                                    EnchantmentArgumentType.getEnchantment(context, "enchantment"),
                                    IntegerArgumentType.getInteger(context, "red"),
                                    IntegerArgumentType.getInteger(context, "green"),
                                    IntegerArgumentType.getInteger(context, "blue")
                                ))
                            )
                        )
                    )
                )
            )
        );
    }

    private static int changeEnchantmentColor(ServerCommandSource source, Enchantment enchantmentEntry, int red, int green, int blue)
    {
        if(!(source.getEntity() instanceof ServerPlayerEntity player))
            return 0;

        Identifier enchantment = Registry.ENCHANTMENT.getId(enchantmentEntry);
        new EnchantmentColorMessage(enchantment, new Color(red, green, blue)).sendToClient(player);
        source.sendFeedback(Text.of("commands.enchantment.color.success"), false);
        return 1;
    }
}