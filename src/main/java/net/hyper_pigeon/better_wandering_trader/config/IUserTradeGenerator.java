package net.hyper_pigeon.better_wandering_trader.config;

import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

public interface IUserTradeGenerator {
    
    public void validate();
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant);
    public float getWeight();
}
