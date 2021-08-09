package net.lt_schmiddy.super_configurable_wandering_trader.generators;

import java.util.Random;

import net.lt_schmiddy.super_configurable_wandering_trader.abstracts.AWeightedAndSortedTradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.factories.SellEnchantmentFactory;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

public class TradeEnchantment extends AWeightedAndSortedTradeGenerator {
    public String enchantment = "minecraft:unbreaking";
    public int level = 1;
    public int price = 5;
    public int maxUses = 4;
    public int experience = 1;

    public transient SellEnchantmentFactory factory;

    @Override
    public void validate() {
        factory = new SellEnchantmentFactory(enchantment, level, price, maxUses, experience, 0.5f);
        
    }

    @Override
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant, Random random) {
        if (factory == null) { return; }

        tradeOfferList.add(factory.create(merchant, random));
    }
    
}
