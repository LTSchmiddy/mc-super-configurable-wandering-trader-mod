package net.hyper_pigeon.better_wandering_trader.trade_info;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.loader.api.FabricLoader;
import net.hyper_pigeon.better_wandering_trader.interfaces.INamedUserTradeGenerator;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserTradeListConfigHandler {
    public static final String EXAMPLE_FILE_NAME = "example.json";

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Path configRoot = FabricLoader.getInstance().getConfigDir();

    public static List<UserTradeListConfigFile> userTradeFiles = new ArrayList<UserTradeListConfigFile>();

    // Exclusives Info:
    public static List<INamedUserTradeGenerator> exclusiveTradeGenerators = new ArrayList<INamedUserTradeGenerator>();
    public static float exclusivesTotalWeight = 0;
    
    // Additives Info:
    protected static List<INamedUserTradeGenerator> additiveTradeGenerators = new ArrayList<INamedUserTradeGenerator>();
    protected static float additivesTotalWeight = 0;

    public static Random configSelectorRandom = new Random();

    public static INamedUserTradeGenerator[] getAdditiveTradeFiles() {
        INamedUserTradeGenerator[] retVal = new INamedUserTradeGenerator[additiveTradeGenerators.size()];
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
        INamedUserTradeGenerator selected = selectExclusive();
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

    public static INamedUserTradeGenerator selectExclusive() {
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

    public static void saveFile(File configFile, UserTradeListConfigFile config) {
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
        List<UserTradeListConfigFile> checkedTradeFiles = new ArrayList<UserTradeListConfigFile>();
        List<INamedUserTradeGenerator> checkedExclusives = new ArrayList<INamedUserTradeGenerator>();
        List<INamedUserTradeGenerator> checkedAdditives = new ArrayList<INamedUserTradeGenerator>();
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

    protected static void processTradeFiles(List<UserTradeListConfigFile> tradeFiles,
            List<INamedUserTradeGenerator> exclusives, List<INamedUserTradeGenerator> additives) {
        exclusivesTotalWeight = 0;
        additivesTotalWeight = 0;
        
        for (UserTradeListConfigFile i : tradeFiles) {
            if (i.config_type == UserTradeListConfigFile.ConfigFileType.exclusive) {
                exclusives.add(i);
                exclusivesTotalWeight += i.getWeight();

            } else if (i.config_type == UserTradeListConfigFile.ConfigFileType.additive) {
                additives.add(i);
                additivesTotalWeight += i.getWeight();
            }
        }

    }

    protected static void readUserTrades(String path, List<UserTradeListConfigFile> tradeFiles) {
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

    private static void loadAll_ForPath(Path path, List<UserTradeListConfigFile> tradeFiles) {
        File configFile = path.toFile();
        UserTradeListConfigFile config = loadFile(configFile);
        // loadFile will return null if the config was malformed, since we want to
        // ignore those:
        if (config == null) {
            return;
        }

        config.validate();
        tradeFiles.add(config);
        // We save the file after it has been processed to ensure it has correct
        // formatting:
        saveFile(configFile, config);
    }

    public static UserTradeListConfigFile loadFile(File configFile) {
        UserTradeListConfigFile config;

        if (configFile.exists()) {
            try {
                FileReader reader = new FileReader(configFile);
                config = gson.fromJson(reader, UserTradeListConfigFile.class);
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

    public static UserTradeListConfigFile createFile(File configFile, UserTradeListConfigFile config) {
        if (config == null) {
            config = new UserTradeListConfigFile();
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

    public static UserTradeListConfigFile createExampleTradeConfig() {       
        UserTradeListGroup common_group = new UserTradeListGroup();
        common_group.label = "Common Trades";
        common_group.inclusion_mode = UserTradeListGroup.ListInclusionMode.select_random;
        common_group.select_random_inclusion_options = new UserTradeListGroup.SelectRandomInclusionOptions();
        common_group.select_random_inclusion_options.count = 5;
        common_group.trades = SellItemFactory.toTradeFormat(SellItemFactory.DefaultCommonTrades);

        UserTradeListGroup rare_group = new UserTradeListGroup();
        rare_group.label = "Rare Trades";
        rare_group.inclusion_mode = UserTradeListGroup.ListInclusionMode.select_random;
        rare_group.select_random_inclusion_options = new UserTradeListGroup.SelectRandomInclusionOptions();
        rare_group.select_random_inclusion_options.count = 1;
        rare_group.trades = SellItemFactory.toTradeFormat(SellItemFactory.DefaultRareTrades);

        UserTradeListConfigFile config = new UserTradeListConfigFile();
        config.root.label = "Example Config";
        config.root.trade_groups = new UserTradeListGroup[] { common_group, rare_group };
        return config;
    }

    public static void setupUserTradesDir(String path) {
        Path userTradesRoot = configRoot.resolve(path);
        userTradesRoot.toFile().mkdirs();

        File exampleFile = userTradesRoot.resolve(EXAMPLE_FILE_NAME).toFile();
        createFile(exampleFile, createExampleTradeConfig());
    }
}
