package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.adapter.in.javafxgui.command.AddDeliveryCommand;
import fr.delivrooom.adapter.in.javafxgui.command.RemoveDeliveryCommand;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    private final AppController controller;

    public Sidebar(AppController controller) {
        super();
        this.controller = controller;

        setAlignment(javafx.geometry.Pos.CENTER);
        setPadding(new javafx.geometry.Insets(10));

        Label label = new Label("Name is: ");
        Button button = new Button("Get Name");

        button.setOnAction(e -> {
            String name = JavaFXApp.getNameUseCase().getName();
            label.setText("Name is: " + name);
        });


        Button addDeliveryBtn = new Button("Add Delivery");
        addDeliveryBtn.setOnAction(e -> handleAddDelivery());

        Button removeDeliveryBtn = new Button("Remove Delivery");
        removeDeliveryBtn.setOnAction(e -> handleRemoveDelivery());

        getChildren().addAll(label, button, addDeliveryBtn, removeDeliveryBtn);
    }

    private void handleAddDelivery() {
        Intersection takeoutIntersection = new Intersection(99, 45.550404, 4.8744674);
        Intersection deliveryIntersection = new Intersection(100, 45.770404, 4.8744674);
        Delivery addedDelivery = new Delivery(takeoutIntersection, deliveryIntersection, 5, 5);
        AddDeliveryCommand addDeliveryCommand = new AddDeliveryCommand(controller, addedDelivery);
        controller.getCommandManager().executeCommand(addDeliveryCommand);

    }

    private void handleRemoveDelivery() {
        RemoveDeliveryCommand removeDeliveryCommand = new RemoveDeliveryCommand(controller, controller.getDeliveriesDemand().getDeliveryByIds(99, 100));
        controller.getCommandManager().executeCommand(removeDeliveryCommand);
    }
}
