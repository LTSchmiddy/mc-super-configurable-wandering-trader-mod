package net.lt_schmiddy.super_configurable_wandering_trader.generators;

import java.util.HashMap;
import java.util.Random;

import net.lt_schmiddy.super_configurable_wandering_trader.abstracts.AWeightedAndSortedTradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.SellItemFactory;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

public class TradeItem extends AWeightedAndSortedTradeGenerator {

    public String identifier = "minecraft:stone";
    public int price = 0;
    public int count = 0;
    public int maxUses = 0;
    public int experience = 0;

    // public HashMap<String, Integer> enchantments = new HashMap<String, Integer>();
    public HashMap<String, Integer> enchantments = null;
    private transient Item item = null;
    private transient SellItemFactory factory = null;
    
    public SellItemFactory getFactory() {
        if (item == null || factory == null) {
            validate();
        }
        return factory;
    }

    @Override
    public void validate() {
        item = Registry.ITEM.get(Identifier.tryParse(identifier));
        if (item != null && item.isEnchantable(new ItemStack(item, 1))) {
            enchantments = new HashMap<String, Integer>();
        }

        factory = new SellItemFactory(item, price, count, maxUses, experience, enchantments);
        
    }

    @Override
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant, Random random) {
        TradeOffer tradeOffer = getFactory().create(merchant, random);
        if (tradeOffer != null) {
            tradeOfferList.add(tradeOffer);
        }
        
    }
}
