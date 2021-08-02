package net.hyper_pigeon.better_wandering_trader;

import com.mojang.brigadier.CommandDispatcher;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.hyper_pigeon.better_wandering_trader.commands.TradeConfigCommands;
import net.hyper_pigeon.better_wandering_trader.config.BetterWanderingTraderConfig;
import net.hyper_pigeon.better_wandering_trader.config.UserTradeListConfigHandler;

public class BetterWanderingTraderMod implements ModInitializer, ServerStarted, ServerStopping {

	//public static BetterWanderingTraderConfig config = AutoConfig.getConfigHolder(BetterWanderingTraderConfig.class).getConfig();
	public static ConfigHolder<BetterWanderingTraderConfig> CONFIG_HOLDER = AutoConfig.register(BetterWanderingTraderConfig.class, JanksonConfigSerializer::new);
	public static BetterWanderingTraderConfig CONFIG = CONFIG_HOLDER.getConfig();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        CommandRegistrationCallback.EVENT.register(this::registerCommands);

		ServerLifecycleEvents.SERVER_STARTED.register(this);
		ServerLifecycleEvents.SERVER_STOPPING.register(this);

		loadTrades();
	}

	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated){
		TradeConfigCommands.registerCommands(dispatcher, dedicated);
	}

	@Override
	public void onServerStopping(MinecraftServer server) {
		CONFIG_HOLDER.save();
	}

	@Override
	public void onServerStarted(MinecraftServer server) {

	
	}

	public static void loadTrades() {
		if (CONFIG.trades.rewrite_default_trades) {
			CONFIG.trades.rewrite_default_trades = false;
			CONFIG.WriteDefaultTrades();
			CONFIG_HOLDER.save();
		}
		if (CONFIG.trades.enable_user_added_trades) {
			loadUserTrades();
		}
	}

	public static void loadUserTrades() {
		UserTradeListConfigHandler.loadAll(CONFIG.trades.user_trade_lists_folder);
	}

	public static void checkUserTrades() {
		UserTradeListConfigHandler.checkAll(CONFIG.trades.user_trade_lists_folder);
	}



}
