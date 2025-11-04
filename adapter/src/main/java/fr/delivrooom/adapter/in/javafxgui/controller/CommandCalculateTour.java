package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.TourSolution;

import java.util.HashMap;

/**
 * Command to calculate all couriers' tours.
 */
public class CommandCalculateTour implements Command {

    private final AppController controller;

    private final HashMap<Courier, TourSolution> previousTourSolutions = new HashMap<>();

    public CommandCalculateTour(AppController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        previousTourSolutions.clear();
        for (Courier courier : controller.couriersProperty().getValue()) {
            previousTourSolutions.put(courier, courier.getTourSolution());
            courier.deleteTourSolution();
        }
        controller.doCalculateTour();
    }

    @Override
    public void undo() {
        for (Courier courier : previousTourSolutions.keySet()) {
            courier.setTourSolution(previousTourSolutions.get(courier));
        }
        controller.invalidateCouriers();
    }

    @Override
    public String getStringDescription() {
        return "Calculate tour for all couriers";
    }

    @Override
    public String getStringReversedDescription() {
        return "Uncalculate tour for all couriers";
    }
}
