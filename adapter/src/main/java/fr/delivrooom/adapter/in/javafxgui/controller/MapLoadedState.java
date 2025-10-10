package fr.delivrooom.adapter.in.javafxgui.controller;

import javafx.scene.control.Alert;

import java.io.File;

/**
 * Map loaded state - a map has been loaded.
 * Allows opening a new map file or loading deliveries.
 */
public class MapLoadedState implements State {

    private final AppController controller;

    public MapLoadedState(AppController controller) {
        this.controller = controller;
    }

    @Override
    public void openMapFile(File file) {
        if (file != null && file.exists()) {
            controller.loadMapFile(file);
            // Stay in MapLoadedState after loading a new map
        } else {
            showError("Invalid map file", "Please select a valid map file.");
        }
    }

    @Override
    public void openDeliveriesFile(File file) {
        if (file != null && file.exists()) {
            controller.loadDeliveriesFile(file);
            controller.setState(new DeliveriesLoadedState(controller));
        } else {
            showError("Invalid deliveries file", "Please select a valid deliveries file.");
        }
    }

    @Override
    public String getStateName() {
        return "MapLoadedState";
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
