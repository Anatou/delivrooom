package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier;

import atlantafx.base.controls.Spacer;
import fr.delivrooom.application.model.Courier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

public class CourierListItem extends ListCell<Courier> {

    private final Consumer<Courier> onDelete;
    private final Consumer<Courier> onCalculate;

    public CourierListItem(Consumer<Courier> onDelete, Consumer<Courier> onCalculate) {
        this.onDelete = onDelete;
        this.onCalculate = onCalculate;
    }

    @Override
    public void updateItem(Courier courier, boolean empty) {
        super.updateItem(courier, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            HBox box = new HBox(5);

            setPadding(new Insets(2, 5, 2, 5));
            setAlignment(Pos.CENTER_LEFT);
            setPrefHeight(CouriersList.COURIER_ITEM_HEIGHT);

            // Courier label
            Label courierLabel = new Label("Courier " + courier.getId());
            courierLabel.getStyleClass().add("text");

            // Calculate button
            Button calculateBtn = new Button();
            calculateBtn.setMinHeight(0);
            calculateBtn.setGraphic(new FontIcon(FontAwesomeSolid.PLAY));
            calculateBtn.getStyleClass().addAll("button-icon", "success");
            calculateBtn.setOnAction(e -> onCalculate.accept(courier));
            calculateBtn.setTooltip(new javafx.scene.control.Tooltip("Calculate route"));

            // Delete button
            Button deleteBtn = new Button();
            deleteBtn.setMinHeight(0);
            deleteBtn.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
            deleteBtn.getStyleClass().addAll("button-icon", "danger");
            deleteBtn.setOnAction(e -> onDelete.accept(courier));
            deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete courier"));

            box.setAlignment(Pos.CENTER_LEFT);
            box.setPrefHeight(CouriersList.COURIER_ITEM_HEIGHT - 4);
            box.getChildren().addAll(courierLabel, new Spacer(), calculateBtn, deleteBtn);
            setGraphic(box);
        }
    }


}
