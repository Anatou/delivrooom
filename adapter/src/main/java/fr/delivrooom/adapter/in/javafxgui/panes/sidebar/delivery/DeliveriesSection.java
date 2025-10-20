package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Collapsible section containing the list of deliveries.
 */
public class DeliveriesSection extends VBox {

    private final AppController controller;
    private final DeliveriesList deliveriesList;
    private final TitledPane titledPane;

    public DeliveriesSection(AppController controller) {
        super(0);
        // no border
        this.controller = controller;

        // Create the deliveries list
        deliveriesList = new DeliveriesList();

        // Create custom title with icon and description
        HBox titleBox = new HBox(6);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        FontIcon icon = new FontIcon(FontAwesomeSolid.TRUCK_MOVING);
        icon.getStyleClass().add("title-icon");

        VBox textBox = new VBox(0);
        Label titleLabel = new Label("Deliveries");
        titleLabel.getStyleClass().add("title-text");
        Label descLabel = new Label("Assign deliveries to couriers");
        descLabel.getStyleClass().addAll("description-text", "text-muted");
        textBox.getChildren().addAll(titleLabel, descLabel);

        titleBox.getChildren().addAll(icon, textBox);

        // Create collapsible TitledPane
        titledPane = new TitledPane();
        titledPane.setGraphic(titleBox);
        titledPane.setText(null);
        titledPane.setContent(deliveriesList);
        titledPane.setExpanded(true);
        titledPane.setCollapsible(true);

        getChildren().add(titledPane);
    }

    public DeliveriesList getDeliveriesList() {
        return deliveriesList;
    }

    public boolean isExpanded() {
        return titledPane.isExpanded();
    }

    public void setExpanded(boolean expanded) {
        titledPane.setExpanded(expanded);
    }

    /**
     * Refresh the deliveries list from the controller's data.
     */
    public void refreshDeliveries() {
        if (controller.getDeliveriesDemand() != null) {
            deliveriesList.setDeliveries(controller.getDeliveriesDemand().deliveries());
        } else {
            deliveriesList.clearDeliveries();
        }
    }
}
