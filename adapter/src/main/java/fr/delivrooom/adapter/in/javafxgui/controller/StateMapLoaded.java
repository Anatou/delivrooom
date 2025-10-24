package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;

import java.net.URL;

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
        controller.showError("No map loaded",
                "Please load a map file first before selecting an intersection.");
    }

    @Override
    public CommandResult createCalculateTourCommand() {
        return CommandResult.error("No deliveries loaded",
                "Please load deliveries first before calculating the tour.");
    }

    @Override
    public CommandResult createCalculateCourierTourCommand(Courier courier) {
        return CommandResult.error("No deliveries loaded",
                "Please load deliveries first before calculating a courier tour.");
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
    public CommandResult createAssignCourierCommand(Delivery delivery, Courier courier) {
        return CommandResult.error("No deliveries loaded",
                "Please load deliveries first before assigning couriers.");
    }

    @Override
    public String getStateName() {
        return "MapLoadedState";
    }

    public void saveTour(String filename) {
        controller.showError("Unable to save tour", "No tour has been calculated yet.");
    }
}
