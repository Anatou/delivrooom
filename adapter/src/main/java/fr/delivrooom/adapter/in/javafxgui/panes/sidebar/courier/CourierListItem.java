package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier;
import fr.delivrooom.application.model.Courier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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

            setPadding(new Insets(5));
            setAlignment(Pos.CENTER_LEFT);
            getStyleClass().add("courier-list-item");

            // Courier label
            Label courierLabel = new Label("Courier " + courier.getId());
            courierLabel.getStyleClass().add("text");

            // Spacer
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Calculate button
            Button calculateBtn = new Button();
            calculateBtn.setGraphic(new FontIcon(FontAwesomeSolid.PLAY));
            calculateBtn.getStyleClass().addAll("button-icon", "success");
            calculateBtn.setOnAction(e -> onCalculate.accept(courier));
            calculateBtn.setTooltip(new javafx.scene.control.Tooltip("Calculate route"));

            // Delete button
            Button deleteBtn = new Button();
            deleteBtn.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
            deleteBtn.getStyleClass().addAll("button-icon", "danger");
            deleteBtn.setOnAction(e -> onDelete.accept(courier));
            deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete courier"));

            box.getChildren().addAll(courierLabel, spacer, calculateBtn, deleteBtn);
            setGraphic(box);
        }
    }


}
