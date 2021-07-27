package net.hyper_pigeon.better_wandering_trader;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.item.Item;
import net.minecraft.village.TradeOffers;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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
        public boolean enable_user_added_traded = false;
        public boolean write_default_trades = true;
    }

    @ConfigEntry.Gui.Excluded
    public TradeArray commonTradeFactory = new TradeArray();;
    // public TradeArray commonTradeFactory = TradeArray.fromTradeFactory(TraderOffersMixin.CommonTrades);

    @ConfigEntry.Gui.Excluded
    public TradeArray rareTradeFactory = new TradeArray();;
    // public TradeArray rareTradeFactory = TradeArray.fromTradeFactory(TraderOffersMixin.RareTrades);
    
    @ConfigEntry.Gui.Excluded
    public TradeArray invisibleTradeFactory = new TradeArray();

    @ConfigEntry.Gui.Excluded
    TradeFormat example = new TradeFormat();

    public static class TradeArray {
        
        //TradeOffers.Factory[] new_trades = new TradeOffers.Factory[]{};
        //TradeOffers.Factory example = new SellItemFactory(Items.TOTEM_OF_UNDYING,  56, 1, 1,1);
        public int number_of_trades = 1;
        public int trades_to_choose = 1;
        public TradeFormat array[] = new TradeFormat[number_of_trades];

        public static TradeArray fromTradeFactory(TradeOffers.Factory[] entries) {
            TradeArray retVal = new TradeArray();
            retVal.number_of_trades = entries.length;
            retVal.array = new TradeFormat[entries.length];

            for (int i = 0; i < entries.length; i++){
                retVal.array[i] = ((SellItemFactory)entries[i]).asTradeFormat();
            }

            return retVal;
        }

        public TradeOffers.Factory[] toTradeFactory() {
            TradeOffers.Factory[] retVal = new TradeOffers.Factory[array.length];
            for (int i = 0; i < array.length; i++) {
                Item item = Registry.ITEM.get(
                        Identifier.tryParse(array[i].identifier));
                int price = array[i].price;
                int count = array[i].count;
                int max_uses = array[i].maxUses;
                int experience = array[i].experience;
                retVal[i] = new SellItemFactory(item, price, count, max_uses, experience);
            }

            return retVal;
        }
    }

    public static class TradeFormat {
        public String identifier = "minecraft:end_crystal";
        public int price = 0;
        public int count = 0;
        public int maxUses = 0;
        public int experience = 0;
    }

    public void WriteDefaultTrades() {
        commonTradeFactory = TradeArray.fromTradeFactory(SellItemFactory.DefaultCommonTrades);
        rareTradeFactory = TradeArray.fromTradeFactory(SellItemFactory.DefaultRareTrades);
    }

}
