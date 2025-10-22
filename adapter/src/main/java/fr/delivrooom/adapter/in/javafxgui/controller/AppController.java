package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.command.CommandManager;
import fr.delivrooom.adapter.in.javafxgui.map.MapCanvas;
import fr.delivrooom.adapter.in.javafxgui.panes.Sidebar;
import fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery.DeliveryListItem;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.application.model.*;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<Courier> couriers;
    private static AppController instance;
    // 0 = not running, 0 < x < 1 = running, 1 = done
    private DoubleProperty tourCalculationProgress = new SimpleDoubleProperty(0);
    // Easter egg: meme mode for map tiles
    private BooleanProperty memeModeProperty = new SimpleBooleanProperty(false);

    private AppController() {
        this.currentState = new SimpleObjectProperty<>(new InitialState(this));
        this.commandManager = new CommandManager();
        this.couriers = FXCollections.observableArrayList();
    }

    public static AppController getController() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
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

    public void handleCourierAssignmentChange(Delivery updatedDelivery, Courier newCourier) {
        for (DeliveryListItem item : sidebar.getDeliveriesSection().getDeliveriesList().getDeliveryItems()) {
            item.getActionButtons().updateDisplayedSelectedCourier(updatedDelivery, newCourier);
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
    public void updateMapCanvas() {
        if (mapCanvas != null) {
            mapCanvas.setAutoFraming(true);
            mapCanvas.drawMap();
        }
    }

    public ObservableList<Courier> getCouriers() {
        return couriers;
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
        sidebar.getDeliveryCreationSection().selectIntersection(intersection);
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

    public double getTourCalculationProgress() {
        return tourCalculationProgress.get();
    }

    public DoubleProperty tourCalculationProgressProperty() {
        return tourCalculationProgress;
    }

    public BooleanBinding tourBeingCalculatedBinding() {
        return tourCalculationProgress.greaterThan(0.0).and(tourCalculationProgress.lessThan(1.0));
    }

    /**
     * Calculate the tour on demand (background thread). Requires a loaded city map and deliveries.
     */
    public void handleCalculateTour() {
        getState().requestCalculateTour();
    }

    public void calculateTour() {
        if (tourCalculationProgress.get() > 0 && tourCalculationProgress.get() < 1) {
            showError("Cannot calculate tour", "Already running");
            return;
        }

        new Thread(() -> {

            try {
                this.tourCalculationProgress.set(0.0001); // Small value, but not 0 cause it would be invisible
                if (JavaFXApp.getCalculateTourUseCase().doesCalculatedTourNeedsToBeChanged(deliveriesDemand)) {
                    JavaFXApp.getCalculateTourUseCase().findOptimalTour(deliveriesDemand, false);
                }
                tourSolution = JavaFXApp.getCalculateTourUseCase().getOptimalTour();
                this.tourCalculationProgress.set(1);
                Platform.runLater(() -> {
                    mapCanvas.drawMap();
                });

            } catch (Exception e) {
                Platform.runLater(() -> showError("Error while calculating tour", e.getMessage() == null ? e.toString() : e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Get the meme mode property for the easter egg.
     * When true, map tiles will load random memes instead of actual map tiles.
     *
     * @return The meme mode property
     */
    public BooleanProperty memeModeProperty() {
        return memeModeProperty;
    }

    /**
     * Toggle the meme mode on/off and clear the tile cache.
     */
    public void toggleMemeMode() {
        memeModeProperty.set(!memeModeProperty.get());
        // Clear tile cache and trigger redraw
        if (mapCanvas != null) {
            mapCanvas.clearTileCache();
            mapCanvas.drawMap();
        }
    }

    public void calculateTourForCourier(Courier courier) {
        if (tourCalculationProgress.get() > 0 && tourCalculationProgress.get() < 1) {
            showError("Cannot calculate tour", "Already running");
            return;
        }

        if (courier.getDeliveriesDemand().deliveries().isEmpty() || courier.getDeliveriesDemand() == null) {
            showError("Cannot calculate tour", "Courier has no deliveries assigned");
            return;
        }
        new Thread(() -> {

            try {
                this.tourCalculationProgress.set(0.0001); // Small value, but not 0 cause it would be invisible
                if (JavaFXApp.getCalculateTourUseCase().doesCalculatedTourNeedsToBeChanged(courier.getDeliveriesDemand())) {
                    System.out.println("tour needs to be recalculated for courier " + courier.getId());
                    JavaFXApp.getCalculateTourUseCase().findOptimalTour(courier.getDeliveriesDemand(), false);

                }
                tourSolution = JavaFXApp.getCalculateTourUseCase().getOptimalTour();
                this.tourCalculationProgress.set(1);
                Platform.runLater(() -> {
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
