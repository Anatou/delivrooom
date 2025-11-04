package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import atlantafx.base.controls.Spacer;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;


/**
 * Section for creating new deliveries.
 */
public class DeliveryCreationSection extends VBox {

    private final VBox addDeliveryBox;
    private final AppController controller;
    private final TitledPane titledPane;

    private boolean requestedIntersectionIsTakeout = true;
    private Intersection takeout;
    private Intersection delivery;

    public DeliveryCreationSection() {
        super(0);
        //setPadding(new Insets(10));
        //setAlignment(Pos.TOP_LEFT);
        this.controller = AppController.getController();

        // Create button and Sprinner : select takeout intersection and its duration
        Button buttonTakeout = new Button("Select TakeOut Intersection");
        buttonTakeout.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        buttonTakeout.setMaxWidth(Double.MAX_VALUE);
        Spinner<Integer> durationSpinnerTakeout = new Spinner<>(1,20,5);
        durationSpinnerTakeout.setEditable(true);
        durationSpinnerTakeout.setMinWidth(175);
        durationSpinnerTakeout.setId("durationSpinnerTakeout");
        durationSpinnerTakeout.setPromptText("Duration in minutes");
        HBox selectIntersectionTakeout = new HBox(5,buttonTakeout, durationSpinnerTakeout);

        // Create button and Sprinner : select delivery intersection and its duration
        Button buttonDelivery = new Button("Select Delivery Intersection");
        buttonDelivery.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        buttonDelivery.setMaxWidth(Double.MAX_VALUE);
        Spinner<Integer> durationSpinnerDelivery = new Spinner<>(1,20,5);
        durationSpinnerDelivery.setEditable(true);
        durationSpinnerDelivery.setMinWidth(175);
        durationSpinnerDelivery.setId("durationSpinnerDelivery");
        durationSpinnerDelivery.setPromptText("Duration in minutes");
        HBox selectIntersectionDelivery = new HBox(5,buttonDelivery, durationSpinnerDelivery);


        // Create button Confirm the choice of the selected Delivery
        Button buttonConfirmAddDelivery = new Button("Add Delivery");
        buttonConfirmAddDelivery.setMaxWidth(Double.MAX_VALUE);
        buttonConfirmAddDelivery.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        buttonConfirmAddDelivery.getStyleClass().add("accent");

        buttonTakeout.setOnAction(e -> {
            requestedIntersectionIsTakeout = true;
            buttonTakeout.getStyleClass().add("accent");
            controller.requestIntersectionSelection();
        });
        buttonDelivery.setOnAction(e -> {
            requestedIntersectionIsTakeout = false;
            buttonDelivery.getStyleClass().add("accent");
            controller.requestIntersectionSelection();
        });
        buttonConfirmAddDelivery.setOnAction(e -> {
            int tDuration;
            int dDuration;
            tDuration = durationSpinnerTakeout.getValue();
            dDuration = durationSpinnerDelivery.getValue();
            if (takeout != null && delivery != null) {
                // Add the delivery by calling the controller and reset style of buttons
                Delivery addedDelivery = new Delivery(this.takeout, this.delivery, tDuration, dDuration);
                controller.requestAddDelivery(addedDelivery);
                buttonTakeout.setText("Select TakeOut Intersection");
                buttonDelivery.setText("Select Delivery Intersection");
                buttonDelivery.getStyleClass().removeAll("success","accent");
                buttonTakeout.getStyleClass().removeAll("success","accent");
            }
        });


        this.addDeliveryBox = new VBox(5,
                selectIntersectionTakeout,
                selectIntersectionDelivery,
                buttonConfirmAddDelivery
        );
        addDeliveryBox.setAlignment(Pos.CENTER_LEFT);


        controller.selectedIntersectionProperty().addListener((obs, oldIntersection, newIntersection) -> {
            if (newIntersection != null) {
                if (requestedIntersectionIsTakeout) {
                    this.takeout = newIntersection;
                    buttonTakeout.getStyleClass().add("success");
                    buttonTakeout.setText("Change TakeOut Intersection");
                } else {
                    this.delivery = newIntersection;
                    buttonDelivery.getStyleClass().add("success");
                    buttonDelivery.setText("Change Delivery Intersection");
                }
            }
        });

        // Create custom title with icon and description
        HBox titleBox = new HBox(6);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        FontIcon icon = new FontIcon(FontAwesomeSolid.PLUS);
        icon.getStyleClass().add("title-icon");

        VBox textBox = new VBox(0);
        Label titleLabel = new Label("Deliveries");
        titleLabel.getStyleClass().add("title-text");
        Label descLabel = new Label("Add New Deliveries");
        descLabel.getStyleClass().addAll("description-text", "text-muted");
        textBox.getChildren().addAll(titleLabel, descLabel);

        titleBox.getChildren().addAll(icon, textBox);

        // Create collapsible TitledPane
        titledPane = new TitledPane();
        titledPane.setGraphic(titleBox);
        titledPane.setText(null);
        titledPane.setContent(addDeliveryBox);
        titledPane.setExpanded(true);
        titledPane.setCollapsible(true);

        getChildren().add(titledPane);

        getChildren().addAll(addDeliveryBox);
    }

}
