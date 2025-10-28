package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery;

import fr.delivrooom.application.model.Delivery;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Represents a single delivery item in the list.
 */
public class DeliveryListItem extends HBox {

    private final DeliveryTooltip tooltip;
    private final DeliveryActionButtons actionButtons;

    public DeliveryListItem(Delivery delivery) {
        super(5);
        this.tooltip = new DeliveryTooltip(delivery, false);
        this.actionButtons = new DeliveryActionButtons(delivery, () -> {
        });

        setPadding(new Insets(8));
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("delivery-list-item");

        // Delivery icon
        FontIcon deliveryIcon = new FontIcon(FontAwesomeSolid.BOX);
        deliveryIcon.getStyleClass().add("accent");

        // Delivery info
        VBox infoBox = new VBox(2);
        Label fromLabel = new Label("From: ID " + delivery.takeoutIntersection().getId());
        fromLabel.getStyleClass().add("text-small");
        Label toLabel = new Label("To: ID " + delivery.deliveryIntersection().getId());
        toLabel.getStyleClass().add("text-small");
        infoBox.getChildren().addAll(fromLabel, toLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Info button to show tooltip
        Button infoBtn = new Button();
        infoBtn.setGraphic(new FontIcon(FontAwesomeSolid.INFO_CIRCLE));
        infoBtn.getStyleClass().addAll("button-icon");
        infoBtn.setOnAction(e -> {
            if (tooltip.isShowing()) {
                tooltip.hide();
            } else {
                tooltip.show(infoBtn);
            }
        });

        getChildren().addAll(deliveryIcon, infoBox, spacer, actionButtons, infoBtn);

        // Make the entire item clickable to show tooltip
        setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                if (tooltip.isShowing()) {
                    tooltip.hide();
                } else {
                    tooltip.show(this);
                }
            }
        });

        // Add hover effect
        setOnMouseEntered(e -> getStyleClass().add("hover"));
        setOnMouseExited(e -> getStyleClass().remove("hover"));
    }
}
