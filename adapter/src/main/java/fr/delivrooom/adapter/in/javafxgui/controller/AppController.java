package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.MapCanvas;
import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import javafx.scene.control.Alert;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Main application controller implementing the State design pattern.
 * Manages the application state and coordinates between UI components.
 */
public class AppController {

    private State currentState;
    private MapCanvas mapCanvas;
    private CityMap cityMap;
    private DeliveriesDemand deliveriesDemand;

    public AppController() {
        this.currentState = new InitialState(this);
    }

    /**
     * Set the MapCanvas reference for rendering updates.
     *
     * @param mapCanvas The MapCanvas instance
     */
    public void setMapCanvas(MapCanvas mapCanvas) {
        this.mapCanvas = mapCanvas;
    }

    /**
     * Handle opening a map file through the current state.
     *
     * @param file The map file to open
     */
    public void handleOpenMapFile(File file) {
        currentState.openMapFile(file);
    }

    /**
     * Handle opening a deliveries file through the current state.
     *
     * @param file The deliveries file to open
     */
    public void handleOpenDeliveriesFile(File file) {
        currentState.openDeliveriesFile(file);
    }

    /**
     * Display an error alert dialog.
     *
     * @param title The title of the error and @param message The error message
     */
    private void showError(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Load a map file using the application service.
     * Called by state implementations.
     *
     * @param file The map file to load
     */
    protected void loadMapFile(File file) {
        try {
            this.cityMap = JavaFXApp.guiUseCase().getCityMap(file.toURI().toURL());
            this.deliveriesDemand = null; // Clear deliveries when loading new map
            updateMapCanvas();
            System.out.println("Map loaded successfully: " + file.getName());
        } catch (MalformedURLException e) {
            System.err.println("Error loading map file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load a deliveries file using the application service.
     * Called by state implementations.
     *
     * @param file The deliveries file to load
     */
    protected void loadDeliveriesFile(File file) {
        if (cityMap == null) {
            System.err.println("Cannot load deliveries without a map");
            return;
        }

        try {
            this.deliveriesDemand = JavaFXApp.guiUseCase().getDeliveriesDemand(cityMap, file.toURI().toURL());
            updateMapCanvas();
            System.out.println("Deliveries loaded successfully: " + file.getName());
        }
        catch (Exception e) {
            System.err.println("Error processing deliveries file: " + e.getMessage());
            e.printStackTrace();
            showError("error", e.getMessage());
        }
    }

    /**
     * Update the MapCanvas with current data.
     */
    private void updateMapCanvas() {
        if (mapCanvas != null) {
            mapCanvas.updateMap(cityMap, deliveriesDemand);
        }
    }

    /**
     * Set the current state.
     * Called by state implementations to transition between states.
     *
     * @param state The new state
     */
    protected void setState(State state) {
        this.currentState = state;
        System.out.println("State changed to: " + state.getStateName());
    }

    /**
     * Get the current state (for debugging/testing).
     *
     * @return The current state
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Get the loaded city map.
     *
     * @return The city map or null if not loaded
     */
    public CityMap getCityMap() {
        return cityMap;
    }

    /**
     * Get the loaded deliveries demand.
     *
     * @return The deliveries demand or null if not loaded
     */
    public DeliveriesDemand getDeliveriesDemand() {
        return deliveriesDemand;
    }
}
