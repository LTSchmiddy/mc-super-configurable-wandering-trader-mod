package net.lt_schmiddy.super_configurable_wandering_trader.factories;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

public class SellEnchantmentFactory implements TradeOffers.Factory { 

    public Enchantment enchantment;
    public int level;
    public int price;
    public int maxUses;
    public int experience;
    public float multiplier;

    public SellEnchantmentFactory(Enchantment p_enchantment, int p_level, int p_price, int p_maxUses, int p_experience,
            float p_multiplier) {
        enchantment = p_enchantment;
        level = p_level;
        price = p_price;
        maxUses = p_maxUses;
        experience = p_experience;
        multiplier = p_multiplier;
    }

    public SellEnchantmentFactory(String p_enchantment_name, int p_level, int p_price, int p_maxUses, int p_experience,
            float p_multiplier) {
        enchantment = Registry.ENCHANTMENT.get(new Identifier(p_enchantment_name));
        level = p_level;
        price = p_price;
        maxUses = p_maxUses;
        experience = p_experience;
        multiplier = p_multiplier;
    }

    public ItemStack tradeOfferStack() {
        ItemStack retVal = new ItemStack(Items.ENCHANTED_BOOK);
        retVal.addEnchantment(enchantment, level);

        return retVal;
    }

    public TradeOffer create(Entity entity, Random random) {
        return new TradeOffer(
            new ItemStack(Items.EMERALD, this.price),
            new ItemStack(Items.BOOK),
            tradeOfferStack(),
            this.maxUses, 
            this.experience, 
            this.multiplier
        );
    }
}
