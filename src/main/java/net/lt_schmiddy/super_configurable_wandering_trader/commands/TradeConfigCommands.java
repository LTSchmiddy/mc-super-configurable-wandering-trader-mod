package net.lt_schmiddy.super_configurable_wandering_trader.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.lt_schmiddy.super_configurable_wandering_trader.BetterWanderingTraderMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TradeConfigCommands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        
        dispatcher.register(CommandManager.literal("reload_user_trades")
        .requires(source -> source.hasPermissionLevel(4))
        .executes(context -> {
            BetterWanderingTraderMod.loadUserTrades();
            return 1;
        }));

        dispatcher.register(CommandManager.literal("check_user_trades")
        .requires(source -> source.hasPermissionLevel(4))
        .executes(context -> {
            BetterWanderingTraderMod.checkUserTrades();
            return 1;
        }));

    }
}
