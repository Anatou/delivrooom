package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * A component that provides action buttons for a delivery, such as assigning a courier
 * or deleting the delivery.
 */
public class DeliveryActionButtons extends HBox {

    Delivery delivery;
    ComboBox<Courier> courierComboBox;

    /**
     * Constructs a set of action buttons for a specific delivery.
     *
     * @param delivery         The delivery to associate with the buttons.
     * @param deleteSideEffect A callback to run after the delete action is performed.
     */
    public DeliveryActionButtons(Delivery delivery, Runnable deleteSideEffect) {
        super(5);
        this.delivery = delivery;
        this.courierComboBox = new ComboBox<>();

        AppController appController = AppController.getController();

        Button deleteBtn = new Button();
        deleteBtn.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        deleteBtn.getStyleClass().addAll("button-icon");
        deleteBtn.setOnAction(e -> {
            appController.requestRemoveDelivery(delivery);
            deleteSideEffect.run();
        });

        courierComboBox.setPromptText("Courier");
        courierComboBox.setItems(appController.couriersProperty());
        courierComboBox.getSelectionModel().select(appController.getCourierForDelivery(delivery));


        courierComboBox.setOnAction(e -> {
            if (!courierComboBox.isFocused()) {
                // If combobox is changed programmatically, it shouldn’t trigger
                return;
            }
            appController.requestAssignCourier(delivery, courierComboBox.getValue());
        });

        getChildren().addAll(courierComboBox, deleteBtn);
    }

}
