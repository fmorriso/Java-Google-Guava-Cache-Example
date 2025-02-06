import com.google.common.cache.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class GuavaCachePersistence {
    public static final String FILE_PATH = ".env"; // similar to how Python prefers to keep secrets

    /** Create a Guava Cache
     * @implNote Assumes both key and value are strings.
     */
    public static final LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(5)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) {
                    return key.toUpperCase();
                }
            });


    /** Read key/value string values from a text file in the file system.
     * @implNote The file is expected to have each key value pair separated by an = (equal) sign.
     * @throws IOException if the file cannot be found or accessed.
     */
    public static void loadCacheFromFile() throws IOException {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim().toUpperCase();
                    String value = parts[1].trim();
                    GuavaCachePersistence.cache.put(key, value);
                }
            }
        }
        System.out.println("Cache loaded from file.");
    }

    /** Save cache contents to a text file
     * @throws IOException if the file cannot be created or written.
     */
    public static void saveCacheToFile() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            // iterate over the key/value pairs in the cache
            for (var entry : cache.asMap().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        }
        System.out.println("Cache saved to file.");
    }

    public static void displayCacheContents(String message) {
        System.out.format("%s %s%n", message, GuavaCachePersistence.cache.asMap());
    }

}
