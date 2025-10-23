package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.adapter.in.javafxgui.controller.CommandAddDelivery;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;


/**
 * Section for creating new deliveries.
 */
public class DeliveryCreationSection extends VBox {

    private final Label selectedIntersectionLabel;
    private final VBox addDeliveryBox;
    private Intersection takeout;
    private Intersection delivery;
    private AppController controller;
    private boolean requestedIntersectionIsTakeout = true;

    public DeliveryCreationSection() {
        super(10);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_LEFT);
        this.controller = AppController.getController();

        // Section title
        Label titleLabel = new Label("New Delivery");
        titleLabel.getStyleClass().addAll("title-4");

        // State display
        Label stateLabel = new Label("State: " + controller.getState().getClass().getSimpleName());
        stateLabel.getStyleClass().add("text-muted");
        controller.stateProperty().addListener((obs, oldState, newState) -> {
            stateLabel.setText("State: " + newState.getClass().getSimpleName());
        });

        // Add test delivery button
        Button addDeliveryBtn = new Button("Add Delivery");
        addDeliveryBtn.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        addDeliveryBtn.setMaxWidth(Double.MAX_VALUE);
        addDeliveryBtn.getStyleClass().add("success");
        addDeliveryBtn.setOnAction(e -> handleAddTestDelivery());

        Label labelTakeout = new Label("Selected TakeOut Intersection: ");
        labelTakeout.setId("labelTakeout");
        Button buttonTakeout = new Button("Select TakeOut Intersection");
        buttonTakeout.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        TextField durationFieldTakeout = new TextField();
        durationFieldTakeout.setId("durationFieldTakeout");
        durationFieldTakeout.setPromptText("Enter duration in minutes");

        Label labelDelivery = new Label("Selected Delivery Intersection: ");
        labelDelivery.setId("labelDelivery");
        Button buttonDelivery = new Button("Select Delivery Intersection");
        buttonDelivery.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        TextField durationFieldDelivery = new TextField();
        durationFieldDelivery.setId("durationFieldDelivery");
        durationFieldDelivery.setPromptText("Enter duration in minutes");


        Button buttonConfirmAddDelivery = new Button("Confirm");

        buttonTakeout.setOnAction(e -> {
            requestedIntersectionIsTakeout = true;
            controller.handleRequestIntersectionSelection();
        });
        buttonDelivery.setOnAction(e -> {
            requestedIntersectionIsTakeout = false;
            controller.handleRequestIntersectionSelection();
        });


        Label addTitle = new Label("Add Delivery");
        addTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        this.addDeliveryBox = new VBox(8, addTitle, addDeliveryBtn,
                new Separator(),
                labelTakeout, buttonTakeout, durationFieldTakeout,
                labelDelivery, buttonDelivery, durationFieldDelivery,
                buttonConfirmAddDelivery
        );
        addDeliveryBox.setPadding(new Insets(10));
        addDeliveryBox.setId("addDeliveryBox");
        addDeliveryBox.setAlignment(Pos.CENTER_LEFT);
        addDeliveryBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        addDeliveryBox.setVisible(false);
        addDeliveryBox.setManaged(false);
        buttonConfirmAddDelivery.setOnAction(e -> {
            handleConfirmAddDelivery();
            addDeliveryBox.setVisible(false);
            addDeliveryBox.setManaged(false);
        });


        // Select intersection button
        Button selectIntersectionBtn = new Button("Select Intersection");
        selectIntersectionBtn.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        selectIntersectionBtn.setMaxWidth(Double.MAX_VALUE);
        selectIntersectionBtn.setOnAction(e -> controller.handleRequestIntersectionSelection());

        // Selected intersection label
        selectedIntersectionLabel = new Label("No intersection selected");
        selectedIntersectionLabel.getStyleClass().add("text-muted");
        selectedIntersectionLabel.setWrapText(true);

        getChildren().addAll(titleLabel, addDeliveryBtn, addDeliveryBox, selectIntersectionBtn, selectedIntersectionLabel);
    }


    private void handleConfirmAddDelivery() {
        String takeoutDuration = ((TextField) addDeliveryBox.lookup("#durationFieldTakeout")).getText();
        String deliveryDuration = ((TextField) addDeliveryBox.lookup("#durationFieldDelivery")).getText();
        int tDuration = 0;
        int dDuration = 0;
        if (takeout != null && delivery != null && takeoutDuration != null && deliveryDuration != null) {
            try {
                tDuration = Integer.parseInt(takeoutDuration);
                dDuration = Integer.parseInt(deliveryDuration);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
            Delivery addedDelivery = new Delivery(this.takeout, this.delivery, tDuration, dDuration);
            CommandAddDelivery addDeliveryCommand = new CommandAddDelivery(controller, addedDelivery);
            controller.getCommandManager().executeCommand(addDeliveryCommand);
            /*controller.getSidebar().getDeliveriesSection().refreshDeliveries();
            controller.updateMapCanvas();*/

            ((TextField) addDeliveryBox.lookup("#durationFieldTakeout")).clear();
            ((TextField) addDeliveryBox.lookup("#durationFieldDelivery")).clear();


        } else {
            System.out.println("Fill all the...");
        }

    }

    private void handleAddTestDelivery() {
        this.addDeliveryBox.setVisible(true);
        this.addDeliveryBox.setManaged(true);
        takeout = null;
        delivery = null;

        Label labelDelivery = (Label) addDeliveryBox.lookup("#labelDelivery");
        labelDelivery.setText("Selected Delivery Intersection: ");
        Label labelTakeout = (Label) addDeliveryBox.lookup("#labelTakeout");
        labelTakeout.setText("Selected TakeOut Intersection: ");
    }

    /**
     * Update the selected intersection display.
     * Should be called by the controller when an intersection is selected.
     *
     * @param intersection The selected intersection, or null if selection was aborted
     */
    public void selectIntersection(Intersection intersection) {
        if (intersection == null) {
            selectedIntersectionLabel.setText("No intersection selected");
        } else {
            selectedIntersectionLabel.setText("Selected Intersection ID : " + intersection.getId());

            if (requestedIntersectionIsTakeout) {
                addDeliveryBox.setVisible(true);
                addDeliveryBox.setManaged(true);
                this.takeout = intersection;
                Label labelTakeout = (Label) addDeliveryBox.lookup("#labelTakeout");
                labelTakeout.setText("Selected Takeout Intersection: " + this.takeout.getId());
            } else {
                addDeliveryBox.setVisible(true);
                addDeliveryBox.setManaged(true);
                this.delivery = intersection;
                Label labelDelivery = (Label) addDeliveryBox.lookup("#labelDelivery");
                labelDelivery.setText("Selected Delivery Intersection: " + this.delivery.getId());
            }
        }
    }

}
