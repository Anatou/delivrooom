package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.command.CommandManager;
import fr.delivrooom.adapter.in.javafxgui.map.MapCanvas;
import fr.delivrooom.adapter.in.javafxgui.panes.Sidebar;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.application.model.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main application controller implementing the State design pattern.
 * Manages the application state and coordinates between UI components.
 */
public class AppController {

    // State management
    private final CommandManager commandManager;
    private SimpleObjectProperty<State> currentState;

    // UI components
    private MapCanvas mapCanvas;
    private Sidebar sidebar;

    // Loaded data
    private CityMap cityMap;
    private DeliveriesDemand deliveriesDemand;
    private TourSolution tourSolution;
    private BooleanProperty isTourBeingCalculated = new SimpleBooleanProperty(false);
    public AppController() {
        this.currentState = new SimpleObjectProperty<>(new InitialState(this));
        this.commandManager = new CommandManager();
    }

    /**
     * Must be called right after the UI components are initialized.
     */
    public void wireComponents(MapCanvas mapCanvas, Sidebar sidebar) {
        this.mapCanvas = mapCanvas;
        this.sidebar = sidebar;
    }

    /**
     * Handle opening a map file through the current state.
     *
     * @param file The map file to open
     */
    public void handleOpenMapFile(File file) {
        try {
            getState().openMapFile(file.toURI().toURL());
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
            getState().openDeliveriesFile(file.toURI().toURL());
        } catch (MalformedURLException e) {
            showError("Invalid file URL", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle selecting an intersection through the current state.
     *
     * @param intersection The intersection to select, set as null if no intersection is selected
     */
    public void handleSelectIntersection(Intersection intersection) {
        getState().selectIntersection(intersection);
    }

    /**
     * Request to switch to intersection selection mode.
     */
    public void handleRequestIntersectionSelection() {
        getState().requestIntersectionSelection();
    }


    /**
     * Load a map file using the application service.
     * Called by state implementations.
     *
     * @param url The map url to load
     */
    protected void loadMapFile(URL url) throws Exception {
        this.cityMap = JavaFXApp.guiUseCase().getCityMap(url);
        JavaFXApp.getCalculateTourUseCase().provideCityMap(cityMap);
        tourSolution = null;
        updateMapCanvas();
    }

    /**
     * Load a delivery file using the application service.
     * Called by state implementations.
     *
     * @param url The deliveries file to load
     */
    protected void loadDeliveriesFile(URL url) throws Exception {
        this.deliveriesDemand = JavaFXApp.guiUseCase().getDeliveriesDemand(cityMap, url);
        tourSolution = null;
        updateMapCanvas();
    }

    /**
     * Update the MapCanvas with current data.
     */
    protected void updateMapCanvas() {
        if (mapCanvas != null) {
            new Thread(() -> {
                if (JavaFXApp.getCalculateTourUseCase().doesCalculatedTourNeedsToBeChanged(deliveriesDemand)) {
                    JavaFXApp.getCalculateTourUseCase().findOptimalTour(deliveriesDemand, false);
                }
                tourSolution = JavaFXApp.getCalculateTourUseCase().getOptimalTour();
                Platform.runLater(() -> {
                    mapCanvas.drawMap();
                });
            }).start();

            mapCanvas.setAutoFraming(true);
            mapCanvas.drawMap();
        }
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

    public void handleLoadDefaultFiles(DefaultMapFilesType type) {
        URL cityMapURL = XMLCityMapLoader.class.getResource("/xml/" + type.map + ".xml");
        URL deliveriesURL = XMLCityMapLoader.class.getResource("/xml/" + type.deliveries + ".xml");
        getState().openMapFile(cityMapURL);
        getState().openDeliveriesFile(deliveriesURL);
    }

    /**
     * Update the selected intersection in the sidebar.
     */
    protected void selectIntersection(Intersection intersection) {
        sidebar.selectIntersection(intersection);
    }

    public void addDelivery(Delivery delivery) {
        System.out.println("Adding delivery " + delivery.takeoutIntersection().getId()
                + " from intersection " + delivery.takeoutIntersection().getId()
                + " to intersection " + delivery.deliveryIntersection().getId());
        this.deliveriesDemand.deliveries().add(delivery);
    }

    public void removeDelivery(Delivery delivery) {
        System.out.println("Removing delivery " + delivery.takeoutIntersection().getId()
                + " from intersection " + delivery.takeoutIntersection().getId()
                + " to intersection " + delivery.deliveryIntersection().getId());
        this.deliveriesDemand.deliveries().remove(delivery);
    }

    public State getState() {
        return currentState.get();
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void setState(State newState) {
        currentState.set(newState);
    }

    public SimpleObjectProperty<State> stateProperty() {
        return currentState;
    }

    public TourSolution getTourSolution() {
        return tourSolution;
    }

    public BooleanProperty isTourBeingCalculatedProperty() {
        return isTourBeingCalculated;
    }



    /**
     * Calculate the tour on demand (background thread). Requires a loaded city map and deliveries.
     */
    public void handleCalculateTour() {
        getState().requestCalculateTour();
    }

    public void calculateTour() {
        if (isTourBeingCalculated.get()) {
            showError("Cannot calculate tour", "Already running");
            return;
        }

        new Thread(() -> {

            try {
                // Delegate to use case to compute the tour
                this.isTourBeingCalculated.set(true);
                tourSolution = JavaFXApp.guiUseCase().getTourSolution(cityMap, deliveriesDemand);
                this.isTourBeingCalculated.set(false);
                Platform.runLater(() -> {
                    // redraw map to show the new tour
                    mapCanvas.drawMap();
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Error while calculating tour", e.getMessage() == null ? e.toString() : e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    public enum DefaultMapFilesType {
        SMALL_1("Small 1", "petitPlan", "demandePetit1"),
        SMALL_2("Small 2", "petitPlan", "demandePetit2"),
        MEDIUM_1("Medium 1", "moyenPlan", "demandeMoyen3"),
        MEDIUM_2("Medium 2", "moyenPlan", "demandeMoyen5"),
        LARGE_1("Large 1", "grandPlan", "demandeGrand7"),
        LARGE_2("Large 2", "grandPlan", "demandeGrand9");

        public final String name;
        public final String map;
        public final String deliveries;

        DefaultMapFilesType(String name, String map, String deliveries) {
            this.name = name;
            this.map = map;
            this.deliveries = deliveries;
        }
    }
}
