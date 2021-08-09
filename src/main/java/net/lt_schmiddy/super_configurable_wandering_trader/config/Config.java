package net.lt_schmiddy.super_configurable_wandering_trader.config;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;

public class Config {

    public static class GeneralSettings {
        public boolean load_configs_on_server_start = true;
    }
    public GeneralSettings general = new GeneralSettings();
    
    public static class UserAddedTrades {
        public boolean enable_base_trades = true;
        public boolean enable_user_added_trades = true;

        public String user_trade_lists_folder = "wt_user_trades";

        public Path getTradesPath() {
            return FabricLoader.getInstance().getConfigDir().resolve(user_trade_lists_folder);
        }
    }
    public UserAddedTrades trades = new UserAddedTrades();
}
