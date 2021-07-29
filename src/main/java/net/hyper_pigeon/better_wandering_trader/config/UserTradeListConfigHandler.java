package net.hyper_pigeon.better_wandering_trader.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;
import net.hyper_pigeon.better_wandering_trader.listings.TradeFileListing;
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

    public static List<TradeFileListing> fileListings = new ArrayList<TradeFileListing>();
    // public static List<TradeListing> tradeArrays;

    public static void addTradeOffers(TradeOfferList tradeOfferList, MerchantEntity merchant) {
        for (TradeFileListing i : fileListings) {
            i.addTradeOffers(tradeOfferList, merchant);
        }
    }

    public static void saveFile(File configFile, UserTradeListConfigFile config){
        String json = gson.toJson(config);
        try {
            FileOutputStream stream = new FileOutputStream(configFile);
            stream.write(json.getBytes(StandardCharsets.UTF_8));
            stream.close();
        } catch (IOException e) {
            System.out.println("Error occurred while saving config file: " + e.getMessage());
        }
    }

    public static void loadAll(String path) {
        fileListings.clear();
        Path userTradesRoot = configRoot.resolve(path);
        // Create the trade folder if not present:
        if (userTradesRoot.toFile().mkdirs()) {
            setupUserTradesDir(path);
        }

        // Scans the User Trades folder for all trade configs.
        try {
            Files.walk(userTradesRoot)
            .filter(Files::isRegularFile)
            .forEach(UserTradeListConfigHandler::loadAll_ForPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadAll_ForPath(Path path) {
        File configFile = path.toFile();
        UserTradeListConfigFile config = loadFile(configFile);

        fileListings.add(new TradeFileListing(path, config));
    }

    public static UserTradeListConfigFile loadFile(File configFile){
        UserTradeListConfigFile config;

        if (configFile.exists()) {
            try {
                FileReader reader = new FileReader(configFile);
                config =  gson.fromJson(reader, UserTradeListConfigFile.class);
            } catch (IOException e) {
                System.out.println("Error occurred while loading config file " + e.getMessage());
                config = new UserTradeListConfigFile();
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
