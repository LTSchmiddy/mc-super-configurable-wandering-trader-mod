package net.lt_schmiddy.super_configurable_wandering_trader;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.*;
import net.lt_schmiddy.super_configurable_wandering_trader.commands.TradeConfigCommands;
import net.lt_schmiddy.super_configurable_wandering_trader.config.ConfigHandler;
import net.lt_schmiddy.super_configurable_wandering_trader.generators.*;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.TradeConfigHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class BetterWanderingTraderMod implements ModInitializer, ServerStarted, ServerStopping {

	@Override
	public void onInitialize() {
		ConfigHandler.load();

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        CommandRegistrationCallback.EVENT.register(this::registerCommands);

		ServerLifecycleEvents.SERVER_STARTED.register(this);
		ServerLifecycleEvents.SERVER_STOPPING.register(this);

		TradeConfigHandler.registerGeneratorType("items", TradeItem.class);
		TradeConfigHandler.registerGeneratorType("potions", TradePotion.class);
		TradeConfigHandler.registerGeneratorType("enchantments", TradeEnchantment.class);
		TradeConfigHandler.registerGeneratorType("groups", TradeGroup.class);
		TradeConfigHandler.registerGeneratorType("files", TradeGroupFileReference.class);

		if (!ConfigHandler.config.general.load_configs_on_server_start) {
			System.out.println("Differing user trade loading until server loads.");
			loadUserTrades();
		}
		ConfigHandler.save();
	}

	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated){
		TradeConfigCommands.registerCommands(dispatcher, dedicated);
	}

	@Override
	public void onServerStopping(MinecraftServer server) {
		server.getFile(".");

		ConfigHandler.save();
		TradeConfigHandler.saveTraderQueue(server.getFile("trader_queue.json"));
	}

	@Override
	public void onServerStarted(MinecraftServer server) {
		if (ConfigHandler.config.general.load_configs_on_server_start) {
			loadUserTrades();
		}
		TradeConfigHandler.loadTraderQueue(server.getFile("trader_queue.json"));
	}

	public static void loadUserTrades() {
		TradeConfigHandler.loadAll(ConfigHandler.config.trades.user_trade_lists_folder);
	}

	public static void checkUserTrades() {
		TradeConfigHandler.checkAll(ConfigHandler.config.trades.user_trade_lists_folder);
	}
}
