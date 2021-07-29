package net.hyper_pigeon.better_wandering_trader.config;

import java.nio.file.Path;
import java.util.Optional;

public class TradeListFileUtils {
    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public static boolean isJsonFile(Path path) {
        Optional<String> result = getFileExtension(path.toString());
        return result.isPresent() && result.get() == "json";
    }
}
