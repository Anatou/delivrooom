
package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Section for test buttons and development utilities.
 */
public class TestSection extends VBox {

    private final TitledPane titledPane;
    private AppController controller;

    /**
     * Constructs the TestSection, a collapsible pane with debugging and testing utilities.
     */
    public TestSection() {
        super(0);

        getStyleClass().add("test-section");

        // Create custom title with icon and description
        HBox titleBox = new HBox(6);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        FontIcon icon = new FontIcon(FontAwesomeSolid.FLASK);
        icon.getStyleClass().add("title-icon");

        VBox textBox = new VBox(0);
        Label titleLabel = new Label("Test & Debug");
        titleLabel.getStyleClass().add("title-text");
        Label descLabel = new Label("Development utilities");
        descLabel.getStyleClass().addAll("description-text", "text-muted");
        textBox.getChildren().addAll(titleLabel, descLabel);

        titleBox.getChildren().addAll(icon, textBox);

        // Create collapsible TitledPane
        VBox content = createTestContent();
        titledPane = new TitledPane();
        titledPane.setGraphic(titleBox);
        titledPane.setText(null);
        titledPane.setContent(content);
        titledPane.setExpanded(false); // Collapsed by default
        titledPane.setCollapsible(true);

        getChildren().add(titledPane);
    }

