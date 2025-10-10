package fr.delivrooom.adapter.in.javafxgui.controller;

import javafx.scene.control.Alert;

import java.io.File;

/**
 * Deliveries loaded state - both map and deliveries have been loaded.
 * Allows opening new map or deliveries files.
 */
public class DeliveriesLoadedState implements State {

    private final AppController controller;

    public DeliveriesLoadedState(AppController controller) {
        this.controller = controller;
    }

    @Override
    public void openMapFile(File file) {
        if (file != null && file.exists()) {
            controller.loadMapFile(file);
            // Transition back to MapLoadedState since deliveries need to be reloaded
            controller.setState(new MapLoadedState(controller));
        } else {
            showError("Invalid map file", "Please select a valid map file.");
        }
    }

    @Override
    public void openDeliveriesFile(File file) {
        if (file != null && file.exists()) {
            controller.loadDeliveriesFile(file);
            // Stay in DeliveriesLoadedState
        } else {
            showError("Invalid deliveries file", "Please select a valid deliveries file.");
        }
    }

    @Override
    public String getStateName() {
        return "DeliveriesLoadedState";
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
