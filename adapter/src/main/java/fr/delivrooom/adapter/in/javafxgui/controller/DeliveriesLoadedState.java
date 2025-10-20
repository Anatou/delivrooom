package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Intersection;

import java.net.URL;

/**
 * Deliveries loaded state - both map and deliveries have been loaded.
 * Allows opening new map or deliveries files.
 */
public record DeliveriesLoadedState(AppController controller) implements State {

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
        controller.showError("Can’t select intersection", "Unable to select intersection for now.");
    }

    @Override
    public void requestIntersectionSelection() {
        controller.setState(new SelectIntersectionState(controller));
    }

    @Override
    public String getStateName() {
        return "DeliveriesLoadedState";
    }

    @Override
    public void requestCalculateTour() {
        try {
            controller.calculateTour();
        } catch (Exception e) {
            controller.showError("Error calculating tour", e.getMessage());
            e.printStackTrace();
        }
    }

}
