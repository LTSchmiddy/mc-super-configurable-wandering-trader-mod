package net.hyper_pigeon.better_wandering_trader.config;

import java.util.HashMap;

import net.hyper_pigeon.better_wandering_trader.SellItemFactory;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

public class TradeFormat implements IUserTradeGenerator {
    public float selction_weight = 1;
    public String identifier = "minecraft:end_crystal";
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
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant) {
        TradeOffer tradeOffer = getFactory().create(merchant, TradeListUtils.random);
        if (tradeOffer != null) {
            tradeOfferList.add(tradeOffer);
        }
        
    }

    @Override
    public float getWeight() {
        return selction_weight;
    }
}
