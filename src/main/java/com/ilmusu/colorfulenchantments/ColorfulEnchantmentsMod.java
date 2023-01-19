package com.ilmusu.colorfulenchantments;

import com.ilmusu.colorfulenchantments.registries.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class ColorfulEnchantmentsMod implements ModInitializer, ClientModInitializer
{
	@Override
	public void onInitialize()
	{
		ModItems.register();
		ModCommands.register();
		ModMessages.ServerHandlers.register();
	}

	@Override
	public void onInitializeClient()
	{
		ModConfigurations.register();
		ModItems.registerModelOverrides();
		ModItems.registerColors();
		ModEnchantments.registerColors();
		ModRenderLayers.register();
		ModMessages.ClientHandlers.register();
	}
}
