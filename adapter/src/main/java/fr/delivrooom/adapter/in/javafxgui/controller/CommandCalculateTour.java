package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.TourSolution;

import java.util.HashMap;
import java.util.Map;

/**
 * Command to calculate all couriers' tours.
 */
public class CommandCalculateTour implements Command {

    private final AppController controller;
    private final HashMap<Courier, TourSolution> solutions;
    private final boolean singleCourier;

    private final HashMap<Courier, TourSolution> previousTourSolutions = new HashMap<>();

    public CommandCalculateTour(AppController controller, HashMap<Courier, TourSolution> solutions, boolean singleCourier) {
        this.controller = controller;
        this.solutions = solutions;
        this.singleCourier = singleCourier;
    }

    @Override
    public void execute() {
        previousTourSolutions.clear();
        for (Courier courier : controller.couriersProperty().getValue()) {
            previousTourSolutions.put(courier, courier.getTourSolution());
            if (!singleCourier) courier.deleteTourSolution();
        }
        for (Map.Entry<Courier, TourSolution> entry : solutions.entrySet()) {
            entry.getKey().setTourSolution(entry.getValue());
        }
        controller.invalidateCouriers();
    }

    @Override
    public void undo() {
        if (!singleCourier) {
            for (Courier courier : controller.couriersProperty().getValue()) {
                courier.deleteTourSolution();
            }
        }
        for (Map.Entry<Courier, TourSolution> entry : previousTourSolutions.entrySet()) {
            entry.getKey().setTourSolution(entry.getValue());
        }
        controller.invalidateCouriers();
    }

    @Override
    public String getStringDescription() {
        if (singleCourier) {
            return "Calculate tour for courier " + solutions.keySet().iterator().next().getId();
        }
        return "Calculate tour for all couriers";
    }

    @Override
    public String getStringReversedDescription() {
        if (singleCourier) {
            return "Uncalculate tour for courier " + solutions.keySet().iterator().next().getId();
        }
        return "Uncalculate tour for all couriers";
    }
}
