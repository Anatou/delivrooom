package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class DeliveryActionButtons extends HBox {

    Courier lastSelectedCourier = null;
    Delivery delivery;
    ComboBox<Courier> courierComboBox;

    public DeliveryActionButtons(Delivery delivery) {
        this.delivery = delivery;
        this.courierComboBox = new ComboBox<Courier>();

        AppController appController = AppController.getController();

        Button deleteBtn = new Button();
        deleteBtn.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        deleteBtn.getStyleClass().addAll("button-icon");
        deleteBtn.setOnAction(e -> {
            System.out.println("Delete delivery");
        });

        courierComboBox.setPromptText("Courier");
        courierComboBox.setItems(appController.getCouriers());

        // Create action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (lastSelectedCourier != null) {
                    lastSelectedCourier.removeDelivery(delivery);
                }
                courierComboBox.getValue().addDelivery(delivery, appController.getDeliveriesDemand().store());
                lastSelectedCourier = courierComboBox.getValue();
                appController.handleCourierAssignmentChange(delivery, courierComboBox.getValue());

            }
        };
        courierComboBox.setOnAction(event);

        getChildren().addAll(courierComboBox, deleteBtn);
    }

    public void updateDisplayedSelectedCourier(Delivery updatedDelivery, Courier newCourier) {
        if (delivery.equals(updatedDelivery)) {
            courierComboBox.setValue(newCourier);
        }
    }
}
