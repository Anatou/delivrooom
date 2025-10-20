package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.command.AddDeliveryCommand;
import fr.delivrooom.adapter.in.javafxgui.command.RemoveDeliveryCommand;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
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

    private final AppController controller;
    private final TitledPane titledPane;

    public TestSection(AppController controller) {
        super(0);
        this.controller = controller;

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

        // Add test delivery button
        Button addDeliveryBtn = new Button("Add Test Delivery");
        addDeliveryBtn.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        addDeliveryBtn.setMaxWidth(Double.MAX_VALUE);
        addDeliveryBtn.getStyleClass().add("success");
        addDeliveryBtn.setOnAction(e -> handleAddTestDelivery());

        // Remove test delivery button
        Button removeDeliveryBtn = new Button("Remove Test Delivery");
        removeDeliveryBtn.setGraphic(new FontIcon(FontAwesomeSolid.MINUS));
        removeDeliveryBtn.setMaxWidth(Double.MAX_VALUE);
        removeDeliveryBtn.getStyleClass().add("danger");
        removeDeliveryBtn.setOnAction(e -> handleRemoveTestDelivery());

        container.getChildren().addAll(
                stateLabel,
                nameLabel,
                getNameBtn,
                addDeliveryBtn,
                removeDeliveryBtn
        );

        return container;
    }

    private void handleAddTestDelivery() {
        Intersection takeoutIntersection = new Intersection(99, 45.550404, 4.8744674);
        Intersection deliveryIntersection = new Intersection(100, 45.770404, 4.8744674);
        Delivery addedDelivery = new Delivery(takeoutIntersection, deliveryIntersection, 5, 5);
        AddDeliveryCommand addDeliveryCommand = new AddDeliveryCommand(controller, addedDelivery);
        controller.getCommandManager().executeCommand(addDeliveryCommand);
    }

    private void handleRemoveTestDelivery() {
        if (controller.getDeliveriesDemand() != null) {
            Delivery delivery = controller.getDeliveriesDemand().getDeliveryByIds(99, 100);
            if (delivery != null) {
                RemoveDeliveryCommand removeDeliveryCommand = new RemoveDeliveryCommand(controller, delivery);
                controller.getCommandManager().executeCommand(removeDeliveryCommand);
            }
        }
    }

    public boolean isExpanded() {
        return titledPane.isExpanded();
    }

    public void setExpanded(boolean expanded) {
        titledPane.setExpanded(expanded);
    }
}
