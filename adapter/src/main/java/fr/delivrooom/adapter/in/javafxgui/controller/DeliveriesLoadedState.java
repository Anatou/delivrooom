package fr.delivrooom.adapter.in.javafxgui.controller;

import java.net.URL;

/**
 * Deliveries loaded state - both map and deliveries have been loaded.
 * Allows opening new map or deliveries files.
 */
public record DeliveriesLoadedState(AppController controller) implements State {

    @Override
    public void openMapFile(URL url) {
        controller.loadMapFile(url);
    }

    @Override
    public void openDeliveriesFile(URL url) {
        controller.loadDeliveriesFile(url);
    }

    @Override
    public String getStateName() {
        return "DeliveriesLoadedState";
    }

}
