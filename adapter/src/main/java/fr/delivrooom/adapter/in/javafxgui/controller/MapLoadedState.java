package fr.delivrooom.adapter.in.javafxgui.controller;

import java.net.URL;

/**
 * Map loaded state - a map has been loaded.
 * Allows opening a new map file or loading deliveries.
 */
public record MapLoadedState(AppController controller) implements State {

    @Override
    public void openMapFile(URL url) {
        controller.loadMapFile(url);
    }

    @Override
    public void openDeliveriesFile(URL url) {
        controller.loadDeliveriesFile(url);
        controller.setState(new DeliveriesLoadedState(controller));
    }

    @Override
    public String getStateName() {
        return "MapLoadedState";
    }
}
