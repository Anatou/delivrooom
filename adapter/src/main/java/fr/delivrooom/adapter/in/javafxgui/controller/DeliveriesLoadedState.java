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
        controller.showError("Can’t select intersection", "Unable to select intersection for now.");
    }

    @Override
    public void requestIntersectionSelection() {
        // TODO: call a function of the mapCanvas to switch stase
        controller.setState(new SelectIntersectionState(controller));
    }

    @Override
    public String getStateName() {
        return "DeliveriesLoadedState";
    }

}
