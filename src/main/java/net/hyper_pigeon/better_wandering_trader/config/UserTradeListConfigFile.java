package net.hyper_pigeon.better_wandering_trader.config;

import java.nio.file.Path;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

import java.util.List;

public class UserTradeListConfigFile implements IUserTradeGenerator {

    public transient Path path;

    public enum ConfigFileType {
        exclusive, // No other exclusive configs will be used.
        additive, // can be appended to other configs.
    }

    public static class ExclusiveTypeOptions {
        public boolean allow_additives = true;
    }
    public static class AdditiveTypeOptions {}

    public ConfigFileType config_type = ConfigFileType.exclusive;
    public ExclusiveTypeOptions exclusive_type_options = null;
    public AdditiveTypeOptions additive_type_options = null;

    public UserTradeListGroup root = new UserTradeListGroup();

    public void validate() {
        // Adding type options if missing:
        if (config_type == ConfigFileType.exclusive) {
            if (exclusive_type_options == null) {
                exclusive_type_options = new ExclusiveTypeOptions();
            }

        } else if (config_type == ConfigFileType.additive) {
            if (additive_type_options == null) {
                additive_type_options = new AdditiveTypeOptions();
            }

        }
        root.validate();
    }


    // On processing as an exclusive, the additives list will be send here for processing:
    public boolean handleAdditives(List<UserTradeListConfigFile> additives, float totalWeight) {
        // Cases where exclusives shouldn't be processed.
        if (config_type != ConfigFileType.exclusive || exclusive_type_options == null || exclusive_type_options.allow_additives == false) {return false;}



        return true;
    }

    @Override
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant) {
        root.addTradeOffers(tradeOfferList, merchant);
        
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
    public IUserTradeGenerator[] getAllGenerators() {
        return root.getAllGenerators();
    }
}
