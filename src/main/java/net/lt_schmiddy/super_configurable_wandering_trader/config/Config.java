package net.lt_schmiddy.super_configurable_wandering_trader.config;

public class Config{

    public static class GeneralSettings {
        public boolean load_configs_on_server_start = false;
    }
    public GeneralSettings general = new GeneralSettings();
    
    public static class UserAddedTrades {
        public boolean enable_base_trades = true;
        public boolean enable_user_added_trades = true;

        public String user_trade_lists_folder = "wt_user_trades";
    }
    public UserAddedTrades trades = new UserAddedTrades();
}
