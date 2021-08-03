package net.lt_schmiddy.super_configurable_wandering_trader.trade_info;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.loader.api.FabricLoader;
import net.lt_schmiddy.super_configurable_wandering_trader.generators.TradeGroupFile;
import net.lt_schmiddy.super_configurable_wandering_trader.interfaces.INamedTradeGenerator;
import net.lt_schmiddy.super_configurable_wandering_trader.interfaces.ITradeGenerator;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TradeConfigHandler {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Path configRoot = FabricLoader.getInstance().getConfigDir();

    
    public static Map<String, Class<? extends ITradeGenerator>> generatorTypes = new HashMap<String, Class<? extends ITradeGenerator>>();

    public static void registerGeneratorType(String id, Class<? extends ITradeGenerator> classType) {
        generatorTypes.put(id, classType);
    }

    public static List<TradeGroupFile> userTradeFiles = new ArrayList<TradeGroupFile>();

    // Exclusives Info:
    public static List<INamedTradeGenerator> exclusiveTradeGenerators = new ArrayList<INamedTradeGenerator>();
    public static float exclusivesTotalWeight = 0;
    
    // Additives Info:
    protected static List<INamedTradeGenerator> additiveTradeGenerators = new ArrayList<INamedTradeGenerator>();
    protected static float additivesTotalWeight = 0;

    public static Random configSelectorRandom = new Random();

    public static INamedTradeGenerator[] getAdditiveTradeFiles() {
        INamedTradeGenerator[] retVal = new INamedTradeGenerator[additiveTradeGenerators.size()];
        for (int i = 0; i < retVal.length; i++){
            retVal[i] = additiveTradeGenerators.get(i);
        }
        return retVal;
    }

    public static float getAdditivesTotalWeight() {
        return additivesTotalWeight;
    }

    public static void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant) {

        // First, select and run an exclusive config:
        INamedTradeGenerator selected = selectExclusive();
        if (selected == null) {
            return;
        }

        System.out.println("Wandering Trader config '" + selected.getName() + "' selected.");

        Random random = new Random();
        long seed = random.nextLong();
        System.out.println("Trader Seed: " + seed);
        random.setSeed(seed);

        selected.addTradeOffers(tradeOfferList, merchant, random);
    }

    public static INamedTradeGenerator selectExclusive() {
        float selection = configSelectorRandom.nextFloat() * exclusivesTotalWeight;
            
        if (exclusiveTradeGenerators.size() < 1) {
            return null;
        }

        float current = 0;
        for(int i = 0; i < exclusiveTradeGenerators.size(); i++) {            
            current += exclusiveTradeGenerators.get(i).getWeight();
            
            // The first trade where current >= selection is the trade corresponding to selection. Add it, and continue:
            if (current >= selection) {
                return exclusiveTradeGenerators.get(i);
            }
        }

        return null;
    }

    public static void saveFile(File configFile, TradeGroupFile config) {
        String json = gson.toJson(config);
        try {
            FileOutputStream stream = new FileOutputStream(configFile);
            stream.write(json.getBytes(StandardCharsets.UTF_8));
            stream.close();
        } catch (IOException e) {
            System.out.println("Error occurred while saving config file: " + e.getMessage());
        }
    }

    public static void checkAll(String path) {
        List<TradeGroupFile> checkedTradeFiles = new ArrayList<TradeGroupFile>();
        List<INamedTradeGenerator> checkedExclusives = new ArrayList<INamedTradeGenerator>();
        List<INamedTradeGenerator> checkedAdditives = new ArrayList<INamedTradeGenerator>();
        readUserTrades(path, checkedTradeFiles);
        processTradeFiles(userTradeFiles, checkedExclusives, checkedAdditives);

        System.out.println("User Trades Checked!");
    }

    public static void loadAll(String path) {
        userTradeFiles.clear();
        exclusiveTradeGenerators.clear();
        additiveTradeGenerators.clear();

        readUserTrades(path, userTradeFiles);
        processTradeFiles(userTradeFiles, exclusiveTradeGenerators, additiveTradeGenerators);
        System.out.println("User Trades Loaded!");
    }

    protected static void processTradeFiles(List<TradeGroupFile> tradeFiles,
            List<INamedTradeGenerator> exclusives, List<INamedTradeGenerator> additives) {
        exclusivesTotalWeight = 0;
        additivesTotalWeight = 0;
        
        for (TradeGroupFile i : tradeFiles) {
            if (i.config_type == TradeGroupFile.ConfigFileType.exclusive) {
                exclusives.add(i);
                exclusivesTotalWeight += i.getWeight();

            } else if (i.config_type == TradeGroupFile.ConfigFileType.additive) {
                additives.add(i);
                additivesTotalWeight += i.getWeight();
            }
        }

    }

    protected static void readUserTrades(String path, List<TradeGroupFile> tradeFiles) {
        Path userTradesRoot = configRoot.resolve(path);
        // Create the trade folder if not present:
        if (userTradesRoot.toFile().mkdirs()) {
            setupUserTradesDir(path);
        }
        // Scans the User Trades folder for all trade configs.
        try {
            Files.walk(userTradesRoot).filter(Files::isRegularFile).forEach((Path fp) -> {
                loadAll_ForPath(fp, tradeFiles);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadAll_ForPath(Path path, List<TradeGroupFile> tradeFiles) {
        File configFile = path.toFile();
        TradeGroupFile config = loadFile(configFile);
        // loadFile will return null if the config was malformed, since we want to
        // ignore those:
        if (config == null) {
            return;
        }
        config.root.loadGenerators();
        config.validate();
        tradeFiles.add(config);
        // We save the file after it has been processed to ensure it has correct
        // formatting:
        saveFile(configFile, config);
    }

    public static TradeGroupFile loadFile(File configFile) {
        TradeGroupFile config;

        if (configFile.exists()) {
            try {
                FileReader reader = new FileReader(configFile);
                config = gson.fromJson(reader, TradeGroupFile.class);
                config.path = configFile.toPath();
            } catch (JsonSyntaxException e) {
                System.out.println("ERROR: Wandering Trader configuration '" + configFile.getName()
                        + "' has invalid syntax - " + e.getMessage());
                config = null;
            } catch (IOException e) {
                System.out.println("Error occurred while loading config file " + e.getMessage());
                config = null;
            }
        } else {
            // config = create(configFile);
            config = null;
        }

        return config;
    }

    public static TradeGroupFile createFile(File configFile, TradeGroupFile config) {
        if (config == null) {
            config = new TradeGroupFile();
        }
        try {
            // new File("config/SmithAndFletch/").mkdirs();
            FileOutputStream stream = new FileOutputStream(configFile);
            String json = gson.toJson(config);
            stream.write(json.getBytes(StandardCharsets.UTF_8));
            stream.close();
        } catch (IOException e) {
            System.out.println("Error occurred while creating config file " + e.getMessage());
        }
        return config;
    }

    public static void setupUserTradesDir(String path) {
        Path userTradesRoot = configRoot.resolve(path);
        userTradesRoot.toFile().mkdirs();

        File exampleFile = userTradesRoot.resolve(ExampleConfig.EXAMPLE_FILE_NAME).toFile();
        createFile(exampleFile, ExampleConfig.createExampleTradeConfig());
    }
}
