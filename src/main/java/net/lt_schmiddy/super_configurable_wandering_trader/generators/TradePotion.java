package net.lt_schmiddy.super_configurable_wandering_trader.generators;

import java.util.Random;

import net.lt_schmiddy.super_configurable_wandering_trader.abstracts.AWeightedAndSortedTradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.factories.SellItemFactory;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

public class TradePotion extends AWeightedAndSortedTradeGenerator {
    public enum PotionType {
        potion,
        splash_potion,
        lingering_potion,
        tipped_arrow,
    }
    
    public PotionType type = PotionType.potion;
    public String effect = "minecraft:mundane";
    public int price = 5;
    public int maxUses = 3;
    public int experience = 1;

    // private transient final Item item = Items.POTION;
    private transient SellItemFactory factory = null;

    public SellItemFactory getFactory() {
        if (factory == null) {
            validate();
        }
        return factory;
    }

    public Item getPotionItem() {
        switch (type) {
            case tipped_arrow:
                return Items.TIPPED_ARROW;
            case splash_potion:
                return Items.SPLASH_POTION;
            case lingering_potion:
                return Items.LINGERING_POTION;
            case potion:
            default:
                return Items.POTION;
        }
    }

    @Override
    public void validate() {
        ItemStack potion = new ItemStack(getPotionItem());

        NbtCompound tag = new NbtCompound();
        tag.putString("Potion", effect);
        potion.setTag(tag);

        factory = new SellItemFactory(potion, price, maxUses, experience);
        
    }

    @Override
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant, Random random) {
        TradeOffer tradeOffer = getFactory().create(merchant, random);
        if (tradeOffer != null) {
            tradeOfferList.add(tradeOffer);
        }
        
    }    
}
