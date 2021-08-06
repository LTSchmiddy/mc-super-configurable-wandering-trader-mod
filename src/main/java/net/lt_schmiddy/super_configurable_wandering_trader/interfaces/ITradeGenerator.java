package net.lt_schmiddy.super_configurable_wandering_trader.interfaces;

import java.util.Random;

import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

public interface ITradeGenerator {

    public ITradeGenerator getParent();
    public void setParent(ITradeGenerator p_parent);

    public void validate();
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant, Random random);
    public float getWeight();
}
