package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.application.model.*;
import javafx.beans.InvalidationListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapCanvas extends StackPane {

    private static final int ZOOM_LEVEL = JavaFXApp.getConfigPropertyUseCase().getIntProperty("maptiler.zoom.level", 14);
    private final MapTileLoader tileLoader;
    private final Canvas tileCanvas = new Canvas();
    private final Canvas overlayCanvas = new Canvas();

    private CityMap currentCityMap;
    private DeliveriesDemand currentDeliveriesDemand;
    private TourCalculator tourCalculator;

    public MapCanvas() {
        tileLoader = new MapTileLoader();

        setStyle("-fx-background-color: #8D8E7F;");
        setMinSize(0, 0);

        getChildren().addAll(tileCanvas, overlayCanvas);
        setupCanvas();
    }

    private void setupCanvas() {
        InvalidationListener canvasResizeListener = (o) -> {
            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }

            tileCanvas.setWidth(getWidth());
            tileCanvas.setHeight(getHeight());
            overlayCanvas.setWidth(getWidth());
            overlayCanvas.setHeight(getHeight());

            if (currentCityMap != null) {
                drawMap(getWidth(), getHeight(), currentCityMap, currentDeliveriesDemand);
            }
        };
        widthProperty().addListener(canvasResizeListener);
        heightProperty().addListener(canvasResizeListener);
    }

    /**
     * Update the map with new data from the controller.
     *
     * @param cityMap          The city map to display (required)
     * @param deliveriesDemand The deliveries to display (optional, can be null)
     */
    public void updateMap(CityMap cityMap, DeliveriesDemand deliveriesDemand) {
        this.currentCityMap = cityMap;
        this.currentDeliveriesDemand = deliveriesDemand;

        // Recalculate the tour when this method is called since it means the data has changed
        CityGraph cityGraph = new CityGraph(cityMap);
        // TODO : use a more advanced TSP solver
        tourCalculator = new TemplateTourCalculator(cityGraph);

        if (tourCalculator.doesCalculatedTourNeedsToBeChanged(deliveriesDemand)) {
            tourCalculator.findOptimalTour(deliveriesDemand);
        }



        if (getWidth() > 0 && getHeight() > 0) {
            drawMap(getWidth(), getHeight(), cityMap, deliveriesDemand);
        }
    }


    private void drawMap(double width, double height, CityMap cityMap, DeliveriesDemand deliveriesDemand) {
        double padding = 30e-7; // Paddings depend on the map scale
        double minX = cityMap.getIntersections().values().stream().mapToDouble(Intersection::getNormalizedX).min().orElse(0) - padding;
        double maxX = cityMap.getIntersections().values().stream().mapToDouble(Intersection::getNormalizedX).max().orElse(1) + padding;
        double minY = cityMap.getIntersections().values().stream().mapToDouble(Intersection::getNormalizedY).min().orElse(0) - padding;
        double maxY = cityMap.getIntersections().values().stream().mapToDouble(Intersection::getNormalizedY).max().orElse(1) + padding;

        // Calculate scale factor between normalized coordinates and canvas coordinates
        double scale = Math.min(width / (maxX - minX), height / (maxY - minY));

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

        GraphicsContext gcTiles = tileCanvas.getGraphicsContext2D();
        GraphicsContext gcOverlay = overlayCanvas.getGraphicsContext2D();

        drawTiles(gcTiles, scale, minX, maxX, minY, maxY);
        drawOverlay(gcOverlay, width, height, cityMap, deliveriesDemand, scale, minX, minY);
    }

    private void drawTiles(GraphicsContext gc, double scale, double minX, double maxX, double minY, double maxY) {
        int zoomLevel = ZOOM_LEVEL;
        int tilesPerSide = (int) Math.pow(2, zoomLevel);

        int minTileX = (int) Math.floor(minX * tilesPerSide);
        int maxTileX = (int) Math.floor(maxX * tilesPerSide);
        int minTileY = (int) Math.floor(minY * tilesPerSide);
        int maxTileY = (int) Math.floor(maxY * tilesPerSide);

        ArrayList<String> requestedTiles = new ArrayList<>();

        for (int tileX = minTileX; tileX <= maxTileX; tileX++) {
            for (int tileY = minTileY; tileY <= maxTileY; tileY++) {
                final int finalTileX = tileX;
                final int finalTileY = tileY;

                // Get tile with callback to draw it when loaded
                String key = tileLoader.getTile(zoomLevel, tileX, tileY, loadedImage -> {
                    // Normalized tile coordinates
                    double tileLeft = (double) finalTileX / tilesPerSide;
                    double tileTop = (double) finalTileY / tilesPerSide;
                    double tileRight = (double) (finalTileX + 1) / tilesPerSide;
                    double tileBottom = (double) (finalTileY + 1) / tilesPerSide;

                    // Calculate tile origin and size on canvas
                    double x1 = (tileLeft - minX) * scale;
                    double y1 = (tileTop - minY) * scale;
                    double w = (tileRight - tileLeft) * scale;
                    double h = (tileBottom - tileTop) * scale;

                    // Draw the loaded tile
                    gc.drawImage(loadedImage, x1, y1, w, h);
                });
                requestedTiles.add(key);
            }
        }
        tileLoader.cancelTilesRequestsNotIn(requestedTiles);
    }

    private void drawOverlay(GraphicsContext gc, double width, double height, CityMap cityMap, DeliveriesDemand deliveriesDemand, double scale, double minX, double minY) {
        double road_width = 2e-7 * scale; // Road width depends on the map scale

        // Clear canvas
        gc.clearRect(0, 0, width, height);
        // Draw roads
        gc.setStroke(Color.rgb(220, 220, 220));
        gc.setLineWidth(road_width);
        for (HashMap<Long, Road> subMap : cityMap.getRoads().values()) {
            for (Road road : subMap.values()) {
                drawRoad(gc, scale, minX, minY, road);
            }
        }
        // Draw intersections
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.rgb(220, 220, 220));
        for (Intersection intersection : cityMap.getIntersections().values()) {
            drawIntersection(gc, scale, minX, minY, intersection, 1.3 * road_width, true);
        }

        // Only draw deliveries if they are loaded
        if (deliveriesDemand != null) {
            // Takeout point is a red square, delivery point in blue circle
            for (Delivery delivery : deliveriesDemand.getDeliveries()) {
                gc.setFill(Color.RED);
                drawIntersection(gc, scale, minX, minY, delivery.getTakeoutIntersection(), 3 * road_width, false); // Draw takeout point in red
                gc.setFill(Color.BLUE);
                drawIntersection(gc, scale, minX, minY, delivery.getDeliveryIntersection(), 3 * road_width, true); // Draw delivery point in blue
            }
            // Draw warehouse point in green
            gc.setFill(Color.GREEN);
            drawIntersection(gc, scale, minX, minY, deliveriesDemand.getStore(), 5 * road_width, true);
        }
        // Draw calculated tour if available
        if (tourCalculator != null && tourCalculator.getOptimalTour() != null) {
            gc.setStroke(Color.LIMEGREEN);
            gc.setLineWidth(2 * road_width);
            TourSolution tourSolution = tourCalculator.getOptimalTour();
            for (Path path : tourSolution.getPaths()) {
                List<Road> intersections = path.getIntersections();
                for (Road road : intersections) {
                    drawRoad(gc, scale, minX, minY, road);
                }
            }
        }
    }

    private void drawIntersection(GraphicsContext gc, double scale, double minX, double minY, Intersection intersection, double radius, boolean circle) {
        double x = (intersection.getNormalizedX() - minX) * scale;
        double y = (intersection.getNormalizedY() - minY) * scale;

        if (circle)
            gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        else
            gc.fillRect(x - radius, y - radius, 2 * radius, 2 * radius);
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
