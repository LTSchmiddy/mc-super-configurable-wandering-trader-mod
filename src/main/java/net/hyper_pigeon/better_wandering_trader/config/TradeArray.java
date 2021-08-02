package net.hyper_pigeon.better_wandering_trader.config;

import net.hyper_pigeon.better_wandering_trader.SellItemFactory;
import net.minecraft.village.TradeOffers;

public class TradeArray {
    public TradeFormat trades[] = new TradeFormat[0];

    public static TradeArray fromTradeFactory(TradeOffers.Factory[] entries) {
        TradeArray retVal = new TradeArray();
        retVal.trades = new TradeFormat[entries.length];

        for (int i = 0; i < entries.length; i++){
            retVal.trades[i] = ((SellItemFactory)entries[i]).asTradeFormat();
        }

        return retVal;
    }

    public TradeOffers.Factory[] toTradeFactory() {
        TradeOffers.Factory[] retVal = new TradeOffers.Factory[trades.length];
        for (int i = 0; i < trades.length; i++) {
            // Item item = Registry.ITEM.get(
            //         Identifier.tryParse(array[i].identifier));
            retVal[i] = trades[i].getFactory(); // SellItemFactory(item, array[i].price, array[i].count, array[i].maxUses, array[i].experience, array[i].enchantments);
        }

        return retVal;
    }
}
