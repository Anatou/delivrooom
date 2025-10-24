

package fr.delivrooom.adapter.in.javafxgui.map;

import atlantafx.base.controls.Popover;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.adapter.in.javafxgui.controller.StateDeliveriesLoaded;
import fr.delivrooom.adapter.in.javafxgui.controller.StateSelectIntersection;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery.DeliveryTooltip;
import fr.delivrooom.application.model.*;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapOverlay extends StackPane {

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

    public MapOverlay() {
        this.canvasLayer = new Canvas();
        this.deliveryLayer = new Pane();
        this.intersectionLayer = new Pane();

        getChildren().addAll(canvasLayer, deliveryLayer, intersectionLayer);

        AppController controller = AppController.getController();

        controller.stateProperty().addListener((observable, oldValue, newValue) -> {
            updateOverlay(width, height, scale, minX, minY);
        });

        controller.memeModeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setOpacity(0.5);
            } else {
                setOpacity(1.0);
            }
        });

        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (controller.getState() instanceof StateSelectIntersection) {
                if (event.getTarget() instanceof Node node) {
                    if (node.getUserData() != "selectIntersection") {
                        controller.requestSelectIntersection(null);
                    }
                }
            }
        });
    }

    public void clear() {
        canvasLayer.getGraphicsContext2D().clearRect(0, 0, canvasLayer.getWidth(), canvasLayer.getHeight());
        intersectionLayer.getChildren().clear();
        deliveryLayer.getChildren().clear();
    }

    public void updateOverlay(double width, double height, double scale, double minX, double minY) {
        AppController controller = AppController.getController();
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

        if (controller.getState() instanceof StateSelectIntersection) {
            intersectionLayer.setVisible(true);
            deliveryLayer.setVisible(false);
            updateIntersectionLayer();
        } else if (controller.getState() instanceof StateDeliveriesLoaded) {
            intersectionLayer.setVisible(false);
            deliveryLayer.setVisible(true);
            updateDeliveryLayer();
        } else {
            intersectionLayer.setVisible(false);
            deliveryLayer.setVisible(false);
        }
    }

    public void updateCanvasLayer() {
        AppController controller = AppController.getController();

        GraphicsContext gc = canvasLayer.getGraphicsContext2D();
        CityMap cityMap = controller.cityMapProperty().getValue();
        if (cityMap == null) return;
        DeliveriesDemand deliveriesDemand = controller.deliveriesDemandProperty().getValue();
        DeliveriesDemand deliveriesLeft = new DeliveriesDemand(
                new ArrayList<>(deliveriesDemand.deliveries()),
                deliveriesDemand.store()
        );

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
        boolean isStoreDrawn = false;
        if (deliveriesDemand != null) {
            System.out.println("Drawing deliveries demand on map overlay.");
            // iterate trough couriers to draw their paths if available
            List<Courier> allCouriers = controller.couriersProperty();
            for (Courier courier : allCouriers) {
                TourSolution courierTour = courier.getTourSolution();
                if (courierTour != null) {
                    // attribute a color to each courier path
                    System.out.println("Drawing tour for courier: " + courier.getId());
                    displayTourSolution(courier);
                    isStoreDrawn = true;
                    // Remove delivered deliveries from deliveriesLeft
                    DeliveriesDemand courierDemand = courier.getDeliveriesDemand();
                    for (Delivery d : courierDemand.deliveries()) {
                        deliveriesLeft.deliveries().remove(d);
                    }
                }
            }
            // Display the remaining deliveries in red for pickup and blue for delivery
            // Draw warehouse point in green
            if (!isStoreDrawn) {
                gc.setFill(Color.GREEN);
                drawIntersection(gc, scale, minX, minY, deliveriesLeft.store(), 4 * unit_scalable, true);
            }
            // Takeout point is a red square, delivery point in a blue circle
            for (Delivery delivery : deliveriesLeft.deliveries()) {
                gc.setFill(Color.BLUE);
                drawIntersection(gc, scale, minX, minY, delivery.takeoutIntersection(), 4 * unit_scalable, false);
                gc.setFill(Color.RED);
                drawIntersection(gc, scale, minX, minY, delivery.deliveryIntersection(), 4 * unit_scalable, true);
            }

        }

    }

    public void displayTourSolution(Courier courier) {
        // This method can be used to highlight or display the tour solution in a specific way if needed
        // Currently, the tour solution is drawn in the updateCanvasLayer method
        AppController controller = AppController.getController();

        GraphicsContext gc = canvasLayer.getGraphicsContext2D();
        CityMap cityMap = controller.cityMapProperty().getValue();
        if (cityMap == null) return;

        DeliveriesDemand deliveriesDemand = courier.getDeliveriesDemand();
        TourSolution tourSolution = courier.getTourSolution();
        Color courierColor = courier.getColor();


        // Draw the calculated tour if available (numbers only on store/pickup/delivery in visit order)
        gc.setStroke(courierColor);
        gc.setLineWidth(4 * unit_scalable);
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

    public void updateDeliveryLayer() {
        DeliveriesDemand deliveriesDemand = AppController.getController().deliveriesDemandProperty().getValue();
        if (deliveriesDemand == null) return;

        for (Delivery delivery : deliveriesDemand.deliveries()) {
            // Add interactive circle over delivery point
            Intersection deliveryIntersection = delivery.deliveryIntersection();
            double deliveryX = (deliveryIntersection.getNormalizedX() - minX) * scale;
            double deliveryY = (deliveryIntersection.getNormalizedY() - minY) * scale;
            double radius = 12 * unit_scalable;

            Circle deliveryCircle = new Circle(deliveryX, deliveryY, radius, Color.TRANSPARENT);
            deliveryCircle.setCursor(Cursor.HAND);

            deliveryCircle.setOnMouseClicked(event -> {
                DeliveryTooltip tooltip = new DeliveryTooltip(delivery, true);
                tooltip.setArrowLocation(Popover.ArrowLocation.TOP_CENTER);
                Point2D screen = deliveryLayer.localToScreen(deliveryX, deliveryY);
                tooltip.show(deliveryCircle, screen.getX() - 10, screen.getY());
            });
            deliveryLayer.getChildren().add(deliveryCircle);

            Intersection pickupIntersection = delivery.takeoutIntersection();
            double pickupX = (pickupIntersection.getNormalizedX() - minX) * scale;
            double pickupY = (pickupIntersection.getNormalizedY() - minY) * scale;

            Circle pickupCircle = new Circle(pickupX, pickupY, radius, Color.TRANSPARENT);
            pickupCircle.setCursor(Cursor.HAND);

            pickupCircle.setOnMouseClicked(event -> {
                DeliveryTooltip tooltip = new DeliveryTooltip(delivery, true);
                tooltip.setArrowLocation(Popover.ArrowLocation.TOP_CENTER);
                Point2D screen = deliveryLayer.localToScreen(pickupX, pickupY);
                tooltip.show(pickupCircle, screen.getX() - 10, screen.getY());
            });
            deliveryLayer.getChildren().add(pickupCircle);
        }
    }

    public void updateIntersectionLayer() {
        CityMap cityMap = AppController.getController().cityMapProperty().getValue();
        if (cityMap == null) return;

        for (Intersection intersection : cityMap.intersections().values()) {
            double intersectionX = (intersection.getNormalizedX() - minX) * scale;
            double intersectionY = (intersection.getNormalizedY() - minY) * scale;
            double radius = 6 * unit_scalable;

            Circle intersectionCircle = new Circle(intersectionX, intersectionY, radius, Color.TRANSPARENT);
            intersectionCircle.setUserData("selectIntersection");
            intersectionCircle.setCursor(Cursor.HAND);

            intersectionCircle.setOnMouseClicked(event -> {
                AppController.getController().requestSelectIntersection(intersection);
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
