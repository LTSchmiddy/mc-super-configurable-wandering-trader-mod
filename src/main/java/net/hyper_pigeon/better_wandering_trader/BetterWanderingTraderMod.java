package net.hyper_pigeon.better_wandering_trader;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class BetterWanderingTraderMod implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		AutoConfig.register(BetterWanderingTraderConfig.class, JanksonConfigSerializer::new);
		//BetterWanderingTraderConfig config = AutoConfig.getConfigHolder(BetterWanderingTraderConfig.class).getConfig();
		//System.out.println("Hello Fabric world!");
	}
}
