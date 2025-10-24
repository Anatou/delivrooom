package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.utils.InvalidableReadOnlyObjectWrapper;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.application.model.*;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main application controller implementing the State-Driven Command Factory pattern.
 *
 * <p><b>Architecture Pattern: State-Driven Command Factory</b></p>
 * <pre>
 * UI Component → Controller.requestX() → State.createXCommand() → CommandResult
 *                                                                     ↓
 *                Controller.doX() ← Command.execute() ← CommandManager
 * </pre>
 *
 * <p><b>Public API (for UI components):</b></p>
 * <ul>
 *   <li>{@code requestX()} - Request operations (creates command via state)</li>
 *   <li>{@code undoCommand()} - Undo the last command</li>
 *   <li>{@code redoCommand()} - Redo the last undone command</li>
 * </ul>
 *
 * <p><b>Protected API (for commands):</b></p>
 * <ul>
 *   <li>{@code doX()} - Perform actual data modifications</li>
 * </ul>
 *
 * <p><b>Package-private API (for states/commands):</b></p>
 * <ul>
 *   <li>{@code transitionToState(State)} - Change application state</li>
 * </ul>
 *
 * @see State
 * @see Command
 * @see CommandManager
 * @see CommandResult
 */
public class AppController {

    // Singleton implementation
    private static AppController instance;

    // State management
    private final CommandManager commandManager = new CommandManager();
    private final InvalidableReadOnlyObjectWrapper<State> currentState = new InvalidableReadOnlyObjectWrapper<>(new StateInitial(this));
    // Loaded data
    private final InvalidableReadOnlyObjectWrapper<CityMap> cityMap = new InvalidableReadOnlyObjectWrapper<>(null);
    private final InvalidableReadOnlyObjectWrapper<DeliveriesDemand> deliveriesDemand = new InvalidableReadOnlyObjectWrapper<>(null);
    private final InvalidableReadOnlyObjectWrapper<TourSolution> tourSolution = new InvalidableReadOnlyObjectWrapper<>(null);
    private final ReadOnlyListWrapper<Courier> couriers = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    // 0 = not running, 0 < x < 1 = running, 1 = done
    private final DoubleProperty tourCalculationProgress = new SimpleDoubleProperty(0);
    private final InvalidableReadOnlyObjectWrapper<Intersection> selectedIntersection = new InvalidableReadOnlyObjectWrapper<>(null);
    private final BooleanProperty memeModeProperty = new SimpleBooleanProperty(false);

    private AppController() {
    }

    public static AppController getController() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    // ============================================================================
    // PUBLIC API - Request Methods (for UI components)
    // ============================================================================


    /**
     * Request to undo the last command.
     */
    public void undoCommand() {
        commandManager.undo();
    }

    /**
     * Request to redo the last undone command.
     */
    public void redoCommand() {
        commandManager.redo();
    }

