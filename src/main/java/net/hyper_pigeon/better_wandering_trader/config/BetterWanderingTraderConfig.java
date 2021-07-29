package net.hyper_pigeon.better_wandering_trader.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.hyper_pigeon.better_wandering_trader.SellItemFactory;

@Config(name = "better_wandering_trader")
public class BetterWanderingTraderConfig implements ConfigData {


    /*@Comment("Toggle A")
    boolean toggleA = true;

    @Comment("Toggle B")
    boolean toggleB = false;

    @ConfigEntry.Gui.CollapsibleObject
    InnerStuff stuff = new InnerStuff();

    @ConfigEntry.Gui.Excluded
    InnerStuff invisibleStuff = new InnerStuff();


    static class InnerStuff {
        int a = 0;
        int b = 1;
    }*/

    @ConfigEntry.Gui.CollapsibleObject
    public UserAddedTrades trades = new UserAddedTrades();

    public static class UserAddedTrades {
        public boolean enable_base_trades = true;
        public boolean enable_user_added_trades = true;
        public boolean rewrite_default_trades = true;

        public String user_trade_lists_folder = "wt_user_trades";
    }


    @ConfigEntry.Gui.Excluded
    public TradeArray commonTradeFactory = new TradeArray();;

    @ConfigEntry.Gui.Excluded
    public TradeArray rareTradeFactory = new TradeArray();;

    @ConfigEntry.Gui.Excluded
    TradeFormat example = new TradeFormat();

    public void WriteDefaultTrades() {
        commonTradeFactory = TradeArray.fromTradeFactory(SellItemFactory.DefaultCommonTrades);
        rareTradeFactory = TradeArray.fromTradeFactory(SellItemFactory.DefaultRareTrades);
    }
}
