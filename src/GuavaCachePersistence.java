import com.google.common.cache.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class GuavaCachePersistence {
    public static final String FILE_PATH = "cache_data.txt";

    // Create a Guava Cache
    public static final LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
            .maximumSize(5)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, String>() {
                @Override
                public String load(Integer key) {
                    return "Value_" + key; // Default value when key is missing
                }
            });


    // Load cache contents from a text file
    public static void loadCacheFromFile() throws IOException {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    Integer key = Integer.parseInt(parts[0]);
                    String value = parts[1];
                    GuavaCachePersistence.cache.put(key, value);
                }
            }
        }
        System.out.println("Cache loaded from file.");
    }
    // Save cache contents to a text file
    public static void saveCacheToFile() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (var entry : cache.asMap().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        }
        System.out.println("Cache saved to file.");
    }

}
