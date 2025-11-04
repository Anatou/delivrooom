package fr.delivrooom.adapter.in.javafxgui.panes;

import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.BottomSection;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.DeliveryCreationSection;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier.CouriersSection;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery.DeliveriesSection;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * Main sidebar containing all sections for the application.
 * Organized into four main vertical sections:
 * 1. Delivery Creation
 * 2. Couriers Management (collapsible)
 * 3. Deliveries List (collapsible)
 * 4. Test & Debug (collapsible)
 * Plus a fixed bottom section with progress bar and GO button.
 */

/**
 * The main sidebar of the application, containing various sections for managing
 * deliveries, couriers, and other application functions.
 */
public class Sidebar extends VBox {

    // Sections
    private final DeliveryCreationSection deliveryCreationSection;
    private final CouriersSection couriersSection;
    private final DeliveriesSection deliveriesSection;
    private final BottomSection bottomSection;

    /**
     * Constructs the Sidebar and initializes all its child sections.
     */
    public Sidebar() {
        super(0);

        setMinWidth(350);
        setMaxWidth(450);
        setPrefWidth(400);

        // Load CSS stylesheet
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/sidebar.css")).toExternalForm());

        // Create all sections
        deliveryCreationSection = new DeliveryCreationSection();
        couriersSection = new CouriersSection();
        deliveriesSection = new DeliveriesSection();
        bottomSection = new BottomSection();

        // Create scrollable content area
        VBox scrollableContent = new VBox(0);
        scrollableContent.getChildren().addAll(
                deliveryCreationSection,
                getNoPaddingSeparator(),
                couriersSection,
                getNoPaddingSeparator(),
                deliveriesSection
        );

        ScrollPane scrollPane = new ScrollPane(scrollableContent);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("edge-to-edge");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Add all components to sidebar
        getChildren().addAll(scrollPane, getNoPaddingSeparator(), bottomSection);
    }

    private Separator getNoPaddingSeparator() {
        Separator separator = new Separator();
        separator.setPadding(new Insets(0, 0, 0, 0));
        return separator;
    }

}

