package fr.delivrooom.adapter.in.javafxgui.map;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.adapter.in.javafxgui.controller.StateInitial;
import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.Intersection;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class MapCanvas extends StackPane {

    // Zoom config
    private static final double ZOOM_STEP = 1.5;
    private final MapTiles tilesLayer;
    private final MapOverlay overlayLayer;
    private final Pane controlsLayer;
    // View state
    private boolean autoFraming = true;
    private double manualScale = 1.0;      // pixels per normalized unit
    private double manualMinX = 0.0;       // normalized coord of left
    private double manualMinY = 0.0;       // normalized coord of top
    private double lastMouseX;
    private double lastMouseY;
    private boolean dragging = false;


    public MapCanvas() {

        this.tilesLayer = new MapTiles();
        this.overlayLayer = new MapOverlay();
        this.controlsLayer = new Pane();

        setStyle("-fx-background-color: #8D8E7F;");
        setMinSize(0, 0);

        getChildren().addAll(tilesLayer, overlayLayer, controlsLayer);
        setupCanvas();
        setupControls();

        ChangeListener<Object> listener = (obs, oldVal, newVal) -> {
            this.autoFraming = true;
            drawMap();
        };

        AppController.getController().cityMapProperty().addListener(listener);
        AppController.getController().deliveriesDemandProperty().addListener(listener);
        AppController.getController().tourSolutionProperty().addListener(listener);
    }

    private void setupCanvas() {
        InvalidationListener canvasResizeListener = (o) -> {
            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }
            tilesLayer.setWidth(getWidth());
            tilesLayer.setHeight(getHeight());
            overlayLayer.setPrefWidth(getWidth());
            overlayLayer.setPrefHeight(getHeight());
            drawMap();
        };
        widthProperty().addListener(canvasResizeListener);
        heightProperty().addListener(canvasResizeListener);
    }

    private void setupControls() {
        AppController controller = AppController.getController();
        // Controls not visible by default
        controlsLayer.setVisible(false);

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
            ensureManualFromAuto();
            zoomAt(getWidth() / 2.0, getHeight() / 2.0, ZOOM_STEP);
        });
        zoomOutBtn.setOnAction(e -> {
            ensureManualFromAuto();
            zoomAt(getWidth() / 2.0, getHeight() / 2.0, 1.0 / ZOOM_STEP);
        });
        resetBtn.setOnAction(e -> {
            autoFraming = true;
            drawMap();
        });

        VBox controls = new VBox(5, zoomInBtn, zoomOutBtn, resetBtn);

        StackPane.setAlignment(controlsLayer, Pos.TOP_RIGHT);
        StackPane.setMargin(controlsLayer, new Insets(10));
        controlsLayer.setMaxSize(30, 0);
        controlsLayer.getChildren().setAll(controls);

        // Mouse interactions
        addEventHandler(ScrollEvent.SCROLL, e -> {
            if (controller.getState() instanceof StateInitial) return;
            ensureManualFromAuto();
            double factor = e.getDeltaY() > 0 ? Math.pow(ZOOM_STEP, 0.15) : 1.0 / Math.pow(ZOOM_STEP, 0.15);
            zoomAt(e.getX(), e.getY(), factor);
            e.consume();
        });

        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (controller.getState() instanceof StateInitial) return;
            if (e.isPrimaryButtonDown()) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                dragging = true;
            }
        });

        addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!dragging) return;
            if (controller.getState() instanceof StateInitial) return;
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
        AppController controller = AppController.getController();

        if (!autoFraming) return;
        if (controller.getState() instanceof StateInitial || getWidth() == 0 || getHeight() == 0) return;
        AutoView v = computeAutoView(getWidth(), getHeight(), controller.cityMapProperty().getValue());
        manualScale = v.scale;
        manualMinX = v.minX;
        manualMinY = v.minY;
        autoFraming = false;
    }

    private void panByPixels(double dx, double dy) {
        // Move the view so the content follows the cursor
        manualMinX -= dx / manualScale;
        manualMinY -= dy / manualScale;
        drawMap();
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

        drawMap();
    }

    private AutoView computeAutoView(double width, double height, CityMap cityMap) {
        double padding = 30e-7; // Paddings depend on the map scale
        double minX = cityMap.intersections().values().stream().mapToDouble(Intersection::getNormalizedX).min().orElse(0) - padding;
        double maxX = cityMap.intersections().values().stream().mapToDouble(Intersection::getNormalizedX).max().orElse(1) + padding;
        double minY = cityMap.intersections().values().stream().mapToDouble(Intersection::getNormalizedY).min().orElse(0) - padding;
        double maxY = cityMap.intersections().values().stream().mapToDouble(Intersection::getNormalizedY).max().orElse(1) + padding;

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

    public void drawMap() {
        if (getWidth() <= 0 || getHeight() <= 0) return;
        CityMap cityMap = AppController.getController().cityMapProperty().getValue();
        controlsLayer.setVisible(cityMap != null);
        overlayLayer.setVisible(cityMap != null);
        tilesLayer.setVisible(cityMap != null);

        // Return if no city map is loaded
        if (cityMap == null) {
            return;
        }

        // Compute visible area
        double minX;
        double minY;
        double maxX;
        double maxY;
        double scale;
        if (autoFraming) {
            AutoView v = computeAutoView(getWidth(), getHeight(), cityMap);
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
        tilesLayer.drawTiles(scale, minX, maxX, minY, maxY);
        overlayLayer.updateOverlay(getWidth(), getHeight(), scale, minX, minY);
    }

    /**
     * Clear the tile cache (used for easter egg meme mode)
     */
    public void clearTileCache() {
        tilesLayer.clearCache();
    }

    private static class AutoView {
        double minX;
        double minY;
        double maxX;
        double maxY;
        double scale;
    }
}
