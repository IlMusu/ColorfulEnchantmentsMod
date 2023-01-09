package com.ilmusu.colorfulenchantments;

import com.ilmusu.colorfulenchantments.registries.ModEnchantments;
import com.ilmusu.colorfulenchantments.registries.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class ColorfulEnchantmentsMod implements ModInitializer, ClientModInitializer
{
	@Override
	public void onInitialize()
	{
		ModItems.register();
	}

	@Override
	public void onInitializeClient()
	{
		ModItems.registerModels();
		ModItems.registerColors();
		ModEnchantments.registerColors();
	}
}
