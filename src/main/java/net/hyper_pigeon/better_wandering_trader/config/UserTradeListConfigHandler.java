package net.hyper_pigeon.better_wandering_trader.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOfferList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserTradeListConfigHandler {
    public static final String EXAMPLE_FILE_NAME = "example.json";

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Path configRoot = FabricLoader.getInstance().getConfigDir();

    public static List<UserTradeListConfigFile> userTradeFiles = new ArrayList<UserTradeListConfigFile>();

    // Exclusives Info:
    public static List<UserTradeListConfigFile> exclusiveTradeFiles = new ArrayList<UserTradeListConfigFile>();
    public static float exclusivesTotalWeight = 0;
    
    // Additives Info:
    public static List<UserTradeListConfigFile> additiveTradeFiles = new ArrayList<UserTradeListConfigFile>();
    public static float additivesTotalWeight = 0;
    // public static List<TradeListing> tradeArrays;

    public static void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant) {
        // First, select and run an exclusive config:
        UserTradeListConfigFile selected = selectExclusive();
        if (selected == null) {
            return;
        }

        System.out.println("Wandering Trader config '" + selected.path.getFileName() + "' selected.");

        selected.addTradeOffers(tradeOfferList, merchant);
        selected.handleAdditives(additiveTradeFiles, additivesTotalWeight);
    }

    public static UserTradeListConfigFile selectExclusive() {
        float selection = TradeListUtils.random.nextFloat() * exclusivesTotalWeight;
            
        if (exclusiveTradeFiles.size() < 1) {
            return null;
        }

        float current = 0;
        for(int i = 0; i < exclusiveTradeFiles.size(); i++) {            
            current += exclusiveTradeFiles.get(i).getWeight();
            
            // The first trade where current >= selection is the trade corresponding to selection. Add it, and continue:
            if (current >= selection) {
                return exclusiveTradeFiles.get(i);
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
        List<UserTradeListConfigFile> checkedExclusives = new ArrayList<UserTradeListConfigFile>();
        List<UserTradeListConfigFile> checkedAdditives = new ArrayList<UserTradeListConfigFile>();
        readUserTrades(path, checkedTradeFiles);
        processTradeFiles(userTradeFiles, checkedExclusives, checkedAdditives);

        System.out.println("User Trades Checked!");
    }

    public static void loadAll(String path) {
        userTradeFiles.clear();
        exclusiveTradeFiles.clear();
        additiveTradeFiles.clear();

        readUserTrades(path, userTradeFiles);
        processTradeFiles(userTradeFiles, exclusiveTradeFiles, additiveTradeFiles);
        System.out.println("User Trades Loaded!");
    }

    protected static void processTradeFiles(List<UserTradeListConfigFile> tradeFiles,
            List<UserTradeListConfigFile> exclusives, List<UserTradeListConfigFile> additives) {
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

    public static void setupUserTradesDir(String path) {
        Path userTradesRoot = configRoot.resolve(path);
        userTradesRoot.toFile().mkdirs();

        File exampleFile = userTradesRoot.resolve(EXAMPLE_FILE_NAME).toFile();
        createFile(exampleFile, null);
    }
}
