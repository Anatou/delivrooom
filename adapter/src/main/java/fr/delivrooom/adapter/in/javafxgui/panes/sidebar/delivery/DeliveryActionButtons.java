package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
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
        super(5);
        this.delivery = delivery;
        this.courierComboBox = new ComboBox<>();

        AppController appController = AppController.getController();

        Button deleteBtn = new Button();
        deleteBtn.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        deleteBtn.getStyleClass().addAll("button-icon");
        deleteBtn.setOnAction(e -> {
            appController.requestRemoveDelivery(delivery);
        });

        courierComboBox.setPromptText("Courier");
        courierComboBox.setItems(appController.couriersProperty());


        courierComboBox.setOnAction(e -> {
            if (lastSelectedCourier != null) {
                lastSelectedCourier.removeDelivery(delivery);
            }
            if (courierComboBox.getValue() != null) {
                courierComboBox.getValue().addDelivery(delivery, appController.deliveriesDemandProperty().getValue().store());
                lastSelectedCourier = courierComboBox.getValue();
            }
            appController.requestAssignCourier(delivery, courierComboBox.getValue());
        });

        getChildren().addAll(courierComboBox, deleteBtn);
    }

    public void updateDisplayedSelectedCourier(Delivery updatedDelivery, Courier newCourier) {
        if (delivery.equals(updatedDelivery)) {
            courierComboBox.setValue(newCourier);
        }
    }
}
