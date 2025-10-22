package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier;

import fr.delivrooom.application.model.Courier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Represents a single courier item in the list.
 */
public class CourierListItem extends HBox {

    private final Courier courier;
    private final Runnable onDelete;
    private final Runnable onCalculate;

    public CourierListItem(int courierId, Runnable onDelete, Runnable onCalculate) {
        super(5);
        this.courier = new Courier(courierId, null);
        this.onDelete = onDelete;
        this.onCalculate = onCalculate;

        setPadding(new Insets(5));
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("courier-list-item");

        // Courier label
        Label courierLabel = new Label("Courier " + courierId);
        courierLabel.getStyleClass().add("text");

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Calculate button
        Button calculateBtn = new Button();
        calculateBtn.setGraphic(new FontIcon(FontAwesomeSolid.PLAY));
        calculateBtn.getStyleClass().addAll("button-icon", "success");
        calculateBtn.setOnAction(e -> onCalculate.run());
        calculateBtn.setTooltip(new javafx.scene.control.Tooltip("Calculate route"));

        // Delete button
        Button deleteBtn = new Button();
        deleteBtn.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        deleteBtn.getStyleClass().addAll("button-icon", "danger");
        deleteBtn.setOnAction(e -> onDelete.run());
        deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete courier"));

        getChildren().addAll(courierLabel, spacer, calculateBtn, deleteBtn);
    }

    public int getCourierId() {
        return courier.getId();
    }
}
