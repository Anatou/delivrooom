package fr.delivrooom.adapter.in.javafxgui.panes;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
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
    Label selectedIntersectionLabel;

    public Sidebar(AppController controller) {
        super();
        this.controller = controller;

        setAlignment(javafx.geometry.Pos.CENTER);
        setPadding(new javafx.geometry.Insets(10));
        setSpacing(10);

        Label stateLabel = new Label("State is: " + controller.getState().getClass().getSimpleName());
        controller.stateProperty().addListener((obs, oldState, newState) -> {
            stateLabel.setText("State is: " + newState.getClass().getSimpleName());
        });
        getChildren().add(stateLabel);


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


        Button selectIntersection = new Button("Select Intersection");
        selectIntersection.setOnAction(e -> controller.handleRequestIntersectionSelection());

        selectedIntersectionLabel = new Label("No intersection selected");


        getChildren().addAll(label, button, addDeliveryBtn, removeDeliveryBtn, selectIntersection, selectedIntersectionLabel);
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

    /**
     * Should be called by the controller when an intersection is selected on the map.
     *
     * @param intersection If null, it means the operation has been aborted.
     */
    public void selectIntersection(Intersection intersection) {
        ;
        selectedIntersectionLabel.setText(intersection == null ? "No intersection selected" : "Selected intersection ID :" + intersection.getId());
    }
}
