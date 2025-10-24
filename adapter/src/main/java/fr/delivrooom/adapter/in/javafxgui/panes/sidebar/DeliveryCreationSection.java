package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
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

    private final VBox addDeliveryBox;
    private final AppController controller;

    private boolean requestedIntersectionIsTakeout = true;
    private Intersection takeout;
    private Intersection delivery;

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

        Label labelTakeout = new Label("Selected TakeOut Intersection: ");
        Button buttonTakeout = new Button("Select TakeOut Intersection");
        buttonTakeout.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        TextField durationFieldTakeout = new TextField();
        durationFieldTakeout.setId("durationFieldTakeout");
        durationFieldTakeout.setPromptText("Enter duration in minutes");

        Label labelDelivery = new Label("Selected Delivery Intersection: ");
        Button buttonDelivery = new Button("Select Delivery Intersection");
        buttonDelivery.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        TextField durationFieldDelivery = new TextField();
        durationFieldDelivery.setId("durationFieldDelivery");
        durationFieldDelivery.setPromptText("Enter duration in minutes");


        Button buttonConfirmAddDelivery = new Button("Confirm");

        buttonTakeout.setOnAction(e -> {
            requestedIntersectionIsTakeout = true;
            controller.requestIntersectionSelection();
        });
        buttonDelivery.setOnAction(e -> {
            requestedIntersectionIsTakeout = false;
            controller.requestIntersectionSelection();
        });


        Label addTitle = new Label("Add Delivery");
        addTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        this.addDeliveryBox = new VBox(8, addTitle,
                new Separator(),
                labelTakeout, buttonTakeout, durationFieldTakeout,
                labelDelivery, buttonDelivery, durationFieldDelivery,
                buttonConfirmAddDelivery
        );
        addDeliveryBox.setPadding(new Insets(10));
        addDeliveryBox.setAlignment(Pos.CENTER_LEFT);

        buttonConfirmAddDelivery.setOnAction(e -> {
            int tDuration;
            int dDuration;
            if (takeout != null && delivery != null) {
                try {
                    tDuration = Integer.parseInt(durationFieldTakeout.getText());
                    dDuration = Integer.parseInt(durationFieldDelivery.getText());
                } catch (NumberFormatException ex) {
                    // TODO: use spinners instead of text fields
                    System.err.println("Invalid duration format for delivery creation");
                    return;
                }
                Delivery addedDelivery = new Delivery(this.takeout, this.delivery, tDuration, dDuration);
                controller.requestAddDelivery(addedDelivery);
            }
            addDeliveryBox.setVisible(false);
            addDeliveryBox.setManaged(false);
        });

        controller.selectedIntersectionProperty().addListener((obs, oldIntersection, newIntersection) -> {
            if (newIntersection != null) {
                if (requestedIntersectionIsTakeout) {
                    addDeliveryBox.setVisible(true);
                    addDeliveryBox.setManaged(true);
                    this.takeout = newIntersection;
                    labelTakeout.setText("Selected Takeout Intersection: " + this.takeout.getId());
                } else {
                    addDeliveryBox.setVisible(true);
                    addDeliveryBox.setManaged(true);
                    this.delivery = newIntersection;
                    labelDelivery.setText("Selected Delivery Intersection: " + this.delivery.getId());
                }
            }
        });

        getChildren().addAll(titleLabel, addDeliveryBox);
    }

}
