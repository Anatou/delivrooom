package fr.delivrooom.adapter.in.javafxgui.controller;

import javafx.scene.control.Alert;

import java.io.File;
import java.net.MalformedURLException;

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
    public void openMapFile(File file) {
        if (file != null && file.exists()) {
            try {
                controller.loadMapFile(file.toURI().toURL());
                controller.setState(new MapLoadedState(controller));
            } catch (MalformedURLException e) {
                showError("Error loading map file", e.getMessage());
            }
        } else {
            showError("Invalid map file", "Please select a valid map file.");
        }
    }

    @Override
    public void openDeliveriesFile(File file) {
        showError("No map loaded", "Please load a map file first before loading deliveries.");
    }

    @Override
    public String getStateName() {
        return "InitialState";
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
