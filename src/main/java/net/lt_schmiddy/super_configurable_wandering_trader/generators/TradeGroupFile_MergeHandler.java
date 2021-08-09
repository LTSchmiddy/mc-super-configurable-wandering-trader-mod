package net.lt_schmiddy.super_configurable_wandering_trader.generators;

import java.nio.file.Path;
import java.util.Random;

import net.lt_schmiddy.super_configurable_wandering_trader.abstracts.ATradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.trades.TradeConfigHandler;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

public class TradeGroupFile_MergeHandler extends ATradeGenerator {

    public String relative_path = "";

    public transient Path refPath;
    public transient TradeGroupFile ref;
    
    // These methods may be useful elsewhere:
    public void loadRef() {
        ref = TradeConfigHandler.loadFile(refPath.toFile());
    }

    public void saveRef() {
        TradeConfigHandler.saveFile(refPath.toFile(), ref);
    }

    @Override
    public void validate() {
        refPath = getRelativeDir().resolve(relative_path);
        System.out.println("Merge Path: " + refPath);

        // Attempt to load the referenced file:
        loadRef();
        if (ref == null) {
            System.out.println("ERROR in '" + getRootName() + "': the merge file path '" + refPath + "' could not be used.");
            return;
        }

        System.out.println(getRootName() + ": merge file path '" + refPath + "' has been loaded.");

        if (TradeConfigHandler.isIgnoredFile(refPath)){
            ref.config_type = TradeGroupFile.ConfigFileType.subconfig;
        }

        ref.setParent(this);
        ref.validate();

        saveRef();
        
    }

    @Override
    public void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant, Random random) {
        // Never used.
        return;
    }
}
