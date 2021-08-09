package net.lt_schmiddy.super_configurable_wandering_trader.abstracts;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;
import net.lt_schmiddy.super_configurable_wandering_trader.config.ConfigHandler;
import net.lt_schmiddy.super_configurable_wandering_trader.generators.TradeGroupFile;
import net.lt_schmiddy.super_configurable_wandering_trader.interfaces.INamedTradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.interfaces.ITradeGenerator;

public abstract class ATradeGenerator implements ITradeGenerator {

    
    private transient ITradeGenerator parent = null;

    public ITradeGenerator getRoot() {
        ITradeGenerator current = this;

        while (current.getParent() != null) {
            current = current.getParent();
        }
        return current;
    }

    public INamedTradeGenerator getNamedRoot() {
        ITradeGenerator root = getRoot();
        if (!(root instanceof INamedTradeGenerator)) {
            System.out.println("ERROR: All root generators MUST be INamedTradeGenerator, not simply ITradeGenerator.");
            return null;
        }

        return (INamedTradeGenerator)root;
    }

    public String getRootName() {
        INamedTradeGenerator root = getNamedRoot();
        if (root == null) {
            return null;
        }

        return root.getName();
    }

    public Path getRelativeDir() {
        ITradeGenerator root = getRoot();
        if (!(root instanceof TradeGroupFile)) {
            return FabricLoader.getInstance().getConfigDir()
                    .resolve(ConfigHandler.config.trades.user_trade_lists_folder);
        }

        TradeGroupFile file = (TradeGroupFile) root;

        return file.path.getParent();
    }

    public ITradeGenerator getParent() {
        return parent;
    }

    public void setParent(ITradeGenerator p_parent) {
        parent = p_parent;
    }
}
