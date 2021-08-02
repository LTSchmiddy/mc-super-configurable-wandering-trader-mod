package net.hyper_pigeon.better_wandering_trader.config;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

public class TradeListUtils {
    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public static transient Random random = new Random();

    public static void fillRandomTradesFromPool(TradeOfferList tradeOfferList, MerchantEntity merchant,
            IUserTradeGenerator[] trades, int count) {
        Set<Integer> set = Sets.newHashSet();

        if (trades.length == 0) {
            return;
        }

        if (count != 1) {
            if (trades.length > count) {
                while (set.size() < count) {
                    set.add(random.nextInt(trades.length));
                }
            } else {
                for (int i = 0; i < trades.length; ++i) {
                    set.add(i);
                }
            }
        } else {
            set.add(random.nextInt(trades.length));
        }
        Iterator<Integer> var9 = set.iterator();

        while (var9.hasNext()) {
            Integer integer = (Integer) var9.next();
            trades[integer].addTradeOffers(tradeOfferList, merchant);
        }
    }

    public static void fillWeightedRandomTradesFromPool(TradeOfferList tradeOfferList, MerchantEntity merchant,
            IUserTradeGenerator[] trades, int count) {
        // We weren't given the total weight, so we'll calculate it now:
        float total_weight = 0;
        for(IUserTradeGenerator i : trades) {
            if (i == null) {continue;}
            total_weight += i.getWeight();
        }
        fillWeightedRandomTradesFromPool(tradeOfferList, merchant, trades, count, total_weight);
    }

    public static void fillWeightedRandomTradesFromPool(TradeOfferList tradeOfferList, MerchantEntity merchant,
            IUserTradeGenerator[] trades, int count, float total_weight) {
        if (trades.length == 0) {
            return;
        }
        
        // Random selection, but factoring in weights:
        Set<Integer> set = Sets.newHashSet();
        while (set.size() < count) {
            // Pick a random value in the weight range:
            float selection = random.nextFloat() * total_weight;
            
            float current = 0;
            for(int i = 0; i < trades.length; i++) {
                if (trades[i] == null) {continue;}
                
                current += trades[i].getWeight();
                
                // The first trade where current >= selection is the trade corresponding to selection. Add it, and continue:
                if (current >= selection) {
                    set.add(i);
                    break;
                }
            }

        }

        Iterator<Integer> var9 = set.iterator();
        while (var9.hasNext()) {
            Integer integer = (Integer) var9.next();
            trades[integer].addTradeOffers(tradeOfferList, merchant);
        }
    }
}
