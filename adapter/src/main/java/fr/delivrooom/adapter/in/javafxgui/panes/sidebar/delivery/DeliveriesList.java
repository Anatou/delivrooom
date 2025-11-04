package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Delivery;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * List of deliveries.
 */
public class DeliveriesList extends VBox {

    private final VBox deliveryItemsContainer;
    private final ObservableList<DeliveryListItem> deliveryItems;
    private final Label emptyLabel;

    public DeliveriesList() {
        super(5);
        this.deliveryItems = FXCollections.observableArrayList();

        setPadding(new Insets(5));
        getStyleClass().add("deliveries-list");

        // Container for delivery items
        deliveryItemsContainer = new VBox(3);
        deliveryItemsContainer.setPadding(new Insets(5, 0, 5, 0));

        // Empty state label
        emptyLabel = new Label("No deliveries yet");
        emptyLabel.getStyleClass().add("text-muted");
        emptyLabel.setPadding(new Insets(20));

        // Scroll pane for delivery items
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("edge-to-edge");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Initially show empty label
        scrollPane.setContent(emptyLabel);

        getChildren().add(scrollPane);

        // Listen to changes in delivery items to toggle empty state
        deliveryItems.addListener((javafx.collections.ListChangeListener<DeliveryListItem>) change -> {
            if (deliveryItems.isEmpty()) {
                scrollPane.setContent(emptyLabel);
            } else {
                scrollPane.setContent(deliveryItemsContainer);
            }
        });

        AppController.getController().deliveriesDemandProperty().addListener(o -> update());
        AppController.getController().couriersProperty().addListener((InvalidationListener) o -> update());
    }

    private void update() {
        DeliveriesDemand deliveries = AppController.getController().deliveriesDemandProperty().getValue();
        if (deliveries != null) {
            setDeliveries(deliveries.deliveries());
        } else {
            setDeliveries(List.of());
        }
    }

    /**
     * Update the list with a new set of deliveries.
     */
    public void setDeliveries(List<Delivery> deliveries) {
        deliveryItems.clear();
        deliveryItemsContainer.getChildren().clear();
        for (Delivery delivery : deliveries) {
            DeliveryListItem item = new DeliveryListItem(delivery);
            deliveryItems.add(item);
            deliveryItemsContainer.getChildren().add(item);
        }
    }

}
