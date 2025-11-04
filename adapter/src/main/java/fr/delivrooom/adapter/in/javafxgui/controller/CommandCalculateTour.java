package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.CouriersTourSolution;
import javafx.application.Platform;

import java.util.HashMap;

/**
 * Command to calculate all couriers' tours.
 */
public class CommandCalculateTour implements Command {

    private final AppController controller;

    public CommandCalculateTour(AppController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.doCalculateTour();
    }

    @Override
    public void undo() {
        Platform.runLater(() -> {
            System.out.println("Undo: clearing all couriers' tours.");

            controller.doRestoreTourSolution(new CouriersTourSolution(new HashMap<>()));

            var couriersList = controller.couriersProperty().getValue();
            if (couriersList != null) {
                for (var courier : couriersList) {
                    courier.deleteTourSolution();
                }
            }

            controller.invalidateCouriers();
        });
    }

    @Override
    public String toString() {
        return "Calculate tour for all couriers";
    }
}
