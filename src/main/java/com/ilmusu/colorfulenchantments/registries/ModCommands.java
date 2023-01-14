package com.ilmusu.colorfulenchantments.registries;

import com.ilmusu.colorfulenchantments.commands.EnchantmentColorCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, dedicated) ->
            EnchantmentColorCommand.register(dispatcher, registry)
        );
    }
}
