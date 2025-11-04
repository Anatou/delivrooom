package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.DeliveriesDemand;

import java.net.URL;
import java.util.Map;

/**
 * Command to load a deliveries demand file.
 * Supports undo by restoring the previous deliveries state and courier assignments.
 */
public class CommandLoadDeliveries implements Command {

    private final AppController controller;
    private final URL deliveriesUrl;
    private final State sourceState;
    private DeliveriesDemand previousDeliveries;
    private Map<Courier, DeliveriesDemand> previousCourierDeliveries;

    /**
     * Creates a command to load a deliveries demand file.
     *
     * @param controller    The main application controller.
     * @param deliveriesUrl The URL of the deliveries file to load.
     * @param sourceState   The state from which the command was created, for undo purposes.
     */
    public CommandLoadDeliveries(AppController controller, URL deliveriesUrl, State sourceState) {
        this.controller = controller;
        this.deliveriesUrl = deliveriesUrl;
        this.sourceState = sourceState;
    }

    /**
     * Executes the command, loading the deliveries demand from the specified URL.
     * It saves the previous deliveries state for undo and transitions to the `StateDeliveriesLoaded`.
     */
    @Override
    public void execute() {
        try {
            previousDeliveries = controller.deliveriesDemandProperty().getValue();
            previousCourierDeliveries = controller.doCaptureCourierDeliveries();
            controller.doLoadDeliveriesFile(deliveriesUrl);
            controller.transitionToState(new StateDeliveriesLoaded(controller));
        } catch (Exception e) {
            controller.showError("Error loading deliveries", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Undoes the command, restoring the previous deliveries demand and courier assignments.
     * It also reverts the application state to the source state.
     */
    @Override
    public void undo() {
        try {
            controller.doRestoreDeliveriesDemand(previousDeliveries);
            controller.doRestoreCourierDeliveries(previousCourierDeliveries);
            controller.transitionToState(sourceState);
        } catch (Exception e) {
            controller.showError("Error undoing deliveries load", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getStringDescription() {
        return "Load Deliveries " + deliveriesUrl.getFile();
    }

    @Override
    public String getStringReversedDescription() {
        return "Unload Deliveries " + deliveriesUrl.getFile();
    }
}
