package fr.delivrooom.adapter.in.javafxgui;

import javafx.application.Platform;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class MapTileLoader {

    private static final String MAPTILER_API_KEY = JavaFXApp.getConfigPropertyUseCase().getProperty("maptiler.api.key");
    private static final String MAPTILER_URL = JavaFXApp.getConfigPropertyUseCase().getProperty("maptiler.url", "https://api.maptiler.com/tiles/satellite-v2/");

    private final Map<String, Image> tileCache = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<Image>> pendingTiles = new ConcurrentHashMap<>();

    public MapTileLoader() {
    }

    /**
     * Get or download a MapTiler tile with callback
     *
     * @param z      zoom level
     * @param x      tile x coordinate
     * @param y      tile y coordinate
     * @param onLoad callback to execute when tile is loaded (called on JavaFX thread)
     * @return the tile key (z/x/y)
     */
    public String getTile(int z, int x, int y, Consumer<Image> onLoad) {
        String tileKey = z + "/" + x + "/" + y;
        // Return the cached tile if available
        if (tileCache.containsKey(tileKey)) {
            onLoad.accept(tileCache.get(tileKey));
        }
        // If the tile is already being loaded, return null
        if (pendingTiles.containsKey(tileKey)) {
            return tileKey;
        }
        // Start async loading with a callback
        loadTileAsync(tileKey, onLoad);
        return tileKey;
    }

    /**
     * Load a tile asynchronously
     */
    private void loadTileAsync(String tileKey, Consumer<Image> onLoad) {
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
            System.out.println("Tile " + tileKey + " loaded");
            pendingTiles.remove(tileKey);
            if (image != null) {
                tileCache.put(tileKey, image);
                Platform.runLater(() -> onLoad.accept(image));
            }
        });
    }

    /*
     * Cancel all pending requests for tiles not in the given list
     */
    public void cancelTilesRequestsNotIn(ArrayList<String> requestedTiles) {
        for (String tileKey : new ArrayList<>(pendingTiles.keySet())) {
            if (!requestedTiles.contains(tileKey)) {
                System.out.println("Cancelling tile " + tileKey);
                pendingTiles.get(tileKey).cancel(true);
                pendingTiles.remove(tileKey);
            }
        }
    }
}
