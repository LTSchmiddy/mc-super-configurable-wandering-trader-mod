package net.hyper_pigeon.better_wandering_trader;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.hyper_pigeon.better_wandering_trader.config.TradeFormat;
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

    public HashMap<String, Integer> enchantments = new HashMap<String, Integer>();

    public TradeFormat asTradeFormat() {
        TradeFormat retVal = new TradeFormat();
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


    // Used to create the default config file:
    public static TradeOffers.Factory[] DefaultCommonTrades = new TradeOffers.Factory[] { 
        new SellItemFactory(Items.GOLDEN_APPLE, 6, 1, 12, 1),
        new SellItemFactory(Items.FIREWORK_ROCKET, 2, 1, 64, 1),
        new SellItemFactory(Items.ACACIA_SAPLING, 1, 1, 16, 1),
        new SellItemFactory(Items.DARK_OAK_SAPLING, 6, 1, 16, 1),
        new SellItemFactory(Items.NAUTILUS_SHELL, 5, 1, 10, 1),
        new SellItemFactory(Items.PUMPKIN_SEEDS, 1, 1, 16, 1),
        new SellItemFactory(Items.MELON_SEEDS, 1, 1, 16, 1),
        new SellItemFactory(Items.SLIME_BALL, 2, 1, 25, 1),
        new SellItemFactory(Items.HONEY_BOTTLE, 3, 1, 25, 1),
        new SellItemFactory(Items.HAY_BLOCK, 1, 1, 64, 1),
        new SellItemFactory(Items.ZOMBIE_HEAD, 8, 1, 1, 1),
        new SellItemFactory(Items.CREEPER_HEAD, 8, 1, 1, 1),
        new SellItemFactory(Items.SKELETON_SKULL, 8, 1, 1, 1),
        new SellItemFactory(Items.BONE_BLOCK, 2, 1, 20, 1),
        new SellItemFactory(Items.ENDER_PEARL, 6, 1, 16, 1),
        new SellItemFactory(Items.GUNPOWDER, 1, 1, 32, 1),
        new SellItemFactory(Items.SEA_PICKLE, 2, 1, 12, 1),
        new SellItemFactory(Items.PODZOL, 3, 1, 32, 1),
        new SellItemFactory(Items.MYCELIUM, 3, 1, 32, 1),
        new SellItemFactory(Items.BLAZE_ROD, 5, 1, 12, 1),
        new SellItemFactory(Items.CACTUS, 1, 1, 32, 1),
        new SellItemFactory(Items.BRAIN_CORAL_BLOCK, 3, 1, 32, 1),
        new SellItemFactory(Items.BUBBLE_CORAL_BLOCK, 3, 1, 32, 1),
        new SellItemFactory(Items.FIRE_CORAL_BLOCK, 3, 1, 32, 1),
        new SellItemFactory(Items.HORN_CORAL_BLOCK, 3, 1, 32, 1),
        new SellItemFactory(Items.TUBE_CORAL_BLOCK, 3, 1, 32, 1),
        new SellItemFactory(Items.PACKED_ICE, 3, 1, 64, 1),
        new SellItemFactory(Items.BLUE_ICE, 3, 1, 64, 1),
        new SellItemFactory(Items.PUFFERFISH_BUCKET, 4, 1, 8, 1),
        new SellItemFactory(Items.TROPICAL_FISH_BUCKET, 4, 1, 8, 1),
        new SellItemFactory(Items.SWEET_BERRIES, 2, 1, 16, 1),
        new SellItemFactory(Items.BAMBOO, 2, 1, 32, 1),
        new SellItemFactory(Items.TERRACOTTA, 1, 1, 64, 1),
        new SellItemFactory(Items.PRISMARINE_SHARD, 4, 1, 28, 1)
    };

    public static TradeOffers.Factory[] DefaultRareTrades = new TradeOffers.Factory[] {
        new SellItemFactory(Items.ENCHANTED_GOLDEN_APPLE, 56, 1, 1, 1),
        new SellItemFactory(Items.TOTEM_OF_UNDYING, 56, 1, 1, 1),
        new SellItemFactory(Items.WITHER_SKELETON_SKULL, 64, 1, 1, 1),
        new SellItemFactory(Items.TRIDENT, 25, 1, 2, 1),
        new SellItemFactory(Items.GHAST_TEAR, 12, 1, 4, 1),
        new SellItemFactory(Items.HEART_OF_THE_SEA, 56, 1, 2, 1),
        new SellItemFactory(Items.DRAGON_BREATH, 32, 1, 3, 1),
        new SellItemFactory(Items.SHULKER_SHELL, 16, 1, 3, 1),
        new SellItemFactory(Items.ENDER_EYE, 16, 1, 3, 1),
        new SellItemFactory(Items.DIAMOND_HELMET, 16, 1, 2, 1),
        new SellItemFactory(Items.DIAMOND_CHESTPLATE, 16, 1, 2, 1),
        new SellItemFactory(Items.DIAMOND_LEGGINGS, 16, 1, 2, 1),
        new SellItemFactory(Items.DIAMOND_BOOTS, 16, 1, 2, 1),
        new SellItemFactory(Items.DIAMOND_PICKAXE, 16, 1, 2, 1),
        new SellItemFactory(Items.DIAMOND_SWORD, 16, 1, 2, 1),
        new SellItemFactory(Items.DIAMOND_AXE, 16, 1, 2, 1) 
    };
}
