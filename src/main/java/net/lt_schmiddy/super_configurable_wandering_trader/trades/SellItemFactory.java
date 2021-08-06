package net.lt_schmiddy.super_configurable_wandering_trader.trades;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.lt_schmiddy.super_configurable_wandering_trader.generators.TradeItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

public class SellItemFactory implements TradeOffers.Factory {
    public ItemStack sell;
    public int price;
    public int count;
    public int maxUses;
    public int experience;
    public float multiplier;

    public HashMap<String, Integer> enchantments = null; //new HashMap<String, Integer>();

    public TradeItem asTradeFormat() {
        TradeItem retVal = new TradeItem();
        retVal.identifier = Registry.ITEM.getId(sell.getItem()).toString();
        retVal.price = price;
        retVal.count = count;
        retVal.maxUses = maxUses;
        retVal.experience = experience;
        retVal.enchantments = enchantments;

        return retVal;
    }

    public SellItemFactory(Block block, int p_price, int p_count, int p_maxUses, int p_experience) {
        this(new ItemStack(block), p_price, p_count, p_maxUses, p_experience);
    }

    public SellItemFactory(Item item, int i, int j, int k) {
        this((ItemStack) (new ItemStack(item)), i, j, 12, k);
    }

    public SellItemFactory(Item item, int p_price, int p_count, int p_maxUses, int p_experience) {
        this(new ItemStack(item),  p_price, p_count, p_maxUses, p_experience);
    }

    public SellItemFactory(Item item, int p_price, int p_count, int p_maxUses, int p_experience, HashMap<String, Integer> p_enchantments) {
        this(new ItemStack(item),  p_price, p_count, p_maxUses, p_experience);
        enchantments = p_enchantments;
    }

    public SellItemFactory(ItemStack itemStack, int p_price, int p_count, int p_maxUses, int p_experience) {
        this(itemStack, p_price, p_count, p_maxUses, p_experience, 0.05F);
    }

    public SellItemFactory(ItemStack itemStack, int price, int count, int maxUses, int experience,
            float multiplier) {
        this.sell = itemStack;
        this.price = price;
        this.count = count;
        this.maxUses = maxUses;
        this.experience = experience;
        this.multiplier = multiplier;
    }

    public ItemStack tradeOfferStack() {
        ItemStack retVal = new ItemStack(this.sell.getItem(), this.count);
        if (enchantments != null) {
            for (Map.Entry<String, Integer> i: enchantments.entrySet()){
                retVal.addEnchantment(Registry.ENCHANTMENT.get(new Identifier(i.getKey())), i.getValue());
            }
        }
        return retVal;
    }

    public TradeOffer create(Entity entity, Random random) {
        return new TradeOffer(new ItemStack(Items.EMERALD, this.price),
            tradeOfferStack(), this.maxUses, this.experience, this.multiplier);
    }

    
    public static TradeItem[] toTradeFormat(TradeOffers.Factory[] entries) {
        TradeItem[] retVal = new TradeItem[entries.length];


        for (int i = 0; i < entries.length; i++){
            retVal[i] = ((SellItemFactory)entries[i]).asTradeFormat();
        }

        return retVal;
    }

    
}
