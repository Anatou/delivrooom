package fr.delivrooom.adapter.in.javafxgui.controller;

import java.net.URL;

/**
 * Initial state - no map or deliveries loaded yet.
 * Only allows opening a map file.
 */
public class InitialState implements State {

    private final AppController controller;

    public InitialState(AppController controller) {
        this.controller = controller;
    }

    @Override
    public void openMapFile(URL url) {
        controller.loadMapFile(url);
        controller.setState(new MapLoadedState(controller));
    }

    @Override
    public void openDeliveriesFile(URL url) {
        controller.showError("No map loaded", "Please load a map file first before loading deliveries.");
    }

    @Override
    public String getStateName() {
        return "InitialState";
    }

}