    private VBox createTestContent() {
        this.controller = AppController.getController();

        VBox container = new VBox(10);
        container.setPadding(new Insets(10, 0, 0, 0));

        // State display
        Label stateLabel = new Label("State: " + controller.getState().getClass().getSimpleName());
        stateLabel.getStyleClass().add("text-muted");
        controller.stateProperty().addListener((obs, oldState, newState) -> {
            stateLabel.setText("State: " + newState.getClass().getSimpleName());
        });

        // Get Name test button
        Label nameLabel = new Label("Name: ");
        nameLabel.getStyleClass().add("text-muted");

        Button getNameBtn = new Button("Get Name");
        getNameBtn.setGraphic(new FontIcon(FontAwesomeSolid.USER));
        getNameBtn.setMaxWidth(Double.MAX_VALUE);
        getNameBtn.setOnAction(e -> {
            String name = JavaFXApp.getNameUseCase().getName();
            nameLabel.setText("Name: " + name);
        });

        /*
        // Add test delivery button
        Button addDeliveryBtn = new Button("Add Test Delivery");
        addDeliveryBtn.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        addDeliveryBtn.setMaxWidth(Double.MAX_VALUE);
        addDeliveryBtn.getStyleClass().add("success");
        addDeliveryBtn.setOnAction(e -> handleAddTestDelivery());

        Label labelTakeout = new Label("Selected TakeOut Intersection: ");
        labelTakeout.setId("labelTakeout");
        Button buttonTakeout = new Button("Select TakeOut Intersection");
        Label labelDelivery = new Label("Selected Delivery Intersection: ");
        labelDelivery.setId("labelDelivery");
        Button buttonDelivery = new Button("Select Delivery Intersection");
        Button buttonConfirmAddDelivery = new Button("Confirm");

        buttonTakeout.setOnAction(e -> {
            handleTakeoutSelection("add");


        });
        buttonDelivery.setOnAction(e -> {
            handleDeliverySelection("add");

        });


        Label addTitle = new Label("Add Delivery");
        addTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        VBox addDeliveryBox = new VBox(8, addTitle, addDeliveryBtn,
                new Separator(),
                labelTakeout, buttonTakeout,
                labelDelivery, buttonDelivery,
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


        // Remove test delivery button
        Button removeDeliveryBtn = new Button("Remove Test Delivery");
        removeDeliveryBtn.setGraphic(new FontIcon(FontAwesomeSolid.MINUS));
        removeDeliveryBtn.setMaxWidth(Double.MAX_VALUE);
        removeDeliveryBtn.getStyleClass().add("danger");
        removeDeliveryBtn.setOnAction(e -> handleRemoveTestDelivery());

        Label labelTakeout2 = new Label("Selected TakeOut Intersection: ");
        labelTakeout2.setId("labelTakeout2");
        Button buttonTakeout2 = new Button("Select TakeOut Intersection");
        Label labelDelivery2 = new Label("Selected Delivery Intersection: ");
        labelDelivery2.setId("labelDelivery2");
        Button buttonDelivery2 = new Button("Select Delivery Intersection");
        Button buttonConfirmRemoveDelivery = new Button("Confirm");
        Label removeTitle = new Label("Remove Delivery");
        removeTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        buttonTakeout2.setOnAction(e -> {
            handleTakeoutSelection("remove");
        });
        buttonDelivery2.setOnAction(e -> {
            handleDeliverySelection("remove");
        });
        VBox removeDeliveryBox = new VBox(8, removeTitle, removeDeliveryBtn,
                new Separator(),
                labelTakeout2, buttonTakeout2,
                labelDelivery2, buttonDelivery2,
                buttonConfirmRemoveDelivery
        );
        removeDeliveryBox.setPadding(new Insets(10));
        removeDeliveryBox.setId("removeDeliveryBox");
        removeDeliveryBox.setAlignment(Pos.CENTER_LEFT);
        removeDeliveryBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        removeDeliveryBox.setVisible(false);
        removeDeliveryBox.setManaged(false);
        buttonConfirmRemoveDelivery.setOnAction(e -> {
            handleConfirmRemoveDelivery();
            removeDeliveryBox.setVisible(false);
            removeDeliveryBox.setManaged(false);

        });

         */


        /*container.getChildren().addAll(
                stateLabel,
                nameLabel,
                getNameBtn,
                addDeliveryBtn,
                addDeliveryBox,
                removeDeliveryBtn,
                removeDeliveryBox
        );*/
        container.getChildren().addAll(
                stateLabel,
                nameLabel,
                getNameBtn
        );

        return container;
    }
    /*

    private void handleConfirmAddDelivery(){


        Delivery addedDelivery = new Delivery(this.takeout, this.delivery, 5, 5);
        AddDeliveryCommand addDeliveryCommand = new AddDeliveryCommand(controller, addedDelivery);
        controller.getCommandManager().executeCommand(addDeliveryCommand);

        //controller.updateMapCanvas();
    }
    private void handleConfirmRemoveDelivery(){
        System.out.println("takeout : "+takeout.getId()+ " delivery : "+delivery.getId());
        Delivery removedDelivery = controller.getDeliveriesDemand().getDeliveryByIds((int) takeout.getId(), (int) delivery.getId());
        RemoveDeliveryCommand removeDeliveryCommand = new RemoveDeliveryCommand(controller, removedDelivery);
        if (removedDelivery != null) {
            controller.getCommandManager().executeCommand(removeDeliveryCommand);
        }else{
            System.out.println("Delivery n'existe pas");
        }
        //controller.updateMapCanvas();
    }

    private void handleTakeoutSelection(String mode){
        this.intersectionIsSelected = false;
        if ( mode == "add") {
            controller.handleRequestIntersectionSelection();
            controller.setSidebarWaitingFor("addTakeout", this);

            //takeoutId = controller.getMapCanvas().updateAddLayer();
        }else{
            controller.handleRequestIntersectionSelection();
            controller.setSidebarWaitingFor("removeTakeout", this);
            //takeoutId = controller.getMapCanvas().updateRemoveLayer();
        }
    }
    private void handleDeliverySelection(String mode){
        if ( mode == "add") {
            controller.handleRequestIntersectionSelection();
            controller.setSidebarWaitingFor("addDelivery", this);
            //deliveryId = controller.getMapCanvas().updateAddLayer();
        }else{
            controller.handleRequestIntersectionSelection();
            controller.setSidebarWaitingFor("removeDelivery", this);
            //deliveryId = controller.getMapCanvas().updateRemoveLayer();
        }


    }

    private void handleAddTestDelivery() {
        AppController controller = AppController.getController();

        VBox contentBox = (VBox) titledPane.getContent();
        VBox addDeliveryBox = (VBox) contentBox.lookup("#addDeliveryBox");
        addDeliveryBox.setVisible(true);
        addDeliveryBox.setManaged(true);

        Label labelDelivery = (Label) addDeliveryBox.lookup("#labelDelivery");
        labelDelivery.setText("Selected Delivery Intersection: ");
        Label labelTakeout = (Label) addDeliveryBox.lookup("#labelTakeout");
        labelTakeout.setText("Selected TakeOut Intersection: ");




    }

    private void handleRemoveTestDelivery() {
        AppController controller = AppController.getController();
        VBox contentBox = (VBox) titledPane.getContent();
        VBox removeDeliveryBox = (VBox) contentBox.lookup("#removeDeliveryBox");
        removeDeliveryBox.setVisible(true);
        removeDeliveryBox.setManaged(true);

        Label labelDelivery2 = (Label) removeDeliveryBox.lookup("#labelDelivery2");
        labelDelivery2.setText("Selected Delivery Intersection: ");
        Label labelTakeout2 = (Label) removeDeliveryBox.lookup("#labelTakeout2");
        labelTakeout2.setText("Selected TakeOut Intersection: ");


    }*/

