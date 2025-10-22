package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;

import java.util.List;


/**
 * List of couriers with controls to add/remove couriers.
 */
public class CouriersList extends VBox {

    private final VBox courierItemsContainer;
    private final ObservableList<CourierListItem> courierItems;
    private int nextCourierId = 1;

    public CouriersList() {
        super(5);
        this.courierItems = FXCollections.observableArrayList();

        setPadding(new Insets(5));
        getStyleClass().add("couriers-list");

        // Bulk creation controls at the top
        HBox bulkCreateBox = new HBox(5);
        bulkCreateBox.setAlignment(Pos.CENTER_LEFT);
        bulkCreateBox.setPadding(new Insets(5));

        Spinner<Integer> courierCountSpinner = new Spinner<>(1, 20, 3);
        courierCountSpinner.setEditable(true);
        courierCountSpinner.setPrefWidth(80);

        Button bulkCreateBtn = new Button("Create");
        bulkCreateBtn.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        bulkCreateBtn.setOnAction(e -> {
            int count = courierCountSpinner.getValue();
            for (int i = 0; i < count; i++) {
                addCourier();
            }
        });

        bulkCreateBox.getChildren().addAll(courierCountSpinner, bulkCreateBtn);

        // Container for courier items
        courierItemsContainer = new VBox(3);
        courierItemsContainer.setPadding(new Insets(5, 0, 5, 0));

        // Scroll pane for courier items
        ScrollPane scrollPane = new ScrollPane(courierItemsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(200);
        scrollPane.getStyleClass().add("edge-to-edge");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Add new courier button at the bottom
        Button addCourierBtn = new Button("New Courier");
        addCourierBtn.setGraphic(new FontIcon(FontAwesomeSolid.USER_PLUS));
        addCourierBtn.setMaxWidth(Double.MAX_VALUE);
        addCourierBtn.getStyleClass().add("accent");
        addCourierBtn.setOnAction(e -> addCourier());

        getChildren().addAll(bulkCreateBox, scrollPane, addCourierBtn);
    }

    private void addCourier() {
        int courierId = nextCourierId++;
        CourierListItem item = new CourierListItem(
                courierId,
                () -> removeCourier(courierId),
                () -> calculateCourierRoute(courierId)
        );
        courierItems.add(item);
        courierItemsContainer.getChildren().add(item);
    }

    private void removeCourier(int courierId) {
        courierItems.removeIf(item -> item.getCourierId() == courierId);
        courierItemsContainer.getChildren().removeIf(node ->
                node instanceof CourierListItem && ((CourierListItem) node).getCourierId() == courierId
        );
    }

    private void calculateCourierRoute(Courier courier) {
        AppController appController = AppController.getController();
        List<Courier> couriers = appController.getCouriers();
        Courier courier_found = null;
        System.out.println("Searching for courier with ID: " + courier.getId());
        for (Courier courier_ : couriers) {
            System.out.println("Checking courier with ID: " + courier_.getId());
            if (courier_.getId() == courier.getId()) {
                courier_found = courier_;
                break;
            }
        }

        // affichage des deliveries demand du courrier
        System.out.println("deliveries demand of :" + courier_found + " \n" + courier_found.getDeliveriesDemand());
        System.out.println("Calculating route for courier " + courier_found);
        appController.calculateTourForCourier(courier_found);
    }
}
