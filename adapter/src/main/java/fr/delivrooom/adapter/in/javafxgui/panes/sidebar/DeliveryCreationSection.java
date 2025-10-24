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
        super(10);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_LEFT);
        this.controller = AppController.getController();

        /*// Section title
        Label titleLabel = new Label("New Delivery");
        titleLabel.getStyleClass().addAll("title-4");

        // State display
        Label stateLabel = new Label("State: " + controller.getState().getClass().getSimpleName());
        stateLabel.getStyleClass().add("text-muted");
        controller.stateProperty().addListener((obs, oldState, newState) -> {
            stateLabel.setText("State: " + newState.getClass().getSimpleName());
        });*/


        Button buttonTakeout = new Button("Select TakeOut Intersection");
        buttonTakeout.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        buttonTakeout.setMaxWidth(Double.MAX_VALUE);
        Spinner<Integer> durationSpinnerTakeout = new Spinner<>(1,20,5);
        durationSpinnerTakeout.setEditable(true);
        durationSpinnerTakeout.setMinWidth(175);
        //durationSpinnerTakeout.setMaxWidth(Double.MAX_VALUE);
        durationSpinnerTakeout.setId("durationSpinnerTakeout");
        durationSpinnerTakeout.setPromptText("Duration in minutes");
        HBox selectIntersectionTakeout = new HBox(buttonTakeout, durationSpinnerTakeout);

        Button buttonDelivery = new Button("Select Delivery Intersection");
        buttonDelivery.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        buttonDelivery.setMaxWidth(Double.MAX_VALUE);
        Spinner<Integer> durationSpinnerDelivery = new Spinner<>(1,20,5);
        durationSpinnerDelivery.setEditable(true);
        durationSpinnerDelivery.setMinWidth(175);
        durationSpinnerDelivery.setId("durationSpinnerDelivery");
        durationSpinnerDelivery.setPromptText("Duration in minutes");
        HBox selectIntersectionDelivery = new HBox(buttonDelivery, durationSpinnerDelivery);


        Button buttonConfirmAddDelivery = new Button("Add Delivery");
        buttonConfirmAddDelivery.setGraphic(new FontIcon(FontAwesomeSolid.USER_PLUS));
        buttonConfirmAddDelivery.setMaxWidth(Double.MAX_VALUE);
        buttonConfirmAddDelivery.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        buttonConfirmAddDelivery.setStyle("-fx-background-color:#A8E4A0");

        buttonTakeout.setOnAction(e -> {
            requestedIntersectionIsTakeout = true;
            buttonTakeout.setStyle("-fx-background-color:#8FD6FA");
            controller.requestIntersectionSelection();
        });
        buttonDelivery.setOnAction(e -> {
            requestedIntersectionIsTakeout = false;
            buttonDelivery.setStyle("-fx-background-color:#8FD6FA");
            controller.requestIntersectionSelection();
        });


        Label addTitle = new Label("Add Delivery");
        addTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        this.addDeliveryBox = new VBox(8,
                selectIntersectionTakeout,
                selectIntersectionDelivery,
                buttonConfirmAddDelivery
        );
        //addDeliveryBox.setPadding(new Insets(10));
        addDeliveryBox.setAlignment(Pos.CENTER_LEFT);

        buttonConfirmAddDelivery.setOnAction(e -> {
            int tDuration;
            int dDuration;
            tDuration = durationSpinnerTakeout.getValue();
            dDuration = durationSpinnerDelivery.getValue();
            if (takeout != null && delivery != null) {

                Delivery addedDelivery = new Delivery(this.takeout, this.delivery, tDuration, dDuration);
                controller.requestAddDelivery(addedDelivery);
                buttonTakeout.setText("Select TakeOut Intersection");
                buttonDelivery.setText("Select Delivery Intersection");
                buttonDelivery.setStyle(null);
                buttonTakeout.setStyle(null);
            }
            //addDeliveryBox.setVisible(false);
            //addDeliveryBox.setManaged(false);
        });

        controller.selectedIntersectionProperty().addListener((obs, oldIntersection, newIntersection) -> {
            if (newIntersection != null) {
                if (requestedIntersectionIsTakeout) {
                    /*addDeliveryBox.setVisible(true);
                    addDeliveryBox.setManaged(true);*/
                    this.takeout = newIntersection;
                    buttonTakeout.setStyle("-fx-background-color: #CEE3A5");
                    buttonTakeout.setText("Select Another TakeOut Intersection");
                } else {
                    /*addDeliveryBox.setVisible(true);
                    addDeliveryBox.setManaged(true);*/
                    this.delivery = newIntersection;
                    buttonDelivery.setStyle("-fx-background-color:#CEE3A5");
                    buttonDelivery.setText("Select Another Delivery Intersection");
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

        getChildren().addAll(new Label(""), addDeliveryBox);
    }

}
