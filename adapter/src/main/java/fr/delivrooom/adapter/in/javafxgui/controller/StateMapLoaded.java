package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Intersection;

import java.net.URL;

/**
 * Map loaded state - a map has been loaded.
 * Allows opening a new map file or loading deliveries.
 */
public record StateMapLoaded(AppController controller) implements State {

    @Override
    public void openMapFile(URL url) {
        try {
            controller.loadMapFile(url);
            controller.setState(new StateMapLoaded(controller));
        } catch (Exception e) {
            controller.showError("Error loading map", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void openDeliveriesFile(URL url) {
        try {
            controller.loadDeliveriesFile(url);
            controller.setState(new StateDeliveriesLoaded(controller));
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
        controller.showError("No deliveries loaded", "Please load deliveries first before selecting an intersection.");
    }

    @Override
    public String getStateName() {
        return "MapLoadedState";
    }

    @Override
    public void requestCalculateTour() {
        controller.showError("No deliveries loaded", "Please load deliveries first before calculating the tour");


    }
}
