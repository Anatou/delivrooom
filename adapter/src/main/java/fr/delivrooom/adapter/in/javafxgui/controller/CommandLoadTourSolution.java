package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.*;

import java.io.IOException;
import java.util.List;

/**
 * Command to load a tour solution from a file.
 * This command restores a previously saved application state, including the city map,
 * deliveries, couriers, and their calculated tours.
 */
public class CommandLoadTourSolution implements Command {

    private final AppController controller;
    private final State sourceState;

    private final CityMap sourceCityMap;
    private final DeliveriesDemand sourceDeliveriesDemand;

    private final List<Courier> sourceCouriers;

    private final String filename;

    /**
     * Creates a command to load a tour solution.
     *
     * @param controller             The main application controller.
     * @param sourceState            The state before loading, for undo.
     * @param sourceCityMap          The city map before loading, for undo.
     * @param sourceDeliveriesDemand The deliveries demand before loading, for undo.
     * @param sourceCouriers         The list of couriers before loading, for undo.
     * @param filename               The path to the file to load the tour solution from.
     */
    public CommandLoadTourSolution(AppController controller, State sourceState, CityMap sourceCityMap, DeliveriesDemand sourceDeliveriesDemand, List<Courier> sourceCouriers, String filename) {
        this.controller = controller;
        this.sourceState = sourceState;
        this.sourceCityMap = sourceCityMap;
        this.sourceDeliveriesDemand = sourceDeliveriesDemand;
        this.sourceCouriers = sourceCouriers;
        this.filename = filename;
    }

    /**
     * Executes the command, loading the tour solution from the file and restoring the application state.
     */
    @Override
    public void execute() {
        TourSolutionSerialiserIO tourSolutionSerialiserIO = new TourSolutionSerialiserIO();
        TourSolutionSerialiser tourSolutionSerialiser;
        try {
            tourSolutionSerialiser = tourSolutionSerialiserIO.loadTourSolutionSerialization(filename);
        } catch (IOException | ClassNotFoundException e) {
            controller.showError("Tour loading failure", "Failed to load tour");
            return;
        }

        controller.doRestoreCityMap(tourSolutionSerialiser.cityMap());
        controller.doRestoreDeliveriesDemand(tourSolutionSerialiser.demand());
        controller.doRestoreCouriers(tourSolutionSerialiser.couriersAndSolutions());

        StateDeliveriesLoaded newState = new StateDeliveriesLoaded(controller);
        controller.transitionToState(newState);

    }

    /**
     * Undoes the command, restoring the application state to how it was before the tour solution was loaded.
     */
    @Override
    public void undo() {
        controller.doRestoreCityMap(sourceCityMap);
        controller.doRestoreDeliveriesDemand(sourceDeliveriesDemand);
        controller.couriersProperty().setAll(sourceCouriers);
        controller.transitionToState(sourceState);
    }

    @Override
    public String getStringDescription() {
        return "Load Solution from file " + filename;
    }

    @Override
    public String getStringReversedDescription() {
        return "Unload TourSolution from file " + filename;
    }


}
