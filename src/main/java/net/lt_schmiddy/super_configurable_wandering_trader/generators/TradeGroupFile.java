package net.lt_schmiddy.super_configurable_wandering_trader.generators;

import java.nio.file.Path;

import net.lt_schmiddy.super_configurable_wandering_trader.abstracts.ANamedTradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.interfaces.ITradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.TradeConfigHandler;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.TradeListUtils;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

import java.util.List;
import java.util.Random;

public class TradeGroupFile extends ANamedTradeGenerator {
    public transient Path path;

    public enum ConfigFileType {
        exclusive, // No other exclusive configs will be used.
        additive, // can be appended to other configs.
        subconfig, // Used as part of another config.
    }

    public static class ExclusiveTypeOptions {
        public boolean allow_additives = false;
        public static class AllowAdditiveOptions {
            int count = 1;
        }
        public AllowAdditiveOptions allow_additive_options = null;
    }
    public static class AdditiveTypeOptions {}
    public static class SubconfigTypeOptions {}

    public ConfigFileType config_type = ConfigFileType.exclusive;
    public ExclusiveTypeOptions exclusive_type_options = null;
    public AdditiveTypeOptions additive_type_options = null;
    public SubconfigTypeOptions subconfig_type_options = null;

    public TradeGroup root = new TradeGroup();

    public void validate() {
        // Adding type options if missing:
        if (config_type == ConfigFileType.exclusive) {
            if (exclusive_type_options == null) {
                exclusive_type_options = new ExclusiveTypeOptions();
            }
            if (exclusive_type_options.allow_additives && exclusive_type_options.allow_additive_options == null) {
                exclusive_type_options.allow_additive_options = new ExclusiveTypeOptions.AllowAdditiveOptions();
            }

        } else if (config_type == ConfigFileType.additive) {
            if (additive_type_options == null) {
                additive_type_options = new AdditiveTypeOptions();
            }

        } else if (config_type == ConfigFileType.subconfig) {
            if (subconfig_type_options == null) {
                subconfig_type_options = new SubconfigTypeOptions();
            }

        }
        root.setParent(this);
        root.validate();
    }


    // On processing as an exclusive, the additives list will be send here for processing:
    public boolean handleAdditives(List<TradeGroupFile> additives, float totalWeight, Random random) {
        // Cases where exclusives shouldn't be processed.    

        return true;
    }

    @Override
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant, Random random) {
        root.addTradeOffers(tradeOfferList, merchant, random);

        if (
            config_type != ConfigFileType.exclusive 
            || exclusive_type_options == null 
            || exclusive_type_options.allow_additives == false 
            || exclusive_type_options.allow_additive_options == null
        ) {return;}

        // If we've made it this far, we're an exclusive file that is allowed to append additives():
        TradeListUtils.fillWeightedRandomTradesFromPool(
            tradeOfferList,
            merchant,
            TradeConfigHandler.getAdditiveTradeFiles(),
            exclusive_type_options.allow_additive_options.count,
            TradeConfigHandler.getAdditivesTotalWeight(),
            random
        );
        
    }

    @Override
    public float getWeight() {
        return root.getWeight();
    }

    // @Override
    public float getTotalWeight() {
        return root.getTotalWeight();
    }

    // @Override
    public ITradeGenerator[] getAllGenerators() {
        return root.getAllGenerators();
    }

    @Override
    public String getName() {
        return path.getFileName().toString();
    }


    @Override
    public int getSortOrder() {
        return root.getSortOrder();
    }
}
