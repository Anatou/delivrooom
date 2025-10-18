package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Intersection;

import java.net.URL;

/**
 * Initial state - no map or deliveries loaded yet.
 * Only allows opening a map file.
 */
public record InitialState(AppController controller) implements State {

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
        controller.showError("No map loaded", "Please load a map file first before loading deliveries.");
    }

    @Override
    public void selectIntersection(Intersection intersection) {
        controller.showError("Can’t select intersection", "Unable to select intersection for now.");
    }

    @Override
    public void requestIntersectionSelection() {
        controller.showError("No map loaded", "Please load a map file first before selecting an intersection.");
    }

    @Override
    public String getStateName() {
        return "InitialState";
    }

}