    /**
     * Checks if the test section is currently expanded.
     *
     * @return true if the pane is expanded, false otherwise.
     */
    public boolean isExpanded() {
        return titledPane.isExpanded();
    }

    /**
     * Sets the expanded state of the test section.
     *
     * @param expanded true to expand the pane, false to collapse it.
     */
    public void setExpanded(boolean expanded) {
        titledPane.setExpanded(expanded);
    }
/*
    public void selectIntersection(Intersection intersection) {
        if (intersection == null) {
            //selectedIntersectionLabel.setText("No intersection selected");
        } else {
            VBox contentBox = (VBox) titledPane.getContent();
            VBox addDeliveryBox = (VBox) contentBox.lookup("#addDeliveryBox");
            VBox removeDeliveryBox = (VBox) contentBox.lookup("#removeDeliveryBox");




            if (controller.getSidebarWaitingFor().equals("addTakeout")){
                addDeliveryBox.setVisible(true);
                addDeliveryBox.setManaged(true);
                this.takeout = intersection;
                Label labelTakeout = (Label) addDeliveryBox.lookup("#labelTakeout");
                labelTakeout.setText("Selected Takeout Intersection: "+ this.takeout.getId());
            } else if (controller.getSidebarWaitingFor().equals("addDelivery")) {
                addDeliveryBox.setVisible(true);
                addDeliveryBox.setManaged(true);
                this.delivery = intersection;
                Label labelDelivery = (Label) addDeliveryBox.lookup("#labelDelivery");
                labelDelivery.setText("Selected Delivery Intersection: "+ this.delivery.getId());
            } else if (controller.getSidebarWaitingFor().equals("removeTakeout")) {
                removeDeliveryBox.setVisible(true);
                removeDeliveryBox.setManaged(true);
                this.takeout = intersection;
                Label labelTakeout = (Label) removeDeliveryBox.lookup("#labelTakeout2");
                labelTakeout.setText("Selected Takeout Intersection: "+ this.takeout.getId());
            }else {
                removeDeliveryBox.setVisible(true);
                removeDeliveryBox.setManaged(true);
                this.delivery = intersection;
                Label labelDelivery = (Label) removeDeliveryBox.lookup("#labelDelivery2");
                labelDelivery.setText("Selected Delivery Intersection: "+ this.delivery.getId());
            }
            //selectedIntersectionLabel.setText("Selected: ID " + intersection.getId());


            // Reset waiting state
            controller.setSidebarWaitingFor(null, null);
        }
    }*/
}
