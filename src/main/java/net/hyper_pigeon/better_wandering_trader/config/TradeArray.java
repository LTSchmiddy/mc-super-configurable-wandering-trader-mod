package net.hyper_pigeon.better_wandering_trader.config;

import net.hyper_pigeon.better_wandering_trader.SellItemFactory;
import net.minecraft.item.Item;
import net.minecraft.village.TradeOffers;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TradeArray {
    public TradeFormat array[] = new TradeFormat[0];

    public static TradeArray fromTradeFactory(TradeOffers.Factory[] entries) {
        TradeArray retVal = new TradeArray();
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
