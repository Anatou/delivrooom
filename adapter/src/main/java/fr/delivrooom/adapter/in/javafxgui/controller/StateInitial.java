package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.*;

import java.net.URL;
import java.util.List;

/**
 * Initial state - no map or deliveries loaded yet.
 * Only allows opening a map file.
 */
public record StateInitial(AppController controller) implements State {

    @Override
    public CommandResult createOpenMapCommand(URL url) {
        return CommandResult.success(new CommandLoadMap(controller, url, this));
    }

    @Override
    public CommandResult createOpenDeliveriesCommand(URL url) {
        return CommandResult.error("No map loaded",
                "Please load a map file first before loading deliveries.");
    }

    @Override
    public CommandResult createAddDeliveryCommand(Delivery delivery) {
        return CommandResult.error("No deliveries loaded",
                "Please load a map and deliveries first.");
    }

    @Override
    public CommandResult createRemoveDeliveryCommand(Delivery delivery) {
        return CommandResult.error("No deliveries loaded",
                "Please load a map and deliveries first.");
    }

    @Override
    public void selectIntersection(Intersection intersection) {
        controller.showError("Not in selection mode",
                "Please click 'Select Intersection' button first to enter selection mode.");
    }

    @Override
    public void requestIntersectionSelection() {
        controller.showError("No map loaded",
                "Please load a map file first before selecting an intersection.");
    }

    @Override
    public CommandResult createCalculateTourCommand() {
        return CommandResult.error("Cannot calculate tour",
                "No map or deliveries loaded.");
    }

    @Override
    public CommandResult createCalculateCourierTourCommand(Courier courier) {
        return CommandResult.error("Cannot calculate tour",
                "No map or deliveries loaded.");
    }

    @Override
    public CommandResult createAssignCourierCommand(Delivery delivery, Courier courier) {
        return CommandResult.error("No deliveries loaded",
                "Please load a map and deliveries first.");
    }

    @Override
    public CommandResult createAddCourierCommand(Courier courier) {
        return CommandResult.success(new CommandAddCourier(controller, courier));
    }

    @Override
    public CommandResult createRemoveCourierCommand(Courier courier) {
        return CommandResult.success(new CommandRemoveCourier(controller, courier));
    }

    @Override
    public String getStateName() {
        return "InitialState";
    }

    @Override
    public void saveTour(String filename) {
        controller.showError("Unable to save tour", "No tour has been calculated yet.");
    }

    public CommandResult createLoadTourCommand(CityMap sourceCityMap, DeliveriesDemand sourceDeliveriesDemand, List<Courier> sourceCouriers, String filename) {
        return CommandResult.success( new CommandLoadTourSolution(controller, this, sourceCityMap, sourceDeliveriesDemand, sourceCouriers, filename) );
    }
}
