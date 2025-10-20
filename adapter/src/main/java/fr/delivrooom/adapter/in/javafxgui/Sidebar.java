package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.adapter.in.javafxgui.command.AddDeliveryCommand;
import fr.delivrooom.adapter.in.javafxgui.command.RemoveDeliveryCommand;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    private final AppController controller;
    private final Label labelTakeout;
    private final Button buttonTakeout;
    private final Label labelDelivery;
    private final Button buttonDelivery;
    private final Button buttonConfirmAddDelivery;
    private final Label labelTakeout2;
    private final Button buttonTakeout2;
    private final Label labelDelivery2;
    private final Button buttonDelivery2;
    private final Button buttonConfirmRemoveDelivery;
    private Long takeoutId;
    private Long deliveryId;

    public Sidebar(AppController controller) {
        super();
        this.controller = controller;

        setAlignment(javafx.geometry.Pos.CENTER);
        setPadding(new javafx.geometry.Insets(10));
        setSpacing(10);

        Label label = new Label("Name is: ");
        Button button = new Button("Get Name");

        button.setOnAction(e -> {
            String name = JavaFXApp.getNameUseCase().getName();
            label.setText("Name is: " + name);
        });


        Button addDeliveryBtn = new Button("Add Delivery");
        addDeliveryBtn.setOnAction(e -> handleAddDelivery());
        this.labelTakeout = new Label("Selected TakeOut Intersection: ");
        this.buttonTakeout = new Button("Select TakeOut Intersection");
        this.labelDelivery = new Label("Selected Delivery Intersection: ");
        this.buttonDelivery = new Button("Select Delivery Intersection");
        this.buttonConfirmAddDelivery = new Button("Confirm");
        Label addTitle = new Label("Add Delivery");
        addTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        VBox addDeliveryBox = new VBox(8, addTitle, addDeliveryBtn,
                new Separator(),
                labelTakeout, buttonTakeout,
                labelDelivery, buttonDelivery,
                buttonConfirmAddDelivery
        );
        addDeliveryBox.setPadding(new Insets(10));
        addDeliveryBox.setAlignment(Pos.CENTER_LEFT);
        addDeliveryBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        modifAddDelivButtons(false);

        Button removeDeliveryBtn = new Button("Remove Delivery");
        this.labelTakeout2 = new Label("Selected TakeOut Intersection: ");
        this.buttonTakeout2 = new Button("Select TakeOut Intersection");
        this.labelDelivery2 = new Label("Selected Delivery Intersection: ");
        this.buttonDelivery2 = new Button("Select Delivery Intersection");
        this.buttonConfirmRemoveDelivery = new Button("Confirm");
        Label removeTitle = new Label("❌ Remove Delivery");
        removeTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        VBox removeDeliveryBox = new VBox(8, removeTitle, removeDeliveryBtn,
                new Separator(),
                labelTakeout2, buttonTakeout2,
                labelDelivery2, buttonDelivery2,
                buttonConfirmRemoveDelivery
        );
        removeDeliveryBox.setPadding(new Insets(10));
        removeDeliveryBox.setAlignment(Pos.CENTER_LEFT);
        removeDeliveryBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        removeDeliveryBtn.setOnAction(e -> handleRemoveDelivery());
        modifRemoveDelivButtons(false);

        getChildren().addAll(label, button, addDeliveryBtn,addDeliveryBox, removeDeliveryBtn,removeDeliveryBox);
    }

    private void handleAddDelivery() {
        modifAddDelivButtons(true);
        labelDelivery.setText("Selected Delivery Intersection: ");
        labelTakeout.setText("Selected TakeOut Intersection: ");

        buttonTakeout.setOnAction(e -> {
            handleTakeoutSelection("add");
            labelTakeout.setText("Selected TakeOut Intersection: "+takeoutId.toString());

        });
        buttonDelivery.setOnAction(e -> {
            handleDeliverySelection("add");
            labelDelivery.setText("Selected Delivery Intersection: "+deliveryId.toString());

        });

        buttonConfirmAddDelivery.setOnAction(e -> {
            handleConfirmAddDelivery(takeoutId, deliveryId);
            modifAddDelivButtons(false);

        });



    }

    private void handleConfirmAddDelivery(Long takeoutId, Long deliveryId){

        Intersection takeoutIntersection = controller.FindIntersectionById(takeoutId);
        Intersection deliveryIntersection = controller.FindIntersectionById(deliveryId);
        Delivery addedDelivery = new Delivery(takeoutIntersection, deliveryIntersection, 5, 5);
        AddDeliveryCommand addDeliveryCommand = new AddDeliveryCommand(controller, addedDelivery);
        controller.getCommandManager().executeCommand(addDeliveryCommand);
    }
    private void handleConfirmRemoveDelivery(Long takeoutId, Long deliveryId){

        Delivery removedDelivery = controller.getDeliveriesDemand().getDeliveryByIds(takeoutId.intValue(), deliveryId.intValue());
        RemoveDeliveryCommand removeDeliveryCommand = new RemoveDeliveryCommand(controller, removedDelivery);
        controller.getCommandManager().executeCommand(removeDeliveryCommand);
    }

    private void handleTakeoutSelection(String mode){
        if ( mode == "add") {
            takeoutId = controller.getMapCanvas().updateAddLayer();
        }else{
            takeoutId = controller.getMapCanvas().updateRemoveLayer();
        }
    }
    private void handleDeliverySelection(String mode){
        if ( mode == "add") {
            deliveryId = controller.getMapCanvas().updateAddLayer();
        }else{
            deliveryId = controller.getMapCanvas().updateRemoveLayer();
        }


    }

    private void handleRemoveDelivery() {
        modifRemoveDelivButtons(true);
        labelTakeout2.setText("Selected TakeOut Intersection: ");
        labelDelivery2.setText("Selected Delivery Intersection: ");

        buttonTakeout2.setOnAction(e -> {
                    handleTakeoutSelection("remove");
                    labelTakeout2.setText("Selected TakeOut Intersection: "+takeoutId.toString());
                }
        );

        buttonDelivery2.setOnAction(e -> {
            handleDeliverySelection("remove");
            labelDelivery2.setText("Selected Delivery Intersection: "+deliveryId.toString());
        });
        buttonConfirmRemoveDelivery.setOnAction(e -> {
            handleConfirmRemoveDelivery(takeoutId, deliveryId);
            modifRemoveDelivButtons(false);

        });

    }
    private void modifAddDelivButtons(boolean visible){
        labelTakeout.setVisible(visible);
        labelTakeout.setManaged(visible);
        buttonTakeout.setVisible(visible);
        buttonTakeout.setManaged(visible);
        labelDelivery.setVisible(visible);
        labelDelivery.setManaged(visible);
        buttonDelivery.setVisible(visible);
        buttonDelivery.setManaged(visible);
        buttonConfirmAddDelivery.setVisible(visible);
        buttonConfirmAddDelivery.setManaged(visible);
    }
    private void modifRemoveDelivButtons(boolean visible){
        labelTakeout2.setVisible(visible);
        labelTakeout2.setManaged(visible);
        buttonTakeout2.setVisible(visible);
        buttonTakeout2.setManaged(visible);
        labelDelivery2.setVisible(visible);
        labelDelivery2.setManaged(visible);
        buttonDelivery2.setVisible(visible);
        buttonDelivery2.setManaged(visible);
        buttonConfirmRemoveDelivery.setVisible(visible);
        buttonConfirmRemoveDelivery.setManaged(visible);
    }
}
