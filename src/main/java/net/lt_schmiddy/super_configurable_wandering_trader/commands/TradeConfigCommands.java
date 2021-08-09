package net.lt_schmiddy.super_configurable_wandering_trader.commands;

import java.io.File;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.arguments.*;

import net.lt_schmiddy.super_configurable_wandering_trader.BetterWanderingTraderMod;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.TradeConfigHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TradeConfigCommands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) { 
        dispatcher.register(CommandManager.literal("scwt")
            .requires(source -> source.hasPermissionLevel(4))
            .then(CommandManager.literal("configs")
                .then(
                    CommandManager.literal("reload")
                    .executes(context -> {
                        BetterWanderingTraderMod.loadUserTrades();
                        return 1;
                    })
                )
                .then(
                    CommandManager.literal("check")
                    .executes(context -> {
                        BetterWanderingTraderMod.checkUserTrades();
                        return 1;
                    })
                )
            )
            .then(CommandManager.literal("queue")
                .then(CommandManager.literal("add")
                    .then(
                        enqueueTrader(false, false)
                    )
                    .then(
                        CommandManager.literal("hidden")
                        .then(
                            enqueueTrader(true, false)
                        )
                    )
                )
                .then(CommandManager.literal("clear")
                    .executes(context -> {
                        return TradeConfigHandler.queue.clearQueue();
                    })
                )
                .then(CommandManager.literal("dump")
                    .then(CommandManager.argument("file_name", StringArgumentType.string())
                        .executes(context -> {
                            return TradeConfigHandler.saveTraderQueue(new File(context.getArgument("file_name", String.class) + ".json"));
                        })
                    )
                )
                .then(CommandManager.literal("load")
                    .then(CommandManager.argument("file_name", StringArgumentType.string())
                        .executes(context -> {
                            return TradeConfigHandler.loadTraderQueue(new File(context.getArgument("file_name", String.class) + ".json"));
                        })
                    )
                )
                .then(CommandManager.literal("lock")
                    .then(CommandManager.literal("set")
                        .then(
                            enqueueTrader(false, true)
                        )
                        .then(
                            CommandManager.literal("hidden")
                            .then(
                                enqueueTrader(true, true)
                            )
                        )
                    )
                    .then(CommandManager.literal("clear")
                        .executes(context -> {
                            return TradeConfigHandler.queue.clearLock();
                        })
                    )
                )
            )
        );

    }

    public static ArgumentBuilder<ServerCommandSource, ?> enqueueTrader(boolean hiddenConfigs, boolean use_lock) {
        return CommandManager.argument("config_path", TradeConfigArgumentType.configs(hiddenConfigs))
        .executes(context -> {
            if (use_lock) {
                return TradeConfigHandler.queue.setLock(context.getArgument("config_path", String.class));
            } else {
                return TradeConfigHandler.queue.enqueue(context.getArgument("config_path", String.class));
            }
        })
        .then(
            CommandManager.argument("seed", LongArgumentType.longArg())
            .executes(context -> {
                if (use_lock) {
                    return TradeConfigHandler.queue.setLock(context.getArgument("config_path", String.class), context.getArgument("seed", Long.class));
                } else {
                    return TradeConfigHandler.queue.enqueue(context.getArgument("config_path", String.class), context.getArgument("seed", Long.class));
                }
            })
        );
    }
}
