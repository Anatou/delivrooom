package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Intersection;

import java.net.URL;

/**
 * Select intersection state - map and deliveries have been loaded, the use now wants to select an intersection.
 * Allows the user to select an intersection on the map canvas.
 */
public record SelectIntersectionState(AppController controller) implements State {

    @Override
    public void openMapFile(URL url) {
        try {
            controller.loadMapFile(url);
            controller.setState(new MapLoadedState(controller));
        } catch (Exception e) {
            controller.showError("Error loading map", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void openDeliveriesFile(URL url) {
        try {
            controller.loadDeliveriesFile(url);
            controller.setState(new DeliveriesLoadedState(controller));
        } catch (Exception e) {
            controller.showError("Error loading deliveries", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void selectIntersection(Intersection intersection) {
        controller.selectIntersection(intersection);
        controller.setState(new DeliveriesLoadedState(controller));
    }

    @Override
    public void requestIntersectionSelection() {
        // Nothing to do here, already in this state.
    }

    @Override
    public String getStateName() {
        return "SelectIntersectionState";
    }

    @Override
    public void requestCalculateTour() {
        controller.showError("Cannot calculate tour", "Please select an intersection first.");
    }
}
