package net.lt_schmiddy.super_configurable_wandering_trader.trades;

import net.lt_schmiddy.super_configurable_wandering_trader.generators.TradeGroupFile;
import net.lt_schmiddy.super_configurable_wandering_trader.factories.SellItemFactory;
import net.lt_schmiddy.super_configurable_wandering_trader.generators.TradeGroup;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;

public class ExampleConfig {
    public static final String EXAMPLE_FILE_NAME = "example.json";

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

    public static TradeGroupFile createExampleTradeConfig() {       
        TradeGroup common_group = new TradeGroup();
        common_group.label = "Common Trades";
        common_group.inclusion_mode = TradeGroup.ListInclusionMode.select_random;
        common_group.select_random_inclusion_options = new TradeGroup.SelectRandomInclusionOptions();
        common_group.select_random_inclusion_options.count = 5;
        common_group.loadedGenerators.put("items", SellItemFactory.toTradeFormat(DefaultCommonTrades));
        common_group.saveGenerators();

        TradeGroup rare_group = new TradeGroup();
        rare_group.label = "Rare Trades";
        rare_group.inclusion_mode = TradeGroup.ListInclusionMode.select_random;
        rare_group.select_random_inclusion_options = new TradeGroup.SelectRandomInclusionOptions();
        rare_group.select_random_inclusion_options.count = 1;
        rare_group.loadedGenerators.put("items", SellItemFactory.toTradeFormat(DefaultRareTrades));
        rare_group.saveGenerators();

        TradeGroupFile config = new TradeGroupFile();
        config.root.label = "Example Config";
        config.root.loadedGenerators.put("groups", new TradeGroup[] { common_group, rare_group });
        config.root.saveGenerators();
        return config;
    }
}
