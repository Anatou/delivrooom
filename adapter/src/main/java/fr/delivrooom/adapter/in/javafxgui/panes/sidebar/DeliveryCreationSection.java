package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Section for creating new deliveries.
 */
public class DeliveryCreationSection extends VBox {

    private final AppController controller;
    private final Label selectedIntersectionLabel;

    public DeliveryCreationSection(AppController controller) {
        super(10);
        this.controller = controller;
        setPadding(new Insets(10));

        setAlignment(Pos.TOP_LEFT);

        // Section title
        Label titleLabel = new Label("New Delivery");
        titleLabel.getStyleClass().addAll("title-4");

        // Select intersection button
        Button selectIntersectionBtn = new Button("Select Intersection");
        selectIntersectionBtn.setGraphic(new FontIcon(FontAwesomeSolid.LOCATION_ARROW));
        selectIntersectionBtn.setMaxWidth(Double.MAX_VALUE);
        selectIntersectionBtn.setOnAction(e -> controller.handleRequestIntersectionSelection());

        // Selected intersection label
        selectedIntersectionLabel = new Label("No intersection selected");
        selectedIntersectionLabel.getStyleClass().add("text-muted");
        selectedIntersectionLabel.setWrapText(true);

        getChildren().addAll(titleLabel, selectIntersectionBtn, selectedIntersectionLabel);
    }

    /**
     * Update the selected intersection display.
     * Should be called by the controller when an intersection is selected.
     *
     * @param intersection The selected intersection, or null if selection was aborted
     */
    public void selectIntersection(fr.delivrooom.application.model.Intersection intersection) {
        if (intersection == null) {
            selectedIntersectionLabel.setText("No intersection selected");
        } else {
            selectedIntersectionLabel.setText("Selected: ID " + intersection.getId());
        }
    }
}
