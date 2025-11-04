

package fr.delivrooom.adapter.in.javafxgui.map;

import atlantafx.base.controls.Popover;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.adapter.in.javafxgui.controller.StateDeliveriesLoaded;
import fr.delivrooom.adapter.in.javafxgui.controller.StateSelectIntersection;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery.DeliveryTooltip;
import fr.delivrooom.application.model.*;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapOverlay extends StackPane {

    private final Canvas canvasLayer;
    private final Pane deliveryLayer;
    private final Pane intersectionLayer;
    private final Image storeImageIcon;
    private final Image pickupImageIcon;
    private final Image depositImageIcon;
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

        final Pane dummyPane = new Pane();
        final Scene dummyScene = new Scene(dummyPane);
        storeImageIcon = getImageIcon(FontAwesomeSolid.WAREHOUSE, dummyPane, Color.GREEN);
        pickupImageIcon = getImageIcon(FontAwesomeSolid.ARROW_CIRCLE_UP, dummyPane, Color.BLUE);
        depositImageIcon = getImageIcon(FontAwesomeSolid.ARROW_CIRCLE_DOWN, dummyPane, Color.RED);

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
        HashMap<Long, Boolean> isDeliveryInCourierTour = new HashMap<>();

        // Draw roads
        gc.setStroke(Color.rgb(220, 220, 220));
        gc.setLineWidth(unit_scalable);
        for (HashMap<Long, Road> subMap : cityMap.roads().values()) {
            for (Road road : subMap.values()) {
                drawRoad(gc, scale, minX, minY, road, false);
            }
        }
        // Draw intersections
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.rgb(220, 220, 220));
        for (Intersection intersection : cityMap.intersections().values()) {
            drawIntersection(gc, scale, minX, minY, intersection, 1.3 * unit_scalable, null, null, -1);
        }

        // Draw deliveries demands
        boolean isStoreDrawn = false;
        if (deliveriesDemand != null) {
            DeliveriesDemand deliveriesLeft = new DeliveriesDemand(
                    new ArrayList<>(deliveriesDemand.deliveries()),
                    deliveriesDemand.store()
            );
            // iterate trough couriers to draw their paths if available
            List<Courier> allCouriers = controller.couriersProperty();
            for (Courier courier : allCouriers) {
                TourSolution courierTour = courier.getTourSolution();
                if (courierTour != null && courier.isDisplayTourSolution()) {
                    // attribute a color to each courier path
                    displayTourSolution(courier);
                    // Remove delivered deliveries from deliveriesLeft
                    for (Delivery delivery : courier.getDeliveriesDemand().deliveries()) {
                        isDeliveryInCourierTour.put(delivery.deliveryIntersection().getId(), true);
                        isDeliveryInCourierTour.put(delivery.takeoutIntersection().getId(), true);

                    }
                }
            }
            // Display the remaining deliveries in red for pickup and blue for delivery
            // Draw warehouse point in green
            drawIntersection(gc, scale, minX, minY, deliveriesLeft.store(), 4 * unit_scalable, storeImageIcon, null, -1);
            // Takeout point is a red square, delivery point in a blue circle
            int pairIndex = 0;
            for (Delivery delivery : deliveriesDemand.deliveries()) {
                if (isDeliveryInCourierTour.getOrDefault(delivery.deliveryIntersection().getId(), false)) {
                    continue;
                }
                String label = indexToLabel(pairIndex++);
                drawIntersection(gc, scale, minX, minY, delivery.takeoutIntersection(), 4 * unit_scalable, pickupImageIcon, label, -1);
                drawIntersection(gc, scale, minX, minY, delivery.deliveryIntersection(), 4 * unit_scalable, depositImageIcon, label, -1);
            }

        }

    }

    private String indexToLabel(int index) {
        // Convertit 0 -> "A", 1 -> "B", ..., 25 -> "Z", 26 -> "AA", etc.
        StringBuilder sb = new StringBuilder();
        int n = index + 1; // rendre 1-based pour la conversion
        while (n > 0) {
            int rem = (n - 1) % 26;
            sb.insert(0, (char) ('A' + rem));
            n = (n - 1) / 26;
        }
        return sb.toString();
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
        Color courierColor = Color.hsb((courier.getId() * 137) % 360, 0.7, 0.9);

        // Draw the calculated tour if available (numbers only on store/pickup/delivery in visit order)
        gc.setStroke(courierColor);
        gc.setFill(courierColor);
        gc.setLineWidth(4 * unit_scalable);
        // Build map of target intersection ids -> type ("store","pickup","delivery")

        if (tourSolution != null) {
            gc.setLineWidth(2 * unit_scalable);

            for (Path path : tourSolution.paths()) {
                List<Road> roads = path.intersections();
                float deltaArrow = 0;
                for (Road road : roads) {
                    deltaArrow += road.getLength();
                    boolean drawArrow = false;
                    if (deltaArrow > 500) { drawArrow = true; deltaArrow = 0; }
                    drawRoad(gc, scale, minX, minY, road, drawArrow);
                }
            }
        }
        // display numbers on store/pickup/delivery in visit order
        int visitIndex = 1;
        for (Long intersectionId : tourSolution.deliveryOrder()) {
            Intersection intersection = cityMap.intersections().get(intersectionId);
            if (intersection != null) {

                if (intersectionId == deliveriesDemand.store().getId()) {
                    continue;
                }
                // check if pickup or delivery
                boolean isPickup = false;
                for (Delivery delivery : deliveriesDemand.deliveries()) {
                    if (delivery.takeoutIntersection().getId() == intersectionId) {
                        isPickup = true;
                        break;
                    }
                }
                if (isPickup) {
                    drawIntersection(gc, scale, minX, minY, intersection, 4 * unit_scalable, pickupImageIcon, Integer.toString(visitIndex), courier.getId());
                } else {
                    drawIntersection(gc, scale, minX, minY, intersection, 4 * unit_scalable, depositImageIcon, Integer.toString(visitIndex), courier.getId());
                }

                visitIndex += 1;
            }
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
        DeliveriesDemand deliveriesDemand = AppController.getController().deliveriesDemandProperty().getValue();

        if (cityMap == null) return;

        for (Intersection intersection : cityMap.intersections().values()) {
            double intersectionX = (intersection.getNormalizedX() - minX) * scale;
            double intersectionY = (intersection.getNormalizedY() - minY) * scale;
            double radius = 6 * unit_scalable;
            boolean isPickupOrDeposit = false;

            Circle intersectionCircle = new Circle(intersectionX, intersectionY, radius, Color.TRANSPARENT);
           for (Delivery delivery : deliveriesDemand.deliveries()){
               if ((intersection.getNormalizedX()== delivery.takeoutIntersection().getNormalizedX() && intersection.getNormalizedY() == delivery.takeoutIntersection().getNormalizedY())
                   ||(intersection.getNormalizedX()== delivery.deliveryIntersection().getNormalizedX() && intersection.getNormalizedY() == delivery.deliveryIntersection().getNormalizedY())){
                   isPickupOrDeposit = true;
                   break;
               }
           }
           if (!isPickupOrDeposit) {
               intersectionCircle.setUserData("selectIntersection");
               intersectionCircle.setCursor(Cursor.HAND);
               intersectionCircle.setOnMouseClicked(event -> {
                   AppController.getController().requestSelectIntersection(intersection);
               });
           }


            intersectionLayer.getChildren().add(intersectionCircle);
        }
    }


    private void drawIntersection(GraphicsContext gc, double scale, double minX, double minY, Intersection intersection, double radius, Image icon, String text, int courierId) {
        double x = (intersection.getNormalizedX() - minX) * scale;
        double y = (intersection.getNormalizedY() - minY) * scale;

        if (icon!=null) {
            Paint oldColor = gc.getFill();
            gc.setFill(Color.WHITE);
            gc.fillOval(x - icon.getHeight()/1.5, y - icon.getHeight()/1.5, 2 * icon.getHeight()/1.5, 2 * icon.getHeight()/1.5);
            gc.setFill(oldColor);
            if (icon == storeImageIcon) {
                gc.drawImage(icon, x-icon.getWidth()/3, y-icon.getHeight()/2.5, icon.getWidth()/1.5, icon.getHeight()/1.5);
            } else {
                gc.drawImage(icon, x-icon.getWidth()/2*1.5, y-icon.getHeight()/2*1.5, icon.getWidth()*1.5, icon.getHeight()*1.5);
            }
        } else {
            gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        }

        if (text != null) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.BASELINE);
            Paint oldColor = gc.getFill();
            double fontSize = radius;
            double circleRadius = radius;
            double x_offset = 0;
            double y_offset = 0;
            if (icon!=null) {
                fontSize = icon.getHeight()/1.5;
                circleRadius = icon.getHeight();
                x_offset = icon.getWidth()/2;
                y_offset = icon.getHeight()/2;
            }
            gc.setFont(javafx.scene.text.Font.font(fontSize));
            // display color of the courier insted
            Color courierColor = (courierId == -1) ? Color.WHITE : Color.hsb((courierId * 137) % 360, 0.7, 0.9);

            gc.setFill(courierColor);
            gc.fillOval(x, y, circleRadius, circleRadius);
            gc.setFill(Color.BLACK);
            gc.fillText(text, x+x_offset, y+y_offset+fontSize/3);
            gc.setFill(oldColor);
        }
    }

    private Image getImageIcon(FontAwesomeSolid icon, Pane dummyPane, Paint iconColor) {
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconColor(iconColor);
        dummyPane.getChildren().add(fontIcon);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        Image fontImage = fontIcon.snapshot(snapshotParameters, null);
        dummyPane.getChildren().remove(fontIcon);
        return fontImage;
    }

    private void drawRoad(GraphicsContext gc, double scale, double minX, double minY, Road road, Boolean drawArrow) {
        if (drawArrow==null) { drawArrow = false; }

        Intersection origin = road.getOrigin();
        Intersection destin = road.getDestination();

        double x1 = (origin.getNormalizedX() - minX) * scale;
        double y1 = (origin.getNormalizedY() - minY) * scale;
        double x2 = (destin.getNormalizedX() - minX) * scale;
        double y2 = (destin.getNormalizedY() - minY) * scale;

        gc.strokeLine(x1, y1, x2, y2);

        if (drawArrow) {
            double road_vector_x = x2-x1;
            double road_vector_y = y2-y1;

            double triangle_center_x = (x1 + x2) / 2;
            double triangle_center_y = (y1 + y2) / 2;

            double alpha = Math.atan2(road_vector_y, road_vector_x) - Math.PI/2;

            double triangle_size = 0.000001*scale;

            double[] xPoints = {
                    -triangle_size*Math.sin(alpha) + triangle_center_x,
                    -triangle_size*Math.sin(alpha + 2*Math.PI/3) + triangle_center_x,
                    -triangle_size*Math.sin(alpha + 4*Math.PI/3) + triangle_center_x,
            };

            double[] yPoints = {
                    triangle_size*Math.cos(alpha) + triangle_center_y,
                    triangle_size*Math.cos(alpha + 2*Math.PI/3) + triangle_center_y,
                    triangle_size*Math.cos(alpha + 4*Math.PI/3) + triangle_center_y,
            };
            Color oldColor = (Color)gc.getFill();
            Color toDarken = (Color)gc.getStroke();
            gc.setFill(toDarken.brighter());
            gc.fillPolygon(xPoints, yPoints, 3);
            gc.setFill(oldColor);
        }
    }
}
