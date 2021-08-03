package net.lt_schmiddy.super_configurable_wandering_trader.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "better_wandering_trader")
public class BetterWanderingTraderConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public UserAddedTrades trades = new UserAddedTrades();

    public static class UserAddedTrades {
        public boolean enable_base_trades = true;
        public boolean enable_user_added_trades = true;

        public String user_trade_lists_folder = "wt_user_trades";
    }
}
