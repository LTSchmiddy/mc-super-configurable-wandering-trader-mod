package net.hyper_pigeon.better_wandering_trader.listings;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.hyper_pigeon.better_wandering_trader.config.UserTradeArray;
import net.hyper_pigeon.better_wandering_trader.config.UserTradeListConfigFile;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;

public class TradeFileListing {
    public Path path;
    public UserTradeListConfigFile configData = null;
    public static List<TradeListing> tradeListings = new ArrayList<TradeListing>();

    // Initialization:
    public TradeFileListing(){}
    
    public TradeFileListing(UserTradeListConfigFile p_configData){
        loadConfig(p_configData);
    }

    public TradeFileListing(Path p_path, UserTradeListConfigFile p_configData){
        path = p_path;
        loadConfig(p_configData);
    }

    public void loadConfig(UserTradeListConfigFile p_configData){
        configData = p_configData;
        tradeListings.clear();

        for(UserTradeArray i : configData.trade_lists){
            tradeListings.add(new TradeListing(path, i));
        }
    }

    // Trade Offer Processing:
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant){
        for (TradeListing i : tradeListings) {
            i.addTradeOffers(tradeOfferList, merchant);
        }
    }
}
