package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.application.model.*;
import javafx.beans.InvalidationListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MapCanvas extends Canvas {

    private static final String MAPTILER_API_KEY = JavaFXApp.getConfigPropertyUseCase().getProperty("maptiler.api.key");
    private static final String MAPTILER_URL = JavaFXApp.getConfigPropertyUseCase().getProperty("maptiler.url", "https://api.maptiler.com/tiles/satellite-v2/");
    private static final int ZOOM_LEVEL = JavaFXApp.getConfigPropertyUseCase().getIntProperty("maptiler.zoom.level", 14);

    private final Map<String, Image> tileCache = new HashMap<>();

    public MapCanvas() {
        setupCanvas();
    }

    private void setupCanvas() {
        InvalidationListener canvasResizeListener = (o) -> {
            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }
            String XML_map = "petitPlan";
            String XML_deliveries = "demandePetit1";

            CityMap cityMap = JavaFXApp.guiUseCase().getCityMap(XML_map);
            DeliveriesDemand deliveriesDemand = JavaFXApp.guiUseCase().getDeliveriesDemand(cityMap, XML_deliveries);
            drawMap(getGraphicsContext2D(), getWidth(), getHeight(), cityMap, deliveriesDemand);
        };
        widthProperty().addListener(canvasResizeListener);
        heightProperty().addListener(canvasResizeListener);
    }


    /**
     * Get or download a MapTiler tile
     */
    private Image getTile(int z, int x, int y) {
        String tileKey = z + "/" + x + "/" + y;

        if (tileCache.containsKey(tileKey)) {
            return tileCache.get(tileKey);
        }

        try {
            String urlStr = MAPTILER_URL + z + "/" + x + "/" + y + ".jpg?key=" + MAPTILER_API_KEY;
            System.out.println("Downloading tile " + x + "/" + y + " zoom " + z);
            URL url = new URL(urlStr);
            Image image = new Image(url.toString());
            tileCache.put(tileKey, image);
            return image;
        } catch (IOException e) {
            System.err.println("Failed to load tile " + tileKey + ": " + e.getMessage());
            return null;
        }
    }

    private void drawMap(GraphicsContext gc, double width, double height, CityMap cityMap, DeliveriesDemand deliveriesDemand ) {
        double padding = 30e-7; // Paddings depend on the map scale
        double minX = cityMap.getIntersections().stream().mapToDouble(Intersection::getNormalizedX).min().orElse(0) - padding;
        double maxX = cityMap.getIntersections().stream().mapToDouble(Intersection::getNormalizedX).max().orElse(1) + padding;
        double minY = cityMap.getIntersections().stream().mapToDouble(Intersection::getNormalizedY).min().orElse(0) - padding;
        double maxY = cityMap.getIntersections().stream().mapToDouble(Intersection::getNormalizedY).max().orElse(1) + padding;

        // Calculate scale factor between normalized coordinates and canvas coordinates
        double scale = Math.min(width / (maxX - minX), height / (maxY - minY));
        double road_width = 2e-7 * scale; // Road width depends on the map scale

        // Center the map on the canvas by editing the min/max coordinates on the non-restricting axis
        if (width / scale > maxX - minX) {
            double extraX = (width / scale - (maxX - minX)) / 2;
            minX -= extraX;
            maxX += extraX;
        } else if (height / scale > maxY - minY) {
            double extraY = (height / scale - (maxY - minY)) / 2;
            minY -= extraY;
            maxY += extraY;
        }

        // Clear canvas
        gc.clearRect(0, 0, width, height);

        // Draw tiles
        drawSatelliteTiles(gc, scale, minX, maxX, minY, maxY);

        // Draw roads
        gc.setStroke(Color.rgb(220, 220, 220));
        gc.setLineWidth(road_width);
        for (Road road : cityMap.getRoads()) {
            drawRoad(gc, scale, minX, minY, road);
        }

        // Draw intersections
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.rgb(220, 220, 220));
        for (Intersection intersection : cityMap.getIntersections()) {
            drawIntersection(gc, scale, minX, minY, intersection, 1.3 * road_width);
        }

        // Draw deliveries points in red
        for (Delivery delivery : deliveriesDemand.getDeliveries()) {

            // takeout point in red, delivery point in blue
            gc.setFill(Color.RED);
            drawIntersection(gc, scale, minX, minY, delivery.getTakeoutIntersection(), 2 * road_width); // Draw takeout point in red
            gc.setFill(Color.BLUE);
            drawIntersection(gc, scale, minX, minY, delivery.getDeliveryIntersection(), 2 * road_width); // Draw delivery point in blue
        }
        // Draw warehouse point in green
        gc.setFill(Color.GREEN);
        System.out.println("point de livraison : " + deliveriesDemand.getStore().getId());
        drawIntersection(gc, scale, minX, minY, deliveriesDemand.getStore(), 2 * road_width);
    }

    private void drawSatelliteTiles(GraphicsContext gc, double scale, double minX, double maxX, double minY, double maxY) {
        int zoomLevel = ZOOM_LEVEL;
        int tilesPerSide = (int) Math.pow(2, zoomLevel);

        int minTileX = (int) Math.floor(minX * tilesPerSide);
        int maxTileX = (int) Math.floor(maxX * tilesPerSide);
        int minTileY = (int) Math.floor(minY * tilesPerSide);
        int maxTileY = (int) Math.floor(maxY * tilesPerSide);

        for (int tileX = minTileX; tileX <= maxTileX; tileX++) {
            for (int tileY = minTileY; tileY <= maxTileY; tileY++) {
                Image tile = getTile(zoomLevel, tileX, tileY);
                if (tile != null) {
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

                    gc.drawImage(tile, x1, y1, w, h);
                }
            }
        }
    }

    private void drawIntersection(GraphicsContext gc, double scale, double minX, double minY, Intersection intersection, double radius) {
        double x = (intersection.getNormalizedX() - minX) * scale;
        double y = (intersection.getNormalizedY() - minY) * scale;

        gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    private void drawRoad(GraphicsContext gc, double scale, double minX, double minY, Road road) {
        Intersection origin = road.getOrigin();
        Intersection destin = road.getDestination();

        double x1 = (origin.getNormalizedX() - minX) * scale;
        double y1 = (origin.getNormalizedY() - minY) * scale;
        double x2 = (destin.getNormalizedX() - minX) * scale;
        double y2 = (destin.getNormalizedY() - minY) * scale;

        gc.strokeLine(x1, y1, x2, y2);
    }

}
