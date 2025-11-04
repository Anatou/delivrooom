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

    public CommandLoadDeliveries(AppController controller, URL deliveriesUrl, State sourceState) {
        this.controller = controller;
        this.deliveriesUrl = deliveriesUrl;
        this.sourceState = sourceState;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Executing CommandLoadDeliveries with file: " + deliveriesUrl.getFile());
            previousDeliveries = controller.deliveriesDemandProperty().getValue();
            previousCourierDeliveries = controller.doCaptureCourierDeliveries();
            controller.doLoadDeliveriesFile(deliveriesUrl);
            controller.transitionToState(new StateDeliveriesLoaded(controller));
        } catch (Exception e) {
            controller.showError("Error loading deliveries", e.getMessage());
            e.printStackTrace();
        }
    }

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
    public String toString() {
        return "Load Deliveries " + deliveriesUrl.getFile();
    }
}
