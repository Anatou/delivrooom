package fr.delivrooom.adapter.in.javafxgui.panes;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.BottomSection;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.DeliveryCreationSection;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.TestSection;
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
public class Sidebar extends VBox {

    private final AppController controller;

    // Sections
    private final DeliveryCreationSection deliveryCreationSection;
    private final CouriersSection couriersSection;
    private final DeliveriesSection deliveriesSection;
    private final TestSection testSection;
    private final BottomSection bottomSection;

    public Sidebar(AppController controller) {
        super(0);
        this.controller = controller;

        setMinWidth(350);
        setMaxWidth(450);
        setPrefWidth(400);

        // Load CSS stylesheet
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/sidebar.css")).toExternalForm());

        // Create all sections
        deliveryCreationSection = new DeliveryCreationSection(controller);
        couriersSection = new CouriersSection(controller);
        deliveriesSection = new DeliveriesSection(controller);
        testSection = new TestSection(controller);
        bottomSection = new BottomSection(controller);

        // Create scrollable content area
        VBox scrollableContent = new VBox(0);
        scrollableContent.getChildren().addAll(
                deliveryCreationSection,
                getNoPaddingSeparator(),
                couriersSection,
                getNoPaddingSeparator(),
                deliveriesSection,
                getNoPaddingSeparator(),
                testSection
        );

        ScrollPane scrollPane = new ScrollPane(scrollableContent);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("edge-to-edge");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Add all components to sidebar
        getChildren().addAll(scrollPane, getNoPaddingSeparator(), bottomSection);

        // Listen to deliveries changes to refresh the list
        controller.stateProperty().addListener((obs, oldState, newState) -> {
            deliveriesSection.refreshDeliveries();
        });
    }

    private Separator getNoPaddingSeparator() {
        Separator separator = new Separator();
        separator.setPadding(new Insets(0, 0, 0, 0));
        return separator;
    }

    /**
     * Get the delivery creation section.
     */
    public DeliveryCreationSection getDeliveryCreationSection() {
        return deliveryCreationSection;
    }

    /**
     * Get the couriers section.
     */
    public CouriersSection getCouriersSection() {
        return couriersSection;
    }

    /**
     * Get the deliveries section.
     */
    public DeliveriesSection getDeliveriesSection() {
        return deliveriesSection;
    }

    /**
     * Get the test section.
     */
    public TestSection getTestSection() {
        return testSection;
    }

    /**
     * Get the bottom section.
     */
    public BottomSection getBottomSection() {
        return bottomSection;
    }

}
