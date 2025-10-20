package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.MapCanvas;
import fr.delivrooom.adapter.in.javafxgui.command.CommandManager;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Delivery;
import javafx.scene.control.Alert;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main application controller implementing the State design pattern.
 * Manages the application state and coordinates between UI components.
 */
public class AppController {

    private final CommandManager commandManager;
    private State currentState;
    private MapCanvas mapCanvas;
    private CityMap cityMap;
    private DeliveriesDemand deliveriesDemand;


    public AppController() {
        this.currentState = new InitialState(this);
        this.commandManager = new CommandManager();
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
        try {
            currentState.openMapFile(file.toURI().toURL());
        } catch (MalformedURLException e) {
            showError("Invalid file URL", e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Handle opening a delivery file through the current state.
     *
     * @param file The deliveries file to open
     */
    public void handleOpenDeliveriesFile(File file) {
        try {
            currentState.openDeliveriesFile(file.toURI().toURL());
        } catch (MalformedURLException e) {
            showError("Invalid file URL", e.getMessage());
            e.printStackTrace();
        }
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
            this.deliveriesDemand = null; // Clear deliveries when loading a new map
            updateMapCanvas();
            System.out.println("Map loaded successfully: " + url);
        } catch (Exception e) {
            System.err.println("Error loading map file: " + e.getMessage());
            showError("An error occurred", e.getMessage());
        }
    }

    /**
     * Load a delivery file using the application service.
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

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void handleLoadDefaultFiles() {
        URL cityMapURL = XMLCityMapLoader.class.getResource("/xml/grandPlan.xml");
        URL deliveriesURL = XMLCityMapLoader.class.getResource("/xml/demandeGrand7.xml");
        currentState.openMapFile(cityMapURL);
        currentState.openDeliveriesFile(deliveriesURL);
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean addDelivery(Delivery delivery) {
        if (delivery!=null ) {
            System.out.println("Adding delivery " + delivery.takeoutIntersection().getId()
                    + " from intersection " + delivery.takeoutIntersection().getId()
                    + " to intersection " + delivery.deliveryIntersection().getId());
            this.deliveriesDemand.deliveries().add(delivery);
            //updateMapCanvas();
            return true;
        }
        return false;
    }

    public boolean removeDelivery(Delivery delivery) {
        if (delivery!=null ) {
            System.out.println("Removing delivery " + delivery.takeoutIntersection().getId()
                    + " from intersection " + delivery.takeoutIntersection().getId()
                    + " to intersection " + delivery.deliveryIntersection().getId());

            this.deliveriesDemand.deliveries().remove(delivery);

            //updateMapCanvas();
            return true;
        }
        return false;
    }
}
