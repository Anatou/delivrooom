package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.TourSolution;

/**
 * Command to calculate the optimal tour for a specific courier.
 * Supports undo by restoring the previous tour solution.
 */
public class CommandCalculateCourierTour implements Command {

    private final AppController controller;
    private final Courier courier;
    private TourSolution previousTourSolution;

    public CommandCalculateCourierTour(AppController controller, Courier courier) {
        this.controller = controller;
        this.courier = courier;
    }

    @Override
    public void execute() {
        previousTourSolution = controller.tourSolutionProperty().getValue();
        controller.doCalculateTourForCourier(courier);
    }

    @Override
    public void undo() {
        controller.doRestoreTourSolution(previousTourSolution);
    }

    @Override
    public String toString() {
        return "Calculate tour for courier " + courier.getId();
    }
}
