package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.*;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Command to load a tour solution from a file.
 * This command restores a previously saved application state, including the city map,
 * deliveries, couriers, and their calculated tours.
 */
public class CommandLoadTourSolution implements Command {

    private final AppController controller;
    private final State sourceState;

    private double previousProgress;
    private final CityMap sourceCityMap;
    private final DeliveriesDemand sourceDeliveriesDemand;

    private HashMap<Courier, Pair<TourSolution, DeliveriesDemand>> previousCouriers;

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
    public CommandLoadTourSolution(AppController controller, State sourceState, CityMap sourceCityMap, DeliveriesDemand sourceDeliveriesDemand, String filename) {
        this.controller = controller;
        this.sourceState = sourceState;
        this.sourceCityMap = sourceCityMap;
        this.sourceDeliveriesDemand = sourceDeliveriesDemand;
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

        previousCouriers = new HashMap<>();
        for (Courier courier : controller.couriers) {
            previousCouriers.put(courier, new Pair<>(courier.getTourSolution(), courier.getDeliveriesDemand()));
        }

        previousProgress = controller.tourCalculationProgress.get();
        controller.doRestoreCityMap(tourSolutionSerialiser.cityMap());
        controller.doRestoreDeliveriesDemand(tourSolutionSerialiser.demand());
        controller.doRestoreCouriers(tourSolutionSerialiser.couriersAndSolutions());
        controller.tourCalculationProgress.set(previousProgress);
        controller.deliveriesDemand.invalidate();


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

        for (Map.Entry<Courier, Pair<TourSolution, DeliveriesDemand>> entry : previousCouriers.entrySet()) {
            entry.getKey().setTourSolution(entry.getValue().getKey());
            entry.getKey().setDeliveriesDemand(entry.getValue().getValue());
        }
        controller.couriersProperty().setAll(previousCouriers.keySet());
        previousCouriers.clear();
        controller.deliveriesDemand.invalidate();

        controller.tourCalculationProgress.set(previousProgress);
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
