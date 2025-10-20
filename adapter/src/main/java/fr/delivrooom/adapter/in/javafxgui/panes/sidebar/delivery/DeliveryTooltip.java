package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery;

import atlantafx.base.controls.Popover;
import fr.delivrooom.application.model.Delivery;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Tooltip/Popover displaying detailed information about a delivery.
 * Can be opened from the sidebar list or from the map view.
 */
public class DeliveryTooltip extends Popover {

    private final Delivery delivery;

    public DeliveryTooltip(Delivery delivery) {
        super();
        this.delivery = delivery;

        setTitle("Delivery Details");
        setHeaderAlwaysVisible(true);
        setArrowLocation(ArrowLocation.TOP_LEFT);

        VBox content = createContent();
        setContentNode(content);
    }

    private VBox createContent() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        container.setAlignment(Pos.TOP_LEFT);
        container.setPrefWidth(300);

        // Takeout information
        VBox takeoutBox = new VBox(5);
        Label takeoutTitle = new Label("Takeout Location");
        takeoutTitle.getStyleClass().addAll("title-4");
        takeoutTitle.setGraphic(new FontIcon(FontAwesomeSolid.SHOPPING_CART));

        GridPane takeoutGrid = createInfoGrid(
                "Intersection ID:", String.valueOf(delivery.takeoutIntersection().getId()),
                "Latitude:", String.format("%.6f", delivery.takeoutIntersection().getLatitude()),
                "Longitude:", String.format("%.6f", delivery.takeoutIntersection().getLongitude()),
                "Duration:", delivery.takeoutDuration() + " min"
        );

        takeoutBox.getChildren().addAll(takeoutTitle, takeoutGrid);

        // Delivery information
        VBox deliveryBox = new VBox(5);
        Label deliveryTitle = new Label("Delivery Location");
        deliveryTitle.getStyleClass().addAll("title-4");
        deliveryTitle.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));

        GridPane deliveryGrid = createInfoGrid(
                "Intersection ID:", String.valueOf(delivery.deliveryIntersection().getId()),
                "Latitude:", String.format("%.6f", delivery.deliveryIntersection().getLatitude()),
                "Longitude:", String.format("%.6f", delivery.deliveryIntersection().getLongitude()),
                "Duration:", delivery.deliveryDuration() + " min"
        );

        deliveryBox.getChildren().addAll(deliveryTitle, deliveryGrid);

        container.getChildren().addAll(takeoutBox, deliveryBox);

        return container;
    }

    private GridPane createInfoGrid(String... labelValuePairs) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);

        for (int i = 0; i < labelValuePairs.length; i += 2) {
            Label label = new Label(labelValuePairs[i]);
            label.getStyleClass().add("text-bold");

            Label value = new Label(labelValuePairs[i + 1]);
            value.getStyleClass().add("text-muted");

            grid.add(label, 0, i / 2);
            grid.add(value, 1, i / 2);
        }

        return grid;
    }

    public Delivery getDelivery() {
        return delivery;
    }
}
