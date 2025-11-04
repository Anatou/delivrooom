package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.adapter.in.javafxgui.controller.State;
import fr.delivrooom.adapter.in.javafxgui.controller.StateSelectIntersection;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
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

    /**
     * Constructs the DeliveryCreationSection, initializing the form for adding new deliveries.
     */
    public DeliveryCreationSection() {
        super(0);
        //setPadding(new Insets(10));
        //setAlignment(Pos.TOP_LEFT);
        this.controller = AppController.getController();

        // Crée une infobulle (popup au survol)
        Tooltip tooltipDurationMinute = new Tooltip("Duration in minutes");


        // Create button and Sprinner : select takeout intersection and its duration
        Button buttonTakeout = new Button("Select Pickup Intersection");
        buttonTakeout.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        buttonTakeout.setMaxWidth(Double.MAX_VALUE);
        buttonTakeout.setMinWidth(250);
        Spinner<Integer> durationSpinnerTakeout = new Spinner<>(1,20,5);
        durationSpinnerTakeout.setEditable(true);
        durationSpinnerTakeout.setId("durationSpinnerTakeout");
        //durationSpinnerTakeout.setPromptText("Duration in minutes");
        durationSpinnerTakeout.setTooltip(tooltipDurationMinute);
        HBox selectIntersectionTakeout = new HBox(5,buttonTakeout, durationSpinnerTakeout);

        // Create button and Sprinner : select delivery intersection and its duration
        Button buttonDelivery = new Button("Select Deposit Intersection");
        buttonDelivery.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        buttonDelivery.setMaxWidth(Double.MAX_VALUE);
        buttonDelivery.setMinWidth(250);
        Spinner<Integer> durationSpinnerDelivery = new Spinner<>(1,20,5);
        durationSpinnerDelivery.setEditable(true);
        durationSpinnerDelivery.setId("durationSpinnerDelivery");
        //durationSpinnerDelivery.setPromptText("Duration in minutes");
        durationSpinnerDelivery.setTooltip(tooltipDurationMinute);
        HBox selectIntersectionDelivery = new HBox(5,buttonDelivery, durationSpinnerDelivery);


        // Create button Confirm the choice of the selected Delivery
        Button buttonConfirmAddDelivery = new Button("Add Delivery");
        buttonConfirmAddDelivery.setMaxWidth(Double.MAX_VALUE);
        buttonConfirmAddDelivery.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        buttonConfirmAddDelivery.getStyleClass().add("accent");

        buttonTakeout.setOnAction(e -> {
            this.takeout = null;
            requestedIntersectionIsTakeout = true;
            buttonTakeout.getStyleClass().removeAll("success","accent");
            buttonTakeout.getStyleClass().add("accent");
            buttonTakeout.setText("Select Intersection on the map");
            controller.requestIntersectionSelection();
        });
        buttonDelivery.setOnAction(e -> {
            this.delivery = null;
            requestedIntersectionIsTakeout = false;
            buttonDelivery.getStyleClass().removeAll("success","accent");
            buttonDelivery.getStyleClass().add("accent");
            buttonDelivery.setText("Select Intersection on the map");
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
                    buttonTakeout.setText("Select Pickup Intersection");
                    buttonDelivery.setText("Select Deposit Intersection");
                    buttonDelivery.getStyleClass().removeAll("success", "accent");
                    buttonTakeout.getStyleClass().removeAll("success", "accent");
                    this.takeout = null;
                    this.delivery = null;
                }else{
                controller.showError("Pickup and/or deposit Intersections null", "Select both Pickup and deposit Intersections");
                buttonTakeout.setText("Select Pickup Intersection");
                buttonDelivery.setText("Select Deposit Intersection");
                buttonDelivery.getStyleClass().removeAll("success", "accent");
                buttonTakeout.getStyleClass().removeAll("success", "accent");
                this.takeout = null;
                this.delivery = null;
            }

        });


        this.addDeliveryBox = new VBox(5,
                selectIntersectionTakeout,
                selectIntersectionDelivery,
                buttonConfirmAddDelivery
        );
        addDeliveryBox.setAlignment(Pos.CENTER_LEFT);

        controller.stateProperty().addListener((observable, oldState, newState )-> {
            State state = controller.stateProperty().getValue();
            Intersection newIntersection = controller.selectedIntersectionProperty().getValue();
            if (oldState instanceof StateSelectIntersection && !(newState instanceof StateSelectIntersection) && newIntersection == null){
                controller.showError("Invalid Intersection", "No intersection found. Select an existing intersection");

                if (requestedIntersectionIsTakeout) {
                    buttonTakeout.setText("Select Pickup Intersection");
                    buttonTakeout.getStyleClass().removeAll("success","accent");
                } else {
                    buttonDelivery.setText("Select Deposit Intersection");
                    buttonDelivery.getStyleClass().removeAll("success","accent");
                }
            }else if (oldState instanceof StateSelectIntersection && !(newState instanceof StateSelectIntersection) && newIntersection != null){
                boolean isInvalid = false;
                for (Delivery delivery : controller.deliveriesDemandProperty().getValue().deliveries()){
                    if ((newIntersection.getNormalizedX()== delivery.takeoutIntersection().getNormalizedX() && newIntersection.getNormalizedY() == delivery.takeoutIntersection().getNormalizedY())
                            ||(newIntersection.getNormalizedX()== delivery.deliveryIntersection().getNormalizedX() && newIntersection.getNormalizedY() == delivery.deliveryIntersection().getNormalizedY())){
                        isInvalid = true;

                        break;
                    }
                }
                Intersection store = controller.deliveriesDemandProperty().getValue().store();
                if (newIntersection.getNormalizedX()== store.getNormalizedX() && newIntersection.getNormalizedY() == store.getNormalizedY()) {
                    isInvalid = true;
                }

                if (requestedIntersectionIsTakeout) {
                    if (delivery!= null && delivery.getId()==newIntersection.getId()){
                        controller.showError("Invalid Intersection", "Pickup and deposit are the same. Select different Pickup and Deposit Intersections");
                        buttonTakeout.setText("Select Pickup Intersection");
                        buttonTakeout.getStyleClass().removeAll("success","accent");
                    }else {
                        if(isInvalid){
                            controller.showError("Invalid Intersection", "Intersection is already a pickup or deposit. Select a valid intersection.");
                            buttonTakeout.setText("Select Pickup Intersection");
                            buttonTakeout.getStyleClass().removeAll("success","accent");
                        }else{
                            System.out.println("selected intersection pickup : " + newIntersection.getId());
                            this.takeout = newIntersection;
                            buttonTakeout.getStyleClass().add("success");
                            buttonTakeout.setText("Change Pickup Intersection");
                        }
                    }
                } else {
                    if (takeout!= null && takeout.getId()==newIntersection.getId()){
                        controller.showError("Pickup and deposit are the same", "Select different Pickup and Deposit Intersections");
                        buttonDelivery.setText("Select Deposit Intersection");
                        buttonDelivery.getStyleClass().removeAll("success","accent");
                    }else {
                        if(isInvalid){
                            controller.showError("Invalid Intersection", "Intersection is already a pickup or deposit. Select a valid intersection.");
                            buttonDelivery.setText("Select Deposit Intersection");
                            buttonDelivery.getStyleClass().removeAll("success","accent");
                        }else {
                            System.out.println("selected intersection deposit : " + newIntersection.getId());
                            this.delivery = newIntersection;
                            buttonDelivery.getStyleClass().add("success");
                            buttonDelivery.setText("Change Deposit Intersection");
                        }
                    }
                }
            }
        });
        /*
        controller.selectedIntersectionProperty().addListener((obs) -> {
            Intersection newIntersection = controller.selectedIntersectionProperty().getValue();
            if (newIntersection != null) {
                if (requestedIntersectionIsTakeout) {
                    System.out.println("selected intersection pickup : "+newIntersection.getId());
                    this.takeout = newIntersection;
                    buttonTakeout.getStyleClass().add("success");
                    buttonTakeout.setText("Change Pickup Intersection");
                } else {
                    System.out.println("selected intersection deposit : "+newIntersection.getId());
                    this.delivery = newIntersection;
                    buttonDelivery.getStyleClass().add("success");
                    buttonDelivery.setText("Change Deposit Intersection");
                }
            }else{
                if (requestedIntersectionIsTakeout) {
                    buttonTakeout.setText("Select Pickup Intersection");
                    buttonTakeout.getStyleClass().removeAll("success","accent");
                } else {
                    buttonDelivery.setText("Select Deposit Intersection");
                    buttonDelivery.getStyleClass().removeAll("success","accent");
                }
            }
        });*/

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
