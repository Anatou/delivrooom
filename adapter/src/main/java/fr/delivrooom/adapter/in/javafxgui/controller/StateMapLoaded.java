package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.*;

import java.net.URL;
import java.util.List;

/**
 * Map loaded state - a map has been loaded.
 * Allows opening a new map file or loading deliveries.
 */
public record StateMapLoaded(AppController controller) implements State {

    @Override
    public CommandResult createOpenMapCommand(URL url) {
        return CommandResult.success(new CommandLoadMap(controller, url, this));
    }

    @Override
    public CommandResult createOpenDeliveriesCommand(URL url) {
        return CommandResult.success(new CommandLoadDeliveries(controller, url, this));
    }

    @Override
    public CommandResult createAddDeliveryCommand(Delivery delivery) {
        return CommandResult.error("No deliveries loaded",
                "Please load deliveries first before adding a delivery.");
    }

    @Override
    public CommandResult createRemoveDeliveryCommand(Delivery delivery) {
        return CommandResult.error("No deliveries loaded",
                "Please load deliveries first before removing a delivery.");
    }

    @Override
    public void selectIntersection(Intersection intersection) {
        controller.showError("Not in selection mode",
                "Please click 'Select Intersection' button first to enter selection mode.");
    }

    @Override
    public void requestIntersectionSelection() {
        controller.showError("No map loaded", "Please load a map file first before selecting an intersection.");
    }

    @Override
    public void requestCalculateTour(Courier courier) {
        controller.showError("Cannot calculate tour", "Please load deliveries first before calculating the tour.");
    }

    @Override
    public CommandResult createAddCourierCommand(Courier courier) {
        return CommandResult.success(new CommandAddCourier(controller, courier));
    }

    @Override
    public CommandResult createRemoveCourierCommand(Courier courier) {
        return CommandResult.success(new ReverseCommand(new CommandAddCourier(controller, courier)));
    }

    @Override
    public CommandResult createAssignCourierCommand(Delivery delivery, Courier courier) {
        return CommandResult.error("No deliveries loaded",
                "Please load deliveries first before assigning couriers.");
    }

    @Override
    public String getStateName() {
        return "MapLoadedState";
    }

    /**
     * In this state, saving a tour is not permitted because no tour has been calculated.
     * Displays an error message to the user.
     *
     * @param filename The filename to save to (unused).
     */
    public void saveTour(String filename) {
        controller.showError("Unable to save tour", "No tour has been calculated yet.");
    }

    /**
     * Creates a command to load a tour solution from a file.
     *
     * @param sourceCityMap          The city map before loading, for undo.
     * @param sourceDeliveriesDemand The deliveries demand before loading, for undo.
     * @param sourceCouriers         The list of couriers before loading, for undo.
     * @param filename               The path to the file to load the tour solution from.
     * @return A {@link CommandResult} containing the load command or an error.
     */
    public CommandResult createLoadTourCommand(CityMap sourceCityMap, DeliveriesDemand sourceDeliveriesDemand, List<Courier> sourceCouriers, String filename) {
        return CommandResult.success(new CommandLoadTourSolution(controller, this, sourceCityMap, sourceDeliveriesDemand, sourceCouriers, filename));
    }
}
