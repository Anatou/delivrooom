package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;
import javafx.util.Pair;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Command to load a city map file.
 * Supports undo by restoring the previous map state.
 */
public class CommandLoadMap implements Command {

    private final AppController controller;
    private final URL mapUrl;

    private final State sourceState;

    private DeliveriesDemand previousDeliveries;
    private double previousProgress = 0;
    private HashMap<Courier, Pair<TourSolution, DeliveriesDemand>> previousCouriers;
    private CityMap previousMap;

    /**
     * Creates a command to load a city map file.
     *
     * @param controller  The main application controller.
     * @param mapUrl      The URL of the map file to load.
     * @param sourceState The state from which the command was created, for undo purposes.
     */
    public CommandLoadMap(AppController controller, URL mapUrl, State sourceState) {
        this.controller = controller;
        this.mapUrl = mapUrl;
        this.sourceState = sourceState;
    }

    /**
     * Executes the command, loading the city map from the specified URL.
     * It saves the previous map state for undo and transitions to the `StateMapLoaded`.
     */
    @Override
    public void execute() {
        try {
            previousDeliveries = controller.deliveriesDemandProperty().getValue();
            previousMap = controller.cityMapProperty().getValue();
            previousProgress = controller.tourCalculationProgress.get();
            previousCouriers = new HashMap<>();
            for (Courier courier : controller.couriers) {
                previousCouriers.put(courier, new Pair<>(courier.getTourSolution(), courier.getDeliveriesDemand()));
                if (courier.getDeliveriesDemand() != null) {
                    courier.getDeliveriesDemand().deliveries().clear();
                }
                if (courier.getTourSolution() != null) {
                    courier.deleteTourSolution();
                }
            }
            controller.deliveriesDemand.set(null);
            controller.tourCalculationProgress.set(0);
            controller.doRestoreCityMap(JavaFXApp.guiUseCase().getCityMap(mapUrl));
            controller.transitionToState(new StateMapLoaded(controller));
            controller.couriers.invalidate();
        } catch (Exception e) {
            controller.showError("Error loading map", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Undoes the command, restoring the previous city map.
     * It also reverts the application state to the source state.
     */
    @Override
    public void undo() {
        try {
            controller.doRestoreCityMap(previousMap);
            controller.tourCalculationProgress.set(previousProgress);
            controller.deliveriesDemand.set(previousDeliveries);
            controller.transitionToState(sourceState);
            for (Map.Entry<Courier, Pair<TourSolution, DeliveriesDemand>> entry : previousCouriers.entrySet()) {
                entry.getKey().setTourSolution(entry.getValue().getKey());
                entry.getKey().setDeliveriesDemand(entry.getValue().getValue());
            }
            previousCouriers.clear();
            controller.couriers.invalidate();
        } catch (Exception e) {
            controller.showError("Error undoing map load", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getStringDescription() {
        return "Load map " + mapUrl.toString();
    }

    @Override
    public String getStringReversedDescription() {
        return "Unload map " + mapUrl.toString();
    }
}
