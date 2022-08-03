package work.lclpnet.mmocontent.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ConfigHelper {

    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> CompletableFuture<T> load(Path path, Class<T> type, Supplier<T> defaultConfig) {
        return CompletableFuture.supplyAsync(() -> {
            if (!Files.exists(path)) {
                T config = defaultConfig.get(); // default config
                save(path, config); // do not chain save, this can be done separately
                return config;
            }

            T config;
            try (JsonReader reader = new JsonReader(new FileReader(path.toFile()))) {
                config = gson.fromJson(reader, type);
                if (config == null) config = defaultConfig.get(); // default config
            } catch (Exception e) {
                logger.error("Could not load path", e);
                config = defaultConfig.get(); // default config
            }

            return config;
        }, Util.getIoWorkerExecutor());
    }

    public static <T> CompletableFuture<Void> save(Path path, T config) {
        return CompletableFuture.runAsync(() -> {
            if (config == null) throw new IllegalStateException("Tried to save null config");

            final Path dir = path.getParent();
            if (!Files.exists(dir)) {
                try {
                    Files.createDirectories(dir);
                } catch (IOException e) {
                    logger.error("Could not create config directory.");
                    return;
                }
            }

            try (JsonWriter writer = gson.newJsonWriter(new FileWriter(path.toFile()))) {
                JsonElement json = gson.toJsonTree(config);
                gson.toJson(json, writer);
            } catch (Exception e) {
                logger.error("Could not write config file", e);
            }
        }, Util.getIoWorkerExecutor());
    }

    public static void dispatchHandledSave(Path path, Object obj) {
        save(path, obj).exceptionally(ex -> {
            logger.error("Failed to save config", ex);
            return null;
        });
    }
}
