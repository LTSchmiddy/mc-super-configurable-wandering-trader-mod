package net.hyper_pigeon.better_wandering_trader.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.hyper_pigeon.better_wandering_trader.BetterWanderingTraderMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TradeConfigCommands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        
        dispatcher.register(CommandManager.literal("reload_user_trades").executes(context -> {
            BetterWanderingTraderMod.loadUserTrades();
            return 1;
        }));

    }
}
