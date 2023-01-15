package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.commands.EnchantmentColorCommand;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class ModCommands
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register(EnchantmentColorCommand::register);
    }
}
