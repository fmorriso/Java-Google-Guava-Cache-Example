import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            // Load cache from file if exists
            GuavaCachePersistence.loadCacheFromFile();

            // Add some data to cache
            GuavaCachePersistence.cache.put(1, "Apple");
            GuavaCachePersistence.cache.put(2, "Banana");
            GuavaCachePersistence.cache.put(3, "Cherry");

            // Print cache before saving
            System.out.println("Cache before saving: " + GuavaCachePersistence.cache.asMap());

            // Save cache to file
            GuavaCachePersistence.saveCacheToFile();

            // Clear and reload cache from file
            GuavaCachePersistence.cache.invalidateAll();
            GuavaCachePersistence.loadCacheFromFile();

            // Print cache after loading
            System.out.println("Cache after reloading: " + GuavaCachePersistence.cache.asMap());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}