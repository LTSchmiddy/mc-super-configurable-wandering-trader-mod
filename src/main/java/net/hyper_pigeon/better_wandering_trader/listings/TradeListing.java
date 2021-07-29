package net.hyper_pigeon.better_wandering_trader.listings;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.hyper_pigeon.better_wandering_trader.config.UserTradeArray;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;

public class TradeListing {
    public Path path;
    public UserTradeArray trades;
    public TradeOffers.Factory[] factories;

    public Random random = new Random();

    public TradeListing() {
    }

    public TradeListing(UserTradeArray p_trades) {
        trades = p_trades;
        factories = trades.toTradeFactory();
    }

    public TradeListing(Path p_path, UserTradeArray p_trades) {
        path = p_path;
        trades = p_trades;
        factories = trades.toTradeFactory();
    }

    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant) {
        this.fillRandomTradesFromPool(merchant, tradeOfferList);

    } 

    public void fillRandomTradesFromPool(MerchantEntity merchant, TradeOfferList recipeList) {
        Set<Integer> set = Sets.newHashSet();
        
        if (this.trades.random_trades_to_choose != 1) {
            if (factories.length > this.trades.random_trades_to_choose) {
                while (set.size() < this.trades.random_trades_to_choose) {
                    set.add(this.random.nextInt(factories.length));
                }
            } else {
                for (int i = 0; i < factories.length; ++i) {
                    set.add(i);
                }
            }
        } else {
            set.add(this.random.nextInt(factories.length));
        }


        Iterator<Integer> var9 = set.iterator();

        while (var9.hasNext()) {
            Integer integer = (Integer) var9.next();
            TradeOffers.Factory factory = factories[integer];
            TradeOffer tradeOffer = factory.create(merchant, this.random);
            if (tradeOffer != null) {
                recipeList.add(tradeOffer);
            }
        }

    }
}