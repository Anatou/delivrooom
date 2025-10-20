package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Collapsible section containing the list of couriers.
 */
public class CouriersSection extends VBox {

    private final AppController controller;
    private final CouriersList couriersList;
    private final TitledPane titledPane;

    public CouriersSection(AppController controller) {
        super(0);
        this.controller = controller;

        // Create the couriers list
        couriersList = new CouriersList();

        // Create custom title with icon and description
        HBox titleBox = new HBox(6);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        FontIcon icon = new FontIcon(FontAwesomeSolid.USERS);
        icon.getStyleClass().add("title-icon");

        VBox textBox = new VBox(0);
        Label titleLabel = new Label("Couriers");
        titleLabel.getStyleClass().add("title-text");
        Label descLabel = new Label("Manage delivery couriers");
        descLabel.getStyleClass().addAll("description-text", "text-muted");
        textBox.getChildren().addAll(titleLabel, descLabel);

        titleBox.getChildren().addAll(icon, textBox);

        // Create collapsible TitledPane
        titledPane = new TitledPane();
        titledPane.setGraphic(titleBox);
        titledPane.setText(null);
        titledPane.setContent(couriersList);
        titledPane.setExpanded(true);
        titledPane.setCollapsible(true);

        getChildren().add(titledPane);
    }

    public CouriersList getCouriersList() {
        return couriersList;
    }

    public boolean isExpanded() {
        return titledPane.isExpanded();
    }

    public void setExpanded(boolean expanded) {
        titledPane.setExpanded(expanded);
    }
}