    /**
     * Request to open a map file.
     *
     * @param file The map file to open
     */
    public void requestOpenMapFile(File file) {
        try {
            CommandResult result = getState().createOpenMapCommand(file.toURI().toURL());
            requestCommand(result);
        } catch (MalformedURLException e) {
            showError("Invalid file URL", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Request to open a deliveries file.
     *
     * @param file The deliveries file to open
     */
    public void requestOpenDeliveriesFile(File file) {
        try {
            CommandResult result = getState().createOpenDeliveriesCommand(file.toURI().toURL());
            requestCommand(result);
        } catch (MalformedURLException e) {
            showError("Invalid file URL", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Request to load default map and deliveries files.
     *
     * @param type The type of default files to load
     */
    public void requestLoadDefaultFiles(DefaultMapFilesType type) {
        URL cityMapURL = XMLCityMapLoader.class.getResource("/xml/" + type.map + ".xml");
        URL deliveriesURL = XMLCityMapLoader.class.getResource("/xml/" + type.deliveries + ".xml");

        CommandResult mapResult = getState().createOpenMapCommand(cityMapURL);
        requestCommand(mapResult);

        CommandResult deliveriesResult = getState().createOpenDeliveriesCommand(deliveriesURL);
        requestCommand(deliveriesResult);
    }

    /**
     * Request to add a delivery.
     *
     * @param delivery The delivery to add
     */
    public void requestAddDelivery(Delivery delivery) {
        CommandResult result = getState().createAddDeliveryCommand(delivery);
        requestCommand(result);
    }

    /**
     * Request to remove a delivery.
     *
     * @param delivery The delivery to remove
     */
    public void requestRemoveDelivery(Delivery delivery) {
        CommandResult result = getState().createRemoveDeliveryCommand(delivery);
        requestCommand(result);
    }

    /**
     * Request to select an intersection.
     *
     * @param intersection The intersection to select
     */
    public void requestSelectIntersection(Intersection intersection) {
        getState().selectIntersection(intersection);
    }

    /**
     * Request to enter intersection selection mode.
     */
    public void requestIntersectionSelection() {
        getState().requestIntersectionSelection();
    }

    /**
     * Request to calculate the tour for all deliveries.
     */
    public void requestCalculateTour() {
        CommandResult result = getState().createCalculateTourCommand();
        requestCommand(result);
    }

    /**
     * Request to calculate the tour for a specific courier.
     *
     * @param courier The courier to calculate the tour for
     */
    public void requestCalculateCourierTour(Courier courier) {
        CommandResult result = getState().createCalculateCourierTourCommand(courier);
        requestCommand(result);
    }

    /**
     * Request to assign a courier to a delivery.
     *
     * @param delivery The delivery to assign
     * @param courier  The courier to assign
     */
    public void requestAssignCourier(Delivery delivery, Courier courier) {
        CommandResult result = getState().createAssignCourierCommand(delivery, courier);
        requestCommand(result);
    }

    /**
     * Request to add a courier to the system.
     *
     * @param courier The courier to add
     */
    public void requestAddCourier(Courier courier) {
        CommandResult result = getState().createAddCourierCommand(courier);
        requestCommand(result);
    }

    public void requestSaveTourFile(File file) {
        Command command = getState().createSaveTourCommand(file);
        if (command != null) {
            commandManager.executeCommand(command);
        } else {
            showError("Invalid Action", "Saving the tour is not allowed in the current state.");
        }
    }

    // ============================================================================
    // PROTECTED API - Do Methods (for commands to modify data)
    // ============================================================================

    /**
     * Request to remove a courier from the system.
     *
     * @param courier The courier to remove
     */
    public void requestRemoveCourier(Courier courier) {
        CommandResult result = getState().createRemoveCourierCommand(courier);
        requestCommand(result);
    }

    /**
     * Load a map file using the application service.
     * Called by commands.
     *
     * @param url The map url to load
     */
    protected void doLoadMapFile(URL url) throws Exception {
        this.cityMap.set(JavaFXApp.guiUseCase().getCityMap(url));
        this.tourSolution.set(null);
        deleteDeliveryDemandOfCourier();
    }

    /**
     * Restore a previous city map state.
     * Called by commands during undo.
     *
     * @param cityMap The city map to restore
     */
    protected void doRestoreCityMap(CityMap cityMap) {
        this.cityMap.set(cityMap);
    }

    /**
     * Load a deliveries file using the application service.
     * Called by commands.
     *
     * @param url The deliveries file to load
     */
    protected void doLoadDeliveriesFile(URL url) throws Exception {
        this.deliveriesDemand.set(JavaFXApp.guiUseCase().getDeliveriesDemand(cityMap.get(), url));
        this.tourSolution.set(null);
        deleteDeliveryDemandOfCourier();
    }

    /**
     * Restore a previous deliveries demand state.
     * Called by commands during undo.
     *
     * @param deliveriesDemand The deliveries demand to restore
     */
    protected void doRestoreDeliveriesDemand(DeliveriesDemand deliveriesDemand) {
        this.deliveriesDemand.set(deliveriesDemand);
    }

    /**
     * Capture the current courier deliveries state for undo purposes.
     * Called by commands before modifying courier assignments.
     *
     * @return A map of couriers to their current deliveries demands
     */
    protected Map<Courier, DeliveriesDemand> doCaptureCourierDeliveries() {
        Map<Courier, DeliveriesDemand> snapshot = new HashMap<>();
        for (Courier courier : couriers) {
            if (courier.getDeliveriesDemand() != null) {
                // Create a deep copy of the deliveries demand
                DeliveriesDemand copy = new DeliveriesDemand(
                        new ArrayList<>(courier.getDeliveriesDemand().deliveries()),
                        courier.getDeliveriesDemand().store()
                );
                snapshot.put(courier, copy);
            }
        }
        return snapshot;
    }

    /**
     * Restore courier deliveries state from a snapshot.
     * Called by commands during undo.
     *
     * @param snapshot The snapshot to restore
     */
    protected void doRestoreCourierDeliveries(Map<Courier, DeliveriesDemand> snapshot) {
        if (snapshot == null) return;

        for (Courier courier : couriers) {
            DeliveriesDemand restoredDemand = snapshot.get(courier);
            if (restoredDemand != null) {
                courier.setDeliveriesDemand(restoredDemand);
            } else {
                courier.setDeliveriesDemand(null);
            }
        }
        deliveriesDemand.invalidate();
    }

    /**
     * Add a delivery to the deliveries demand.
     * Called by commands.
     *
     * @param delivery The delivery to add
     */
    protected void doAddDelivery(Delivery delivery) {
        System.out.println("Adding delivery from intersection " + delivery.takeoutIntersection().getId()
                + " to intersection " + delivery.deliveryIntersection().getId());
        deliveriesDemand.get().deliveries().add(delivery);
        deliveriesDemand.invalidate();
    }

    /**
     * Remove a delivery from the deliveries demand.
     * Called by commands.
     *
     * @param delivery The delivery to remove
     */
    protected void doRemoveDelivery(Delivery delivery) {
        System.out.println("Removing delivery from intersection " + delivery.takeoutIntersection().getId()
                + " to intersection " + delivery.deliveryIntersection().getId());
        deliveriesDemand.get().deliveries().remove(delivery);
        deliveriesDemand.invalidate();
    }

    /**
     * Add a courier to the system.
     * Called by commands.
     *
     * @param courier The courier to add
     */
    protected void doAddCourier(Courier courier) {
        couriers.add(courier);
    }

    /**
     * Remove a courier from the system.
     * Called by commands.
     *
     * @param courier The courier to remove
     */
    protected void doRemoveCourier(Courier courier) {
        couriers.remove(courier);
    }

    /**
     * Select an intersection and update the UI.
     * Called by commands.
     *
     * @param intersection The intersection to select, or null to clear selection
     */
    protected void doSelectIntersection(Intersection intersection) {
        selectedIntersection.set(intersection);
    }

    /**
     * Calculate the optimal tour for all deliveries.
     * Called by commands.
     */
    protected void doCalculateTour() {
        if (tourCalculationProgress.get() > 0 && tourCalculationProgress.get() < 1) {
            showError("Cannot calculate tour", "Already running");
            return;
        }

        new Thread(() -> {
            try {
                this.tourCalculationProgress.set(0.0001);
                JavaFXApp.getCalculateTourUseCase().provideCityMap(cityMap.get());
                if (JavaFXApp.getCalculateTourUseCase().doesCalculatedTourNeedsToBeChanged(deliveriesDemand.get())) {
                    JavaFXApp.getCalculateTourUseCase().findOptimalTour(deliveriesDemand.get(), false);
                }
                TourSolution solution = JavaFXApp.getCalculateTourUseCase().getOptimalTour();
                Platform.runLater(() -> {
                    this.tourCalculationProgress.set(1);
                    this.tourSolution.set(solution);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Error while calculating tour",
                        e.getMessage() == null ? e.toString() : e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Calculate the optimal tour for a specific courier.
     * Called by commands.
     *
     * @param courier The courier to calculate the tour for
     */
    protected void doCalculateTourForCourier(Courier courier) {
        if (tourCalculationProgress.get() > 0 && tourCalculationProgress.get() < 1) {
            showError("Cannot calculate tour", "Already running");
            return;
        }

        if (courier.getDeliveriesDemand() == null || courier.getDeliveriesDemand().deliveries().isEmpty()) {
            showError("Cannot calculate tour", "Courier has no deliveries assigned");
            return;
        }

        new Thread(() -> {
            try {
                this.tourCalculationProgress.set(0.0001);
                JavaFXApp.getCalculateTourUseCase().provideCityMap(cityMap.get());
                if (JavaFXApp.getCalculateTourUseCase().doesCalculatedTourNeedsToBeChanged(courier.getDeliveriesDemand())) {
                    System.out.println("Tour needs to be recalculated for courier " + courier.getId());
                    JavaFXApp.getCalculateTourUseCase().findOptimalTour(courier.getDeliveriesDemand(), false);
                }
                TourSolution solution = JavaFXApp.getCalculateTourUseCase().getOptimalTour();
                Platform.runLater(() -> {
                    this.tourCalculationProgress.set(1);
                    this.tourSolution.set(solution);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Error while calculating tour",
                        e.getMessage() == null ? e.toString() : e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Restore a previous tour solution state.
     * Called by commands during undo.
     *
     * @param tourSolution The tour solution to restore
     */
    protected void doRestoreTourSolution(TourSolution tourSolution) {
        this.tourSolution.set(tourSolution);
    }

    // ============================================================================
    // PACKAGE-PRIVATE API - State Management (for states and commands)
    // ============================================================================

    /**
     * Assign a courier to a delivery.
     * Called by commands.
     *
     * @param delivery The delivery to assign
     * @param courier  The courier to assign to the delivery
     */
    protected void doAssignCourier(Delivery delivery, Intersection store, Courier courier) {
        Courier assignedCourier = getCourierForDelivery(delivery);
        if (assignedCourier != null) {
            if (!assignedCourier.equals(courier)) {
                assignedCourier.removeDelivery(delivery);
                courier.addDelivery(delivery, store);
            }
        } else {
            courier.addDelivery(delivery, store);
        }
        this.deliveriesDemand.invalidate();
    }

    /**
     * Transition to a new state.
     * Called by commands and states.
     *
     * @param newState The new state to transition to
     */
    void transitionToState(State newState) {
        currentState.set(newState);
    }

    // ============================================================================
    // PUBLIC GETTERS & PROPERTIES
    // ============================================================================

    /**
     * Get the courier currently assigned to a delivery.
     * Called by commands.
     *
     * @param delivery The delivery to check
     * @return The assigned courier, or null if none
     */
    public Courier getCourierForDelivery(Delivery delivery) {
        for (Courier courier : couriers) {
            if (courier.getDeliveriesDemand() != null &&
                    courier.getDeliveriesDemand().deliveries().contains(delivery)) {
                return courier;
            }
        }
        return null;
    }

    /**
     * This courier observable list should be used as a read-only list!
     *
     * @return The list of couriers in the system, as an observable list.
     */
    public ObservableList<Courier> couriersProperty() {
        return couriers;
    }

    public ReadOnlyProperty<CityMap> cityMapProperty() {
        return cityMap.getReadOnlyProperty();
    }

    public ReadOnlyProperty<TourSolution> tourSolutionProperty() {
        return tourSolution.getReadOnlyProperty();
    }

    public ReadOnlyProperty<DeliveriesDemand> deliveriesDemandProperty() {
        return deliveriesDemand.getReadOnlyProperty();
    }

    public ReadOnlyProperty<Intersection> selectedIntersectionProperty() {
        return selectedIntersection.getReadOnlyProperty();
    }

    public State getState() {
        return currentState.get();
    }

    public ReadOnlyProperty<State> stateProperty() {
        return currentState.getReadOnlyProperty();
    }


    public DoubleProperty tourCalculationProgressProperty() {
        return tourCalculationProgress;
    }

    public BooleanBinding tourBeingCalculatedBinding() {
        return tourCalculationProgress.greaterThan(0.0).and(tourCalculationProgress.lessThan(1.0));
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
    }

    public void saveTourSolution(String filename) {
        TourSolutionSerialiserIO tourSolutionSerialiserIO = new TourSolutionSerialiserIO();
        List<TourSolution> tourList = new ArrayList<>();
        for (Courier courier : couriers) {
            tourList.add(courier.getTourSolution());
        }
        try {
            tourSolutionSerialiserIO.saveList(tourList, filename);
        }
        catch (IOException e) {
            showError("Tour saving error", "Unable to save Tour");
        }
    }

    // ============================================================================
    // PRIVATE/UTILITY METHODS
    // ============================================================================

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Execute a command result through the command manager.
     * Private to force UI to use specific requestX() methods.
     *
     * @param result The command result containing either a command or an error
     */
    private void requestCommand(CommandResult result) {
        if (result.isSuccess()) {
            commandManager.executeCommand(result.command());
        } else {
            showError(result.errorTitle(), result.errorMessage());
        }
    }

    private void deleteDeliveryDemandOfCourier() {
        for (Courier courier : couriers) {
            if (courier.getDeliveriesDemand() != null) {
                courier.getDeliveriesDemand().deliveries().clear();
            }
        }
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
