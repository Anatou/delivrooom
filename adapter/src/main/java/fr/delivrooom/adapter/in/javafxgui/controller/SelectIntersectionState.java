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
        controller.loadMapFile(url);
        controller.setState(new MapLoadedState(controller));
    }

    @Override
    public void openDeliveriesFile(URL url) {
        controller.loadDeliveriesFile(url);
        controller.setState(new DeliveriesLoadedState(controller));
    }

    @Override
    public void selectIntersection(Intersection intersection) {
        controller.selectIntersection(intersection);
    }

    @Override
    public void requestIntersectionSelection() {
        // Nothing to do here, already in this state.
    }

    @Override
    public String getStateName() {
        return "SelectIntersectionState";
    }
}
