package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.MapCanvas;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import javafx.scene.control.Alert;

import java.io.File;
import java.net.URL;

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
     * @param url The map url to load
     */
    protected void loadMapFile(URL url) {
        try {
            this.cityMap = JavaFXApp.guiUseCase().getCityMap(url);
            this.deliveriesDemand = null; // Clear deliveries when loading new map
            updateMapCanvas();
            System.out.println("Map loaded successfully: " + url);
        } catch (Exception e) {
            System.err.println("Error loading map file: " + e.getMessage());
            showError("An error occurred", e.getMessage());
        }
    }

    /**
     * Load a deliveries file using the application service.
     * Called by state implementations.
     *
     * @param url The deliveries file to load
     */
    protected void loadDeliveriesFile(URL url) {
        try {
            this.deliveriesDemand = JavaFXApp.guiUseCase().getDeliveriesDemand(cityMap, url);
            updateMapCanvas();
            System.out.println("Deliveries loaded successfully: " + url);
        } catch (Exception e) {
            System.err.println("Error loading deliveries file: " + e.getMessage());
            showError("An error occurred", e.getMessage());
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

    public void handleLoadDefaultFiles() {
        URL cityMapURL = XMLCityMapLoader.class.getResource("/xml/petitPlan.xml");
        URL deliveriesURL = XMLCityMapLoader.class.getResource("/xml/demandePetit1.xml");
        loadMapFile(cityMapURL);
        loadDeliveriesFile(deliveriesURL);
    }
}
