package net.lt_schmiddy.super_configurable_wandering_trader.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.lt_schmiddy.super_configurable_wandering_trader.config.ConfigHandler;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.TradeConfigHandler;

public class TradeConfigArgumentType implements ArgumentType<String> {

    public boolean showHiddenConfigs = false;

    public TradeConfigArgumentType() {}

    public TradeConfigArgumentType(boolean p_showHiddenConfigs) {
        showHiddenConfigs = p_showHiddenConfigs;
    }

    public static TradeConfigArgumentType configs() {
        return new TradeConfigArgumentType(false);
    }

    public static TradeConfigArgumentType configs(boolean hidden) {
        return new TradeConfigArgumentType(hidden);
    }

    public static TradeConfigArgumentType hiddenConfigs() {
        return new TradeConfigArgumentType(true);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
        // return ConfigHandler.config.trades.getTradesPath().resolve(reader.readString());
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {

        try {
            Files.walk(ConfigHandler.config.trades.getTradesPath())
            .filter(Files::isRegularFile)
            .filter((Path fp) -> {return showHiddenConfigs ? TradeConfigHandler.isIgnoredFile(fp) : !TradeConfigHandler.isIgnoredFile(fp);})
            .forEach((Path fp) -> {
                builder.suggest("\"" + ConfigHandler.config.trades.getTradesPath().relativize(fp).toString().replace("\\", "/") + "\"");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return builder.buildFuture();
    }
}
