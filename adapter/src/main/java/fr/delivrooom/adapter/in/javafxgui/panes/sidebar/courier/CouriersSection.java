package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier;

import atlantafx.base.controls.Spacer;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Courier;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Collapsible section containing the list of couriers.
 */
public class CouriersSection extends VBox {

    private final VBox content = new VBox();
    private final CouriersList couriersList;
    private final TitledPane titledPane;

    public CouriersSection() {
        super(0);

        // Create the couriers list
        couriersList = new CouriersList();


        // Create courier controls
        // Bulk creation controls at the top
        HBox bulkCreateBox = new HBox(5);
        bulkCreateBox.setAlignment(Pos.CENTER_LEFT);
        bulkCreateBox.setPadding(new Insets(5));

        Spinner<Integer> courierCountSpinner = new Spinner<>(1, 20, 3);
        courierCountSpinner.setEditable(true);
        courierCountSpinner.setPrefWidth(80);

        Button bulkCreateBtn = new Button("Create");
        bulkCreateBtn.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        bulkCreateBtn.setOnAction(e -> {
            int count = courierCountSpinner.getValue();
            for (int i = 0; i < count; i++) {
                addCourier();
            }
        });

        bulkCreateBox.getChildren().addAll(courierCountSpinner, bulkCreateBtn);

        // Add new courier button at the bottom
        Button addCourierBtn = new Button("New Courier");
        addCourierBtn.setGraphic(new FontIcon(FontAwesomeSolid.USER_PLUS));
        addCourierBtn.setMaxWidth(Double.MAX_VALUE);
        addCourierBtn.getStyleClass().add("accent");
        addCourierBtn.setOnAction(e -> addCourier());

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

        content.getChildren().addAll(bulkCreateBox, couriersList, new Spacer(5, Orientation.VERTICAL), addCourierBtn);

        // Create collapsible TitledPane
        titledPane = new TitledPane();
        titledPane.setGraphic(titleBox);
        titledPane.setText(null);
        titledPane.setContent(content);
        titledPane.setExpanded(true);
        titledPane.setCollapsible(true);

        getChildren().add(titledPane);
    }

    private int getNextCourierId() {
        AtomicInteger firstAvailableId = new AtomicInteger(1);
        while (AppController.getController().couriersProperty().stream().anyMatch(courier -> courier.getId() == firstAvailableId.get())) {
            firstAvailableId.incrementAndGet();
        }
        return firstAvailableId.get();
    }

    private void addCourier() {
        Courier newCourier = new Courier(getNextCourierId());
        AppController.getController().requestAddCourier(newCourier);
    }


    public boolean isExpanded() {
        return titledPane.isExpanded();
    }

    public void setExpanded(boolean expanded) {
        titledPane.setExpanded(expanded);
    }
}
