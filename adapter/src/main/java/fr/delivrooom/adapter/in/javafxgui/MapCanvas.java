package fr.delivrooom.adapter.in.javafxgui;

import atlantafx.base.controls.Popover;
import fr.delivrooom.application.model.*;
import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.PopOver;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.HashMap;

public class MapCanvas extends StackPane {
    private final MapTileLoader tileLoader;
    private final Canvas tileCanvas = new Canvas();
    private final Canvas overlayCanvas = new Canvas();
    private final Pane interactiveLayer = new Pane();
    private final Pane controlsPane = new Pane();

    private CityMap currentCityMap;
    private DeliveriesDemand currentDeliveriesDemand;

    private static final double TILE_SIZE_PX = 256.0;
    private static final double ZOOM_STEP = 1.5;
    // Zoom config
    private final double ZOOM_SCALE_FACTOR; // tunable factor between canvas scale and tile zoom level
    // View state
    private boolean autoFraming = true;
    private double manualScale = 1.0;      // pixels per normalized unit
    private double manualMinX = 0.0;       // normalized coord of left
    private double manualMinY = 0.0;       // normalized coord of top
    private double lastMouseX;
    private double lastMouseY;
    private boolean dragging = false;

    public MapCanvas() {
        tileLoader = new MapTileLoader();
        ZOOM_SCALE_FACTOR = JavaFXApp.getConfigPropertyUseCase().getDoubleProperty("map.zoom.scaleFactor", 1.0);

        setStyle("-fx-background-color: #8D8E7F;");
        setMinSize(0, 0);

        getChildren().addAll(tileCanvas, overlayCanvas, interactiveLayer, controlsPane);
        setupCanvas();
        setupControls();
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
            drawMap();
        };
        widthProperty().addListener(canvasResizeListener);
        heightProperty().addListener(canvasResizeListener);
    }

    private void setupControls() {
        // Controls not visible by default
        controlsPane.setVisible(false);

        // Controls
        Button zoomInBtn = new Button("", new FontIcon(FontAwesomeSolid.PLUS));
        Button zoomOutBtn = new Button("", new FontIcon(FontAwesomeSolid.MINUS));
        Button resetBtn = new Button("", new FontIcon(FontAwesomeSolid.EXPAND));
        zoomInBtn.setPadding(new Insets(0));
        zoomInBtn.setMinSize(30, 30);
        zoomOutBtn.setPadding(new Insets(0));
        zoomOutBtn.setMinSize(30, 30);
        resetBtn.setPadding(new Insets(0));
        resetBtn.setMinSize(30, 30);


        zoomInBtn.setOnAction(e -> {
            if (currentCityMap == null) return;
            ensureManualFromAuto();
            zoomAt(getWidth() / 2.0, getHeight() / 2.0, ZOOM_STEP);
        });
        zoomOutBtn.setOnAction(e -> {
            if (currentCityMap == null) return;
            ensureManualFromAuto();
            zoomAt(getWidth() / 2.0, getHeight() / 2.0, 1.0 / ZOOM_STEP);
        });
        resetBtn.setOnAction(e -> {
            autoFraming = true;
            redraw();
        });

        VBox controls = new VBox(5, zoomInBtn, zoomOutBtn, resetBtn);

        StackPane.setAlignment(controlsPane, Pos.TOP_RIGHT);
        StackPane.setMargin(controlsPane, new Insets(10));
        controlsPane.setMaxSize(30, 0);
        controlsPane.getChildren().setAll(controls);

        // Mouse interactions
        addEventHandler(ScrollEvent.SCROLL, e -> {
            if (currentCityMap == null) return;
            ensureManualFromAuto();
            double factor = e.getDeltaY() > 0 ? Math.pow(ZOOM_STEP, 0.15) : 1.0 / Math.pow(ZOOM_STEP, 0.15);
            zoomAt(e.getX(), e.getY(), factor);
            e.consume();
        });

        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.isPrimaryButtonDown()) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                dragging = true;
            }
        });

        addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!dragging) return;
            if (currentCityMap == null) return;
            ensureManualFromAuto();
            double dx = e.getX() - lastMouseX;
            double dy = e.getY() - lastMouseY;
            panByPixels(dx, dy);
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            e.consume();
        });

        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> dragging = false);
    }

    private void ensureManualFromAuto() {
        if (!autoFraming) return;
        if (currentCityMap == null || getWidth() == 0 || getHeight() == 0) return;
        AutoView v = computeAutoView(getWidth(), getHeight(), currentCityMap);
        manualScale = v.scale;
        manualMinX = v.minX;
        manualMinY = v.minY;
        autoFraming = false;
    }

    private void panByPixels(double dx, double dy) {
        // Move the view so the content follows the cursor
        manualMinX -= dx / manualScale;
        manualMinY -= dy / manualScale;
        redraw();
    }

    private void zoomAt(double canvasX, double canvasY, double factor) {
        double oldScale = manualScale;
        double newScale = oldScale * factor;
        if (newScale < 1e-12) newScale = 1e-12;

        // Keep the world point under the cursor fixed
        double worldX = manualMinX + canvasX / oldScale;
        double worldY = manualMinY + canvasY / oldScale;

        manualScale = newScale;
        manualMinX = worldX - canvasX / newScale;
        manualMinY = worldY - canvasY / newScale;

        redraw();
    }

    private void redraw() {
        if (getWidth() > 0 && getHeight() > 0) {
            drawMap();
        }
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

        autoFraming = true;
        if (getWidth() > 0 && getHeight() > 0) {
            drawMap();
        }
    }

    private AutoView computeAutoView(double width, double height, CityMap cityMap) {
        double padding = 30e-7; // Paddings depend on the map scale
        double minX = cityMap.getIntersections().values().stream().mapToDouble(Intersection::getNormalizedX).min().orElse(0) - padding;
        double maxX = cityMap.getIntersections().values().stream().mapToDouble(Intersection::getNormalizedX).max().orElse(1) + padding;
        double minY = cityMap.getIntersections().values().stream().mapToDouble(Intersection::getNormalizedY).min().orElse(0) - padding;
        double maxY = cityMap.getIntersections().values().stream().mapToDouble(Intersection::getNormalizedY).max().orElse(1) + padding;

        // Calculate scale factor between normalized coordinates and canvas coordinates
        double scale;
        double scaleByWidth = width / (maxX - minX);
        double scaleByHeight = height / (maxY - minY);

        // Center the map on the canvas by editing the min/max coordinates on the non-restricting axis
        if (scaleByHeight < scaleByWidth) {
            scale = scaleByHeight;
            double extraX = (width / scale - (maxX - minX)) / 2;
            minX -= extraX;
            maxX += extraX;
        } else {
            scale = scaleByWidth;
            double extraY = (height / scale - (maxY - minY)) / 2;
            minY -= extraY;
            maxY += extraY;
        }
        AutoView v = new AutoView();
        v.minX = minX;
        v.minY = minY;
        v.maxX = maxX;
        v.maxY = maxY;
        v.scale = scale;
        return v;
    }

    private void drawMap() {
        GraphicsContext gcTiles = tileCanvas.getGraphicsContext2D();
        GraphicsContext gcOverlay = overlayCanvas.getGraphicsContext2D();

        // Clear canvases
        gcTiles.clearRect(0, 0, getWidth(), getHeight());
        gcOverlay.clearRect(0, 0, getWidth(), getHeight());
        interactiveLayer.getChildren().clear();
        controlsPane.setVisible(currentCityMap != null);

        // Return if no city map is loaded
        if (currentCityMap == null) return;

        // Compute visible area
        double minX;
        double minY;
        double maxX;
        double maxY;
        double scale;
        if (autoFraming) {
            AutoView v = computeAutoView(getWidth(), getHeight(), currentCityMap);
            minX = v.minX;
            minY = v.minY;
            maxX = v.maxX;
            maxY = v.maxY;
            scale = v.scale;
        } else {
            scale = manualScale;
            minX = manualMinX;
            minY = manualMinY;
            maxX = minX + getWidth() / scale;
            maxY = minY + getHeight() / scale;
        }

        // Update tiles and overlay
        drawOverlay(gcOverlay, scale, minX, minY);
        updateInteractiveLayer(scale, minX, minY);
        drawTiles(gcTiles, scale, minX, maxX, minY, maxY);
    }

    private int computeZoomLevel(double scale) {
        double z = Math.log(scale / (TILE_SIZE_PX * ZOOM_SCALE_FACTOR)) / Math.log(2);
        int zi = (int) Math.round(z);
        if (zi < 0) zi = 0;
        if (zi > 22) zi = 22;
        return zi;
    }

    private void drawTiles(GraphicsContext gc, double scale, double minX, double maxX, double minY, double maxY) {
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
                String key = tileLoader.getTileKey(zoomLevel, tileX, tileY);
                requestedTiles.add(key);
                Image image = tileLoader.getTile(key, () -> {
                    drawTiles(gc, scale, minX, maxX, minY, maxY);
                });
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
        tileLoader.cancelTilesRequestsNotIn(requestedTiles);
    }

    private void drawOverlay(GraphicsContext gc, double scale, double minX, double minY) {
        double road_width = 2e-7 * scale; // Road width depends on the map scale

        // Draw roads
        gc.setStroke(Color.rgb(220, 220, 220));
        gc.setLineWidth(road_width);
        for (HashMap<Long, Road> subMap : currentCityMap.getRoads().values()) {
            for (Road road : subMap.values()) {
                drawRoad(gc, scale, minX, minY, road);
            }
        }
        // Draw intersections
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.rgb(220, 220, 220));
        for (Intersection intersection : currentCityMap.getIntersections().values()) {
            drawIntersection(gc, scale, minX, minY, intersection, 1.3 * road_width, true);
        }

        // Draw deliveries demands
        if (currentDeliveriesDemand != null) {

            // Draw warehouse point in green
            gc.setFill(Color.GREEN);
            drawIntersection(gc, scale, minX, minY, currentDeliveriesDemand.getStore(), 4 * road_width, true);

            // Takeout point is a red square, delivery point in blue circle
            for (Delivery delivery : currentDeliveriesDemand.getDeliveries()) {
                gc.setFill(Color.RED);
                drawIntersection(gc, scale, minX, minY, delivery.getTakeoutIntersection(), 4 * road_width, false);
                gc.setFill(Color.BLUE);
                drawIntersection(gc, scale, minX, minY, delivery.getDeliveryIntersection(), 4 * road_width, true);
            }
        }
    }

    private void updateInteractiveLayer(double scale, double minX, double minY) {
        double road_width = 2e-7 * scale; // Road width depends on the map scale
        if (currentDeliveriesDemand == null) return;

        for (Delivery delivery : currentDeliveriesDemand.getDeliveries()) {
            // Add interactive circle over delivery point
            Intersection deliveryIntersection = delivery.getDeliveryIntersection();
            double deliveryX = (deliveryIntersection.getNormalizedX() - minX) * scale;
            double deliveryY = (deliveryIntersection.getNormalizedY() - minY) * scale;
            double radius = 12 * road_width;

            Circle deliveryCircle = new Circle(deliveryX, deliveryY, radius, Color.TRANSPARENT);
            deliveryCircle.setCursor(Cursor.HAND);

            deliveryCircle.setOnMouseClicked(event -> {
                Label content = new Label("Delivery ID : " + deliveryIntersection.getId());
                Popover popover = new Popover(content);
                popover.setArrowLocation(Popover.ArrowLocation.TOP_CENTER);
                Point2D screen = interactiveLayer.localToScreen(deliveryX, deliveryY);
                popover.show(deliveryCircle, screen.getX() - 10, screen.getY());
            });
            interactiveLayer.getChildren().add(deliveryCircle);

            Intersection pickupIntersection = delivery.getTakeoutIntersection();
            double pickupX = (pickupIntersection.getNormalizedX() - minX) * scale;
            double pickupY = (pickupIntersection.getNormalizedY() - minY) * scale;

            Circle pickupCircle = new Circle(pickupX, pickupY, radius, Color.TRANSPARENT);
            pickupCircle.setCursor(Cursor.HAND);

            pickupCircle.setOnMouseClicked(event -> {
                Label content = new Label("Pickup ID : " + pickupIntersection.getId());
                PopOver popover = new PopOver(content);
                popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                Point2D screen = interactiveLayer.localToScreen(pickupX, pickupY);
                popover.show(pickupCircle, screen.getX() - 10, screen.getY());
            });
            interactiveLayer.getChildren().add(pickupCircle);
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

    private static class AutoView {
        double minX;
        double minY;
        double maxX;
        double maxY;
        double scale;
    }
}
