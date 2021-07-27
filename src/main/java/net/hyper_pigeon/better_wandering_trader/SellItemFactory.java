package net.hyper_pigeon.better_wandering_trader;

import java.util.Random;

import net.hyper_pigeon.better_wandering_trader.BetterWanderingTraderConfig.TradeFormat;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

public class SellItemFactory implements TradeOffers.Factory {
    private final ItemStack sell;
    private final int price;
    private final int count;
    private final int maxUses;
    private final int experience;
    private final float multiplier;

    public TradeFormat asTradeFormat() {
        TradeFormat retVal = new TradeFormat();
        retVal.identifier = Registry.ITEM.getId(sell.getItem()).toString();
        retVal.price = price;
        retVal.count = count;
        retVal.maxUses = maxUses;
        retVal.experience = experience;

        return retVal;
    }

    public SellItemFactory(Block block, int i, int j, int k, int l) {
        this(new ItemStack(block), i, j, k, l);
    }

    public SellItemFactory(Item item, int i, int j, int k) {
        this((ItemStack) (new ItemStack(item)), i, j, 12, k);
    }

    public SellItemFactory(Item item, int i, int j, int k, int l) {
        this(new ItemStack(item), i, j, k, l);
    }

    public SellItemFactory(ItemStack itemStack, int i, int j, int k, int l) {
        this(itemStack, i, j, k, l, 0.05F);
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

    public TradeOffer create(Entity entity, Random random) {
        return new TradeOffer(new ItemStack(Items.EMERALD, this.price),
                new ItemStack(this.sell.getItem(), this.count), this.maxUses, this.experience, this.multiplier);
    }

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
