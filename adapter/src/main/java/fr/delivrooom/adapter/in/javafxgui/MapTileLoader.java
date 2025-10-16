package fr.delivrooom.adapter.in.javafxgui;

import javafx.application.Platform;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MapTileLoader {

    private static final String MAPTILER_API_KEY = JavaFXApp.getConfigPropertyUseCase().getProperty("maptiler.api.key");
    private static final String MAPTILER_URL = JavaFXApp.getConfigPropertyUseCase().getProperty("maptiler.url", "https://api.maptiler.com/tiles/satellite-v2/");

    private final Map<String, Image> tileCache = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<Image>> pendingTiles = new ConcurrentHashMap<>();

    public MapTileLoader() {
        System.out.println("Loaded MapTileLoader with maptiler url: " + MAPTILER_URL);
    }


    /**
     * Get the tile unique identifier (key) used in the cache
     *
     * @param z zoom level
     * @param x tile x coordinate
     * @param y tile y coordinate
     * @return the tile key
     */
    public String getTileKey(int z, int x, int y) {
        return z + "/" + x + "/" + y;
    }

    /**
     * Get or download a MapTiler tile with callback
     *
     * @param tileKey the tile key got from getTileKey()
     * @param onImageLoaded callback called when the tile is loaded
     * @return the tile image if it is in cache. Otherwise null, and will call onImageLoaded upon completion.
     */
    public Image getTile(String tileKey, Runnable onImageLoaded) {
        // Return the cached tile if available
        if (tileCache.containsKey(tileKey)) {
            return tileCache.get(tileKey);
        }
        // Start async loading with a callback
        if (!pendingTiles.containsKey(tileKey)) {
            loadTileAsync(tileKey, onImageLoaded);
        }
        return null;
    }
    /**
     * Load a tile asynchronously
     */
    private void loadTileAsync(String tileKey, Runnable onLoad) {
        CompletableFuture<Image> future = CompletableFuture.supplyAsync(() -> {
            try {
                String urlStr = MAPTILER_URL + tileKey + ".jpg?key=" + MAPTILER_API_KEY;
                URL url = new URL(urlStr);
                System.out.println("Downloading tile " + tileKey);

                // Load image in a background thread
                Image image = new Image(url.toString());

                if (image.isError()) {
                    throw new IOException("Failed to load image");
                }

                return image;
            } catch (IOException e) {
                System.err.println("Failed to load tile " + tileKey + ": " + e.getMessage());
                return null;
            }
        });


        pendingTiles.put(tileKey, future);
        future.thenAccept(image -> {
            pendingTiles.remove(tileKey);
            if (image != null) {
                tileCache.put(tileKey, image);
                Platform.runLater(onLoad);
            }
        });
    }

    /*
     * Cancel all pending requests for tiles not in the given list
     */
    public void cancelTilesRequestsNotIn(ArrayList<String> requestedTiles) {
        for (String tileKey : new ArrayList<>(pendingTiles.keySet())) {
            if (!requestedTiles.contains(tileKey)) {
                System.out.println("Cancelling tile request " + tileKey);
                pendingTiles.get(tileKey).cancel(true);
                pendingTiles.remove(tileKey);
            }
        }
    }
}
