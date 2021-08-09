package net.lt_schmiddy.super_configurable_wandering_trader.trades;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.lt_schmiddy.super_configurable_wandering_trader.config.ConfigHandler;
import net.lt_schmiddy.super_configurable_wandering_trader.interfaces.INamedTradeGenerator;

public class TraderQueue {

    public static class TraderQueueEntry {
        public String config_path = "";
        public Long seed = null;

        public TraderQueueEntry() {}
        

        public TraderQueueEntry(String p_config_path, Long p_seed) {
            config_path = p_config_path;
            seed = p_seed;
            
        }

        public Path getResolvedPath() {
            return ConfigHandler.config.trades.getTradesPath().resolve(config_path);
        }

        public boolean doesExist() {
            Path path = getResolvedPath();
            return path.toFile().exists();
        }

        public INamedTradeGenerator getGenerator() {
            Path path = getResolvedPath();

            if (!path.toFile().exists()) {
                return null;
            }

            return TradeConfigHandler.loadConfigFromFile(path.toFile());

        }
    }

    public TraderQueueEntry locked_entry = null;
    public List<TraderQueueEntry> queue = new ArrayList<TraderQueueEntry>();

    protected TraderQueueEntry newEntry(String configPath, Long seed){
        TraderQueueEntry entry = new TraderQueueEntry(configPath, seed);

        if (!entry.doesExist()) {
            System.out.println("ERROR: Cannot find '" + configPath + "'");
            return null;
        }

        return entry;
    }

    public TraderQueueEntry getNext() {
        if (locked_entry != null) {
            return locked_entry;
        }

        if (queue.size() > 0) {
            TraderQueueEntry retVal = queue.get(0);
            queue.remove(0);
            return retVal;
        }

        return null;
    }

    public int setLock(String configPath) {
        return setLock(configPath, null);
    }
    public int setLock(String configPath, Long seed) {
        TraderQueueEntry entry = newEntry(configPath, seed);

        if (entry == null) {
            return -1;
        }

        locked_entry = entry;
        return 1;
    }

    public int clearLock() {
        locked_entry = null;
        return 1;
    }

    // Enqueue Operations:
    public int enqueue(String configPath) {
        return enqueue(configPath, null);
    }

    public int enqueue(String configPath, Long seed) {
        TraderQueueEntry entry = newEntry(configPath, seed);

        if (entry == null) {
            return -1;
        }

        queue.add(entry);
        return 1;
    }

    public int enqueue(int pos, String configPath) {
        return enqueue(pos, configPath, null);
    }

    public int enqueue(int pos, String configPath, Long seed) {
        TraderQueueEntry entry = newEntry(configPath, seed);

        if (entry == null) {
            return -1;
        }

        queue.add(pos, entry);
        return 1;
    }

    public int clearQueue() {
        queue.clear();
        return 1;
    }
}
