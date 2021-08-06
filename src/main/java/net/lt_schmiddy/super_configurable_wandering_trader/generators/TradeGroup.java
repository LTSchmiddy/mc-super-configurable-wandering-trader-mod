package net.lt_schmiddy.super_configurable_wandering_trader.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonObject;

import net.lt_schmiddy.super_configurable_wandering_trader.abstracts.ATradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.interfaces.ITradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.TradeConfigHandler;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.TradeListUtils;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

public class TradeGroup extends ATradeGenerator {
    // Instance Section:
    public enum ListInclusionMode {
        all, // process all lists.
        select_random, // randomly select lists to use.
        independent_random, // each list has a random chance to be included.
    }

    // Options for inclusion modes:
    public static class AllInclusionOptions {}

    public static class SelectRandomInclusionOptions {
        public int count = 0;
    }

    public static class IndependentRandomInclusionOptions {
        float out_of = 0;
    }

    public float selction_weight = 1;
    public String label = "";

    public ListInclusionMode inclusion_mode = ListInclusionMode.all;
    public AllInclusionOptions all_inclusion_options = null;
    public SelectRandomInclusionOptions select_random_inclusion_options = null;
    public IndependentRandomInclusionOptions independent_random_inclusion_options = null;

    // public TradeFormat trades[] = new TradeFormat[0];
    // public TradeGroup[] trade_groups = new TradeGroup[0];

    public TradeGroupFileReference[] merge_with_files = new TradeGroupFileReference[0];

    public transient Map<String, ITradeGenerator[]> loadedGenerators = new HashMap<String, ITradeGenerator[]>();
    public Map<String, JsonObject[]> generators = new HashMap<String, JsonObject[]>();

    private transient List<ITradeGenerator> allGenerators;
    private transient float total_weight = -1;



    public void loadGenerators() {
        // Add missing type sections:
        for (String i : TradeConfigHandler.generatorTypes.keySet()) {
            generators.putIfAbsent(i, new JsonObject[0]);
        }

        // Now, we load:
        allGenerators = new ArrayList<ITradeGenerator>();

        // Constructing the loadedGenerators map (which we need for saving later):
        loadedGenerators.clear();
        for (Map.Entry<String, JsonObject[]> i : generators.entrySet()){
            if (!TradeConfigHandler.generatorTypes.containsKey(i.getKey())){continue;}

            ITradeGenerator[] newArray = new ITradeGenerator[i.getValue().length];
            int index = 0;
            // Skip if type was undefined:
            for (JsonObject j : i.getValue()) {
                ITradeGenerator gen = TradeConfigHandler.gson.fromJson(j, TradeConfigHandler.generatorTypes.get(i.getKey()));

                // If we loaded another trade group, load it's generators:
                allGenerators.add(gen);
                newArray[index++] = gen;
            }
            loadedGenerators.put(i.getKey(), newArray);
        }
    }

    public void saveGenerators() {
        for (String i : TradeConfigHandler.generatorTypes.keySet()) {
            loadedGenerators.putIfAbsent(i, new ITradeGenerator[0]);
        }
        generators.clear();
        for (Map.Entry<String, ITradeGenerator[]> i : loadedGenerators.entrySet()){
            if (!TradeConfigHandler.generatorTypes.containsKey(i.getKey())){continue;}

            JsonObject[] newArray = new JsonObject[i.getValue().length];
            int index = 0;
            // Skip if type was undefined:
            for (ITradeGenerator j : i.getValue()) {
                JsonObject gen = (JsonObject)TradeConfigHandler.gson.toJsonTree(j, j.getClass());

                // If we loaded another trade group, load it's generators:
                newArray[index++] = gen;
            }
            generators.put(i.getKey(), newArray);
        }
    }

    public ITradeGenerator[] getAllGenerators() {
        if (allGenerators == null) {
            loadGenerators();
        }
        ITradeGenerator[] array = new ITradeGenerator[allGenerators.size()];
        allGenerators.toArray(array);
        return array;
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


        // Loading sub-generators:
        loadGenerators();

        

        // Validate any generators:
        for (ITradeGenerator i : getAllGenerators()) {
            i.setParent(this);
            i.validate();
        }

        saveGenerators();

        // Loading Merges:
        for (TradeGroupFileReference i : merge_with_files) {
            
            // Even though we're merging, we're going to use this as the parent to 
            // indicate where the actual file reference is happening.
            i.setParent(this);
            i.validate();

            // Once the reference is set up, we add all its generators to ours.
            // The reference itself is never added to our generators, however.
            // We also set much of refs configuration to match ours.
            if (i.ref != null) {
                allGenerators.addAll(i.ref.root.allGenerators);
            }
        }
    }

    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant, Random random) {

        // Include All:
        if (inclusion_mode == ListInclusionMode.all) {
            for (ITradeGenerator i : getAllGenerators()) {
                if (i == null) {
                    continue;
                }
                i.addTradeOffers(tradeOfferList, merchant, random);
            }
        }

        if (inclusion_mode == ListInclusionMode.select_random) {
            TradeListUtils.fillWeightedRandomTradesFromPool(tradeOfferList, merchant, getAllGenerators(),
                    select_random_inclusion_options.count, getTotalWeight(), random);            
        }

        
        if (inclusion_mode == ListInclusionMode.independent_random) {
            for (ITradeGenerator i : getAllGenerators()) {
                float odds = random.nextFloat() * independent_random_inclusion_options.out_of;

                if (odds <= i.getWeight()) {
                    i.addTradeOffers(tradeOfferList, merchant, random);
                }
            }
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
            for (ITradeGenerator i : getAllGenerators()) {
                if (i == null) {
                    continue;
                }
                total_weight += i.getWeight();
            }
        }
        return total_weight;
    }
}