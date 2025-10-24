package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.CityMap;

import java.net.URL;

/**
 * Command to load a city map file.
 * Supports undo by restoring the previous map state.
 */
public class CommandLoadMap implements Command {

    private final AppController controller;
    private final URL mapUrl;
    private final State sourceState;
    private CityMap previousMap;

    public CommandLoadMap(AppController controller, URL mapUrl, State sourceState) {
        this.controller = controller;
        this.mapUrl = mapUrl;
        this.sourceState = sourceState;
    }

    @Override
    public void execute() {
        try {
            previousMap = controller.cityMapProperty().getValue();
            controller.doLoadMapFile(mapUrl);
            controller.transitionToState(new StateMapLoaded(controller));
        } catch (Exception e) {
            controller.showError("Error loading map", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        try {
            controller.doRestoreCityMap(previousMap);
            controller.transitionToState(sourceState);
        } catch (Exception e) {
            controller.showError("Error undoing map load", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Load map " + mapUrl.toString();
    }
}
