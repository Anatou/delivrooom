package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery;

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

    private final DeliveriesList deliveriesList;
    private final TitledPane titledPane;

    /**
     * Constructs the DeliveriesSection, a collapsible pane containing the list of deliveries.
     */
    public DeliveriesSection() {
        super(0);
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

    /**
     * Gets the underlying list of deliveries.
     *
     * @return The {@link DeliveriesList} instance.
     */
    public DeliveriesList getDeliveriesList() {
        return deliveriesList;
    }

    /**
     * Checks if the deliveries section is currently expanded.
     *
     * @return true if the pane is expanded, false otherwise.
     */
    public boolean isExpanded() {
        return titledPane.isExpanded();
    }

    /**
     * Sets the expanded state of the deliveries section.
     *
     * @param expanded true to expand the pane, false to collapse it.
     */
    public void setExpanded(boolean expanded) {
        titledPane.setExpanded(expanded);
    }

}
