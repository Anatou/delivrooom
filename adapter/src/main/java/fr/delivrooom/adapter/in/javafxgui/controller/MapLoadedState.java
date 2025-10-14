package fr.delivrooom.adapter.in.javafxgui.controller;

import javafx.scene.control.Alert;

import java.io.File;
import java.net.MalformedURLException;

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
            try {
                controller.loadMapFile(file.toURI().toURL());
            } catch (MalformedURLException e) {
                showError("Error loading map file", e.getMessage());
            }
        } else {
            showError("Invalid map file", "Please select a valid map file.");
        }
    }

    @Override
    public void openDeliveriesFile(File file) {
        if (file != null && file.exists()) {
            try {
                controller.loadDeliveriesFile(file.toURI().toURL());
                controller.setState(new DeliveriesLoadedState(controller));
            } catch (MalformedURLException e) {
                showError("Error loading deliveries file", e.getMessage());
            }
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
