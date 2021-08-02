package net.hyper_pigeon.better_wandering_trader.config;

import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

public class UserTradeListGroup implements IUserTradeGenerator {
    public enum ListInclusionMode {
        all, // process all lists.
        select_random, // randomly select lists to use.
        independent_random, // each list has a random chance to be included.
    }

    // Options for inclusion modes:
    public static class AllInclusionOptions {
    }

    public static class SelectRandomInclusionOptions {
        int count = 0;
    }

    public static class IndependentRandomInclusionOptions {
        int count = 0;
    }

    public float selction_weight = 1;
    public String label = "";

    public ListInclusionMode inclusion_mode = ListInclusionMode.all;
    public AllInclusionOptions all_inclusion_options = null;
    public SelectRandomInclusionOptions select_random_inclusion_options = null;
    public IndependentRandomInclusionOptions independent_random_inclusion_options = null;

    public TradeFormat trades[] = new TradeFormat[0];
    public UserTradeListGroup[] trade_groups = new UserTradeListGroup[0];

    private transient IUserTradeGenerator[] allGenerators;
    private transient float total_weight = -1;

    public IUserTradeGenerator[] getAllGenerators() {
        if (allGenerators == null) {
            allGenerators = new IUserTradeGenerator[trades.length + trade_groups.length];

            int i = 0;
            for (int j = 0; j < trades.length; j++) {
                allGenerators[i] = trades[j];
                i++;
            }
            for (int j = 0; j < trade_groups.length; j++) {
                allGenerators[i] = trade_groups[j];
                i++;
            }
        }
        return allGenerators;
    }

    public void validate() {
        // Adding inclusion options if missing:
        if (inclusion_mode == ListInclusionMode.all) {
            if (all_inclusion_options == null) {
                all_inclusion_options = new AllInclusionOptions();
            }
        }

        if (inclusion_mode == ListInclusionMode.select_random) {
            if (select_random_inclusion_options == null) {
                select_random_inclusion_options = new SelectRandomInclusionOptions();
            }
        }

        if (inclusion_mode == ListInclusionMode.independent_random) {
            if (independent_random_inclusion_options == null) {
                independent_random_inclusion_options = new IndependentRandomInclusionOptions();
            }
        }

        // Validate any generators:
        for (IUserTradeGenerator i : getAllGenerators()) {
            i.validate();
        }
    }

    @Override
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant) {

        // Include All:
        if (inclusion_mode == ListInclusionMode.all) {
            for (IUserTradeGenerator i : getAllGenerators()) {
                if (i == null) {
                    continue;
                }
                i.addTradeOffers(tradeOfferList, merchant);
            }
        }

        if (inclusion_mode == ListInclusionMode.select_random) {
            TradeListUtils.fillWeightedRandomTradesFromPool(tradeOfferList, merchant, getAllGenerators(),
                    select_random_inclusion_options.count, getTotalWeight());            
        }
    }

    @Override
    public float getWeight() {
        return selction_weight;
    }

    public float getTotalWeight() {
        if (total_weight < 0) {
            // meaning that we've not calculated the total_weight yet:
            total_weight = 0;
            for (IUserTradeGenerator i : getAllGenerators()) {
                if (i == null) {
                    continue;
                }
                total_weight += i.getWeight();
            }
        }
        return total_weight;
    }
}