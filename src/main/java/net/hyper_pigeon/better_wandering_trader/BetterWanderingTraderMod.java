package net.hyper_pigeon.better_wandering_trader;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.*;
import net.minecraft.server.MinecraftServer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class BetterWanderingTraderMod implements ModInitializer, ServerStarted, ServerStopping {

	//public static BetterWanderingTraderConfig config = AutoConfig.getConfigHolder(BetterWanderingTraderConfig.class).getConfig();
	
	public static ConfigHolder<BetterWanderingTraderConfig> CONFIG_HOLDER = AutoConfig.register(BetterWanderingTraderConfig.class, JanksonConfigSerializer::new);
	public static BetterWanderingTraderConfig CONFIG = CONFIG_HOLDER.getConfig();


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//AutoConfig.register(BetterWanderingTraderConfig.class, JanksonConfigSerializer::new);
		//config = AutoConfig.getConfigHolder(BetterWanderingTraderConfig.class).getConfig();
		//BetterWanderingTraderConfig config = AutoConfig.getConfigHolder(BetterWanderingTraderConfig.class).getConfig();
		//System.out.println("Hello Fabric world!");

		ServerLifecycleEvents.SERVER_STARTED.register(this);
		ServerLifecycleEvents.SERVER_STOPPING.register(this);

		if (CONFIG.trades.write_default_trades) {
			CONFIG.trades.write_default_trades = false;

			CONFIG.WriteDefaultTrades();
			CONFIG_HOLDER.save();
		}	
	}

	public void onQuit() {
		
	}

	@Override
	public void onServerStopping(MinecraftServer server) {
		// TODO Auto-generated method stub
		CONFIG_HOLDER.save();
	}

	@Override
	public void onServerStarted(MinecraftServer server) {
		// TODO Auto-generated method stub

	
	}




}
