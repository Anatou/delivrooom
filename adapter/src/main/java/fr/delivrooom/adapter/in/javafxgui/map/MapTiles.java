package fr.delivrooom.adapter.in.javafxgui.map;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MapTiles extends Canvas {

    private static final double TILE_SIZE_PX = 256.0;
    private final double ZOOM_SCALE_FACTOR; // tunable factor between canvas scale and tile zoom level

    private static final String MAPTILER_API_KEY = JavaFXApp.getConfigPropertyUseCase().getProperty("maptiler.api.key");
    private static final String MAPTILER_URL = JavaFXApp.getConfigPropertyUseCase().getProperty("maptiler.url", "https://api.maptiler.com/tiles/satellite-v2/");

    private final Map<String, Image> tileCache = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<Image>> pendingTiles = new ConcurrentHashMap<>();
    private final List<String> memeFiles = new ArrayList<>();
    private final Random random = new Random();

    public MapTiles() {
        ZOOM_SCALE_FACTOR = JavaFXApp.getConfigPropertyUseCase().getDoubleProperty("map.zoom.scaleFactor", 1.0);
        loadMemeFiles();

        AppController.getController().memeModeProperty().addListener((obs, oldVal, newVal) -> {
            tileCache.clear();
        });
    }

    /**
     * Load the list of meme files from resources/assets/memes
     */
    private void loadMemeFiles() {
        int count = 0;
        while (true) {
            if (memeExists(count + ".png")) {
                memeFiles.add(count + ".png");
            } else if (memeExists(count + ".jpg")) {
                memeFiles.add(count + ".jpg");
            } else if (memeExists(count + ".jpeg")) {
                memeFiles.add(count + ".jpeg");
            } else {
                break;
            }
            count++;
        }
    }

    private boolean memeExists(String name) {
        return JavaFXApp.class.getResource("/assets/memes/" + name) != null;
    }

    /**
     * Clear the tile cache and cancel pending requests
     */
    public void clearCache() {
        tileCache.clear();
        for (CompletableFuture<Image> future : pendingTiles.values()) {
            future.cancel(true);
        }
        pendingTiles.clear();
        System.out.println("Tile cache cleared");
    }

    public void clear() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
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
     * Get or download a MapTiler tile with a callback
     *
     * @param tileKey       the tile key got from getTileKey()
     * @param onImageLoaded callback called when the tile is loaded
     * @return the tile image if it is in the cache. Otherwise null, and will call onImageLoaded upon completion.
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
                // Check if meme mode is enabled
                if (AppController.getController().memeModeProperty().get()) {
                    String memePath = "/assets/memes/" + memeFiles.get(random.nextInt(memeFiles.size()));
                    InputStream memeStream = JavaFXApp.class.getResourceAsStream(memePath);
                    if (memeStream != null) {
                        Image image = new Image(memeStream);
                        if (!image.isError()) {
                            return image;
                        }
                    }
                }

                // Normal map tile loading
                URL url = MapTiles.class.getResource("/tiles-cache/" + tileKey.replace("/", "_") + ".jpg");
                if (url == null) {
                    String urlStr = MAPTILER_URL + tileKey + ".jpg?key=" + MAPTILER_API_KEY;
                    url = new URI(urlStr).toURL();
                    System.out.println("Downloading tile " + tileKey);
                } else {
                    System.out.println("Loading tile " + tileKey + " from file cache");
                }

                // Load image in a background thread
                Image image = new Image(url.toString());

                if (image.isError()) {
                    throw new IOException("Failed to load image");
                }

                return image;
            } catch (IOException | URISyntaxException e) {
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


    private int computeZoomLevel(double scale) {
        double z = Math.log(scale / (TILE_SIZE_PX * ZOOM_SCALE_FACTOR)) / Math.log(2);
        int zi = (int) Math.round(z);
        if (zi < 0) zi = 0;
        if (zi > 22) zi = 22;
        return zi;
    }

    public void drawTiles(double scale, double minX, double maxX, double minY, double maxY) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        int zoomLevel = computeZoomLevel(scale);
        int tilesPerSide = (int) Math.pow(2, zoomLevel);

        int minTileX = (int) Math.floor(minX * tilesPerSide);
        int maxTileX = (int) Math.floor(maxX * tilesPerSide);
        int minTileY = (int) Math.floor(minY * tilesPerSide);
        int maxTileY = (int) Math.floor(maxY * tilesPerSide);

        // Clamp to valid tile index range
        int startX = Math.max(0, minTileX);
        int endX = Math.min(tilesPerSide - 1, maxTileX);
        int startY = Math.max(0, minTileY);
        int endY = Math.min(tilesPerSide - 1, maxTileY);

        ArrayList<String> requestedTiles = new ArrayList<>();

        for (int tileX = startX; tileX <= endX; tileX++) {
            for (int tileY = startY; tileY <= endY; tileY++) {
                String key = getTileKey(zoomLevel, tileX, tileY);
                requestedTiles.add(key);
                Image image = getTile(key, () -> drawTiles(scale, minX, maxX, minY, maxY));
                if (image != null) {
                    // Normalized tile coordinates
                    double tileLeft = (double) tileX / tilesPerSide;
                    double tileTop = (double) tileY / tilesPerSide;
                    double tileRight = (double) (tileX + 1) / tilesPerSide;
                    double tileBottom = (double) (tileY + 1) / tilesPerSide;
                    // Calculate tile origin and size on canvas
                    double x1 = (tileLeft - minX) * scale;
                    double y1 = (tileTop - minY) * scale;
                    double w = (tileRight - tileLeft) * scale;
                    double h = (tileBottom - tileTop) * scale;
                    // Draw the loaded tile
                    gc.drawImage(image, x1, y1, w, h);
                }
            }
        }
        cancelTilesRequestsNotIn(requestedTiles);
    }
}
