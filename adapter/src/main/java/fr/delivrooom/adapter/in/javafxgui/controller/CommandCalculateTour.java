package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.TourSolution;

/**
 * Command to calculate the optimal tour for all deliveries.
 * Supports undo by restoring the previous tour solution.
 */
public class CommandCalculateTour implements Command {

    private final AppController controller;
    private TourSolution previousTourSolution;

    public CommandCalculateTour(AppController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        previousTourSolution = controller.tourSolutionProperty().getValue();
        controller.doCalculateTour();
    }

    @Override
    public void undo() {
        controller.doRestoreTourSolution(previousTourSolution);
    }
}
