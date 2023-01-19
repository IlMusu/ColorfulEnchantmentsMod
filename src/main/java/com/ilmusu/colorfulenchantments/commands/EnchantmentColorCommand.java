package com.ilmusu.colorfulenchantments.commands;

import com.ilmusu.colorfulenchantments.Resources;
import com.ilmusu.colorfulenchantments.networking.messages.EnchantmentColorMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

public class EnchantmentColorCommand
{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access)
    {
        dispatcher.register(CommandManager.literal(Resources.MOD_ID)
            .then(CommandManager.literal("enchantment")
                .then(CommandManager.argument("enchantment", RegistryEntryArgumentType.registryEntry(access, RegistryKeys.ENCHANTMENT))
                    .then(CommandManager.argument("red", IntegerArgumentType.integer(0, 255))
                        .then(CommandManager.argument("green", IntegerArgumentType.integer(0, 255))
                            .then(CommandManager.argument("blue", IntegerArgumentType.integer(0, 255))
                                .executes(context -> changeEnchantmentColor(
                                    context.getSource(),
                                    RegistryEntryArgumentType.getEnchantment(context, "enchantment"),
                                    IntegerArgumentType.getInteger(context, "red"),
                                    IntegerArgumentType.getInteger(context, "green"),
                                    IntegerArgumentType.getInteger(context, "blue")
                                ))
                            )
                        )
                    )
                    .then(CommandManager.literal("reset")
                            .executes(context -> resetEnchantmentColor(
                                    context.getSource(),
                                    RegistryEntryArgumentType.getEnchantment(context, "enchantment")
                            ))
                    )
                )
            )
        );
    }

    private static int resetEnchantmentColor(ServerCommandSource source, RegistryEntry<Enchantment> enchantmentEntry)
    {
        if(!(source.getEntity() instanceof ServerPlayerEntity player))
            return 0;

        Identifier enchantment = Registries.ENCHANTMENT.getId(enchantmentEntry.value());
        new EnchantmentColorMessage(enchantment, true).sendToClient(player);
        source.sendFeedback(Text.translatable("commands.enchantment.reset.success"), false);
        return 1;
    }

    private static int changeEnchantmentColor(ServerCommandSource source, RegistryEntry<Enchantment> enchantmentEntry, int red, int green, int blue)
    {
        if(!(source.getEntity() instanceof ServerPlayerEntity player))
            return 0;

        Identifier enchantment = Registries.ENCHANTMENT.getId(enchantmentEntry.value());
        new EnchantmentColorMessage(enchantment, new Color(red, green, blue)).sendToClient(player);
        source.sendFeedback(Text.translatable("commands.enchantment.color.success"), false);
        return 1;
    }
}