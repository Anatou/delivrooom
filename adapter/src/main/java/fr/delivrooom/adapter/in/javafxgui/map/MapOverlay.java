package fr.delivrooom.adapter.in.javafxgui.map;

import atlantafx.base.controls.Popover;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.adapter.in.javafxgui.controller.DeliveriesLoadedState;
import fr.delivrooom.adapter.in.javafxgui.controller.SelectIntersectionState;
import fr.delivrooom.application.model.*;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.PopOver;

import java.util.HashMap;
import java.util.List;

public class MapOverlay extends StackPane {

    private final AppController controller;
    private final Canvas canvasLayer;
    private final Pane deliveryLayer;
    private final Pane intersectionLayer;
    private final double unit_fixed = 5; // The unit size for roads and points dimensioning, fixed with the map
    private double unit_scalable = 5; // The unit size for roads and points dimensioning, scaling with the map
    private double width;
    private double height;
    private double scale;
    private double minX;
    private double minY;

    public MapOverlay(AppController controller) {
        this.controller = controller;
        this.canvasLayer = new Canvas();
        this.deliveryLayer = new Pane();
        this.intersectionLayer = new Pane();

        getChildren().addAll(canvasLayer, deliveryLayer, intersectionLayer);

        controller.stateProperty().addListener((observable, oldValue, newValue) -> {
            updateOverlay(width, height, scale, minX, minY);
        });

        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (controller.getState() instanceof SelectIntersectionState) {
                if (event.getTarget() instanceof Node node) {
                    if (node.getUserData() != "selectIntersection") {
                        controller.handleSelectIntersection(null);
                    }
                }
            }
        });
    }

    public void updateOverlay(double width, double height, double scale, double minX, double minY) {
        unit_scalable = 2e-7 * scale;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.minX = minX;
        this.minY = minY;

        canvasLayer.setWidth(width);
        canvasLayer.setHeight(height);

        canvasLayer.getGraphicsContext2D().clearRect(0, 0, canvasLayer.getWidth(), canvasLayer.getHeight());
        intersectionLayer.getChildren().clear();
        deliveryLayer.getChildren().clear();

        updateCanvasLayer();

        if (controller.getState() instanceof SelectIntersectionState) {
            intersectionLayer.setVisible(true);
            deliveryLayer.setVisible(false);
            updateIntersectionLayer();
        } else if (controller.getState() instanceof DeliveriesLoadedState) {
            intersectionLayer.setVisible(false);
            deliveryLayer.setVisible(true);
            updateDeliveryLayer();
        } else {
            intersectionLayer.setVisible(false);
            deliveryLayer.setVisible(false);
        }
    }

    public void updateCanvasLayer() {
        GraphicsContext gc = canvasLayer.getGraphicsContext2D();
        CityMap cityMap = controller.getCityMap();
        DeliveriesDemand deliveriesDemand = controller.getDeliveriesDemand();
        TourSolution tourSolution = controller.getTourSolution();

        // Draw roads
        gc.setStroke(Color.rgb(220, 220, 220));
        gc.setLineWidth(unit_scalable);
        for (HashMap<Long, Road> subMap : cityMap.roads().values()) {
            for (Road road : subMap.values()) {
                drawRoad(gc, scale, minX, minY, road);
            }
        }
        // Draw intersections
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.rgb(220, 220, 220));
        for (Intersection intersection : cityMap.intersections().values()) {
            drawIntersection(gc, scale, minX, minY, intersection, 1.3 * unit_scalable, true);
        }

        // Draw deliveries demands
        if (deliveriesDemand != null) {

            if (tourSolution == null) {
                // Draw warehouse point in green
                gc.setFill(Color.GREEN);
                drawIntersection(gc, scale, minX, minY, deliveriesDemand.store(), 4 * unit_scalable, true);

                // Takeout point is a red square, delivery point in a blue circle
                for (Delivery delivery : deliveriesDemand.deliveries()) {
                    gc.setFill(Color.BLUE);
                    drawIntersection(gc, scale, minX, minY, delivery.takeoutIntersection(), 4 * unit_scalable, false);
                    gc.setFill(Color.RED);
                    drawIntersection(gc, scale, minX, minY, delivery.deliveryIntersection(), 4 * unit_scalable, true);
                }
            } else {
                // Draw the calculated tour if available (numbers only on store/pickup/delivery in visit order)
                gc.setStroke(Color.LIMEGREEN);
                gc.setLineWidth(2 * unit_scalable);
                // Build map of target intersection ids -> type ("store","pickup","delivery")


                HashMap<Long, String> targetTypes = new HashMap<>();
                Intersection store = deliveriesDemand.store();
                if (store != null) targetTypes.put(store.getId(), "store");
                for (Delivery d : deliveriesDemand.deliveries()) {
                    if (d.takeoutIntersection() != null) {
                        targetTypes.putIfAbsent(d.takeoutIntersection().getId(), "pickup");
                    }
                    if (d.deliveryIntersection() != null) {
                        targetTypes.putIfAbsent(d.deliveryIntersection().getId(), "delivery");
                    }
                }

                // Build visit order position map (warehouse is 1, first pickup is 2, etc.)
                List<Long> visitOrder = tourSolution.deliveryOrder();
                HashMap<Long, Integer> visitPos = new HashMap<>();
                int pos = 1;
                for (Long id : visitOrder) {
                    visitPos.put(id, pos++);
                }

                // Build delivery to pickup map
                HashMap<Long, Long> deliveryToPickup = new HashMap<>();
                for (Delivery d : deliveriesDemand.deliveries()) {
                    if (d.deliveryIntersection() != null && d.takeoutIntersection() != null) {
                        deliveryToPickup.put(d.deliveryIntersection().getId(), d.takeoutIntersection().getId());
                    }
                }

                // Build labels for each visited target (for deliveries, show pickup and delivery positions with a dot)
                HashMap<Long, String> labels = new HashMap<>();
                for (Long id : visitOrder) {
                    String type = targetTypes.get(id);
                    Integer myPos = visitPos.get(id);
                    if ("delivery".equals(type)) {
                        Long pickupId = deliveryToPickup.get(id);
                        Integer pickupPos = pickupId != null ? visitPos.get(pickupId) : null;
                        String suffix = (pickupPos != null) ? String.valueOf(pickupPos) : "?";
                        labels.put(id, (myPos != null ? myPos : -1) + "." + suffix);
                    } else {
                        labels.put(id, myPos != null ? String.valueOf(myPos) : "?");
                    }
                }

                for (Path path : tourSolution.paths()) {
                    List<Road> roads = path.intersections();
                    for (Road road : roads) {
                        drawRoad(gc, scale, minX, minY, road);
                    }
                }

                gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
                gc.setTextBaseline(javafx.geometry.VPos.CENTER);

                for (Long id : visitOrder) {
                    String label = labels.get(id);
                    if (label == null) continue;
                    Intersection inter = cityMap.intersections().get(id);
                    if (inter == null) continue;
                    double x = (inter.getNormalizedX() - minX) * scale;
                    double y = (inter.getNormalizedY() - minY) * scale;

                    double fontSize = Math.max(10.0, 6.0 * unit_scalable);
                    double numBgRadius = fontSize;

                    String type = targetTypes.get(id);
                    Color bgColor = Color.WHITE;
                    Color strokeColor = Color.DARKGREEN;
                    Color textColor = Color.DARKGREEN;
                    if ("store".equals(type)) {
                        bgColor = Color.GREEN;
                        strokeColor = Color.DARKGREEN;
                        textColor = Color.WHITE;
                    } else if ("pickup".equals(type)) {
                        bgColor = Color.BLUE;
                        strokeColor = Color.DARKBLUE;
                        textColor = Color.WHITE;
                    } else if ("delivery".equals(type)) {
                        bgColor = Color.RED;
                        strokeColor = Color.DARKRED;
                        textColor = Color.WHITE;
                    }

                    gc.setFill(bgColor);
                    gc.fillOval(x - numBgRadius, y - numBgRadius, numBgRadius * 2.0, numBgRadius * 2.0);
                    gc.setStroke(strokeColor);
                    gc.setLineWidth(1.0);
                    gc.strokeOval(x - numBgRadius, y - numBgRadius, numBgRadius * 2.0, numBgRadius * 2.0);

                    gc.setFill(textColor);
                    gc.setFont(javafx.scene.text.Font.font(fontSize));
                    gc.fillText(label, x, y);
                }
            }
        }

    }

    public void updateDeliveryLayer() {
        DeliveriesDemand deliveriesDemand = controller.getDeliveriesDemand();

        for (Delivery delivery : deliveriesDemand.deliveries()) {
            // Add interactive circle over delivery point
            Intersection deliveryIntersection = delivery.deliveryIntersection();
            double deliveryX = (deliveryIntersection.getNormalizedX() - minX) * scale;
            double deliveryY = (deliveryIntersection.getNormalizedY() - minY) * scale;
            double radius = 12 * unit_scalable;

            Circle deliveryCircle = new Circle(deliveryX, deliveryY, radius, Color.TRANSPARENT);
            deliveryCircle.setCursor(Cursor.HAND);

            deliveryCircle.setOnMouseClicked(event -> {
                Label content = new Label("Delivery ID : " + deliveryIntersection.getId());
                Popover popover = new Popover(content);
                popover.setArrowLocation(Popover.ArrowLocation.TOP_CENTER);
                Point2D screen = deliveryLayer.localToScreen(deliveryX, deliveryY);
                popover.show(deliveryCircle, screen.getX() - 10, screen.getY());
            });
            deliveryLayer.getChildren().add(deliveryCircle);

            Intersection pickupIntersection = delivery.takeoutIntersection();
            double pickupX = (pickupIntersection.getNormalizedX() - minX) * scale;
            double pickupY = (pickupIntersection.getNormalizedY() - minY) * scale;

            Circle pickupCircle = new Circle(pickupX, pickupY, radius, Color.TRANSPARENT);
            pickupCircle.setCursor(Cursor.HAND);

            pickupCircle.setOnMouseClicked(event -> {
                Label content = new Label("Pickup ID : " + pickupIntersection.getId());
                PopOver popover = new PopOver(content);
                popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                Point2D screen = deliveryLayer.localToScreen(pickupX, pickupY);
                popover.show(pickupCircle, screen.getX() - 10, screen.getY());
            });
            deliveryLayer.getChildren().add(pickupCircle);
        }
    }

    public void updateIntersectionLayer() {
        CityMap cityMap = controller.getCityMap();

        for (Intersection intersection : cityMap.intersections().values()) {
            double intersectionX = (intersection.getNormalizedX() - minX) * scale;
            double intersectionY = (intersection.getNormalizedY() - minY) * scale;
            double radius = 6 * unit_scalable;

            Circle intersectionCircle = new Circle(intersectionX, intersectionY, radius, Color.TRANSPARENT);
            intersectionCircle.setUserData("selectIntersection");
            intersectionCircle.setCursor(Cursor.HAND);

            intersectionCircle.setOnMouseClicked(event -> {
                controller.handleSelectIntersection(intersection);
            });
            intersectionLayer.getChildren().add(intersectionCircle);
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
