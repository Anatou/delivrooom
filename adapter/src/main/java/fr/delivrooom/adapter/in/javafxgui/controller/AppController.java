package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.utils.InvalidableReadOnlyListWrapper;
import fr.delivrooom.adapter.in.javafxgui.utils.InvalidableReadOnlyObjectWrapper;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.application.model.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
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
    // Loaded data
    protected final InvalidableReadOnlyObjectWrapper<CityMap> cityMap = new InvalidableReadOnlyObjectWrapper<>(null);
    protected final InvalidableReadOnlyObjectWrapper<DeliveriesDemand> deliveriesDemand = new InvalidableReadOnlyObjectWrapper<>(null);
    protected final InvalidableReadOnlyListWrapper<Courier> couriers = new InvalidableReadOnlyListWrapper<>(FXCollections.observableArrayList());
    // 0 = not running, 0 < x < 1 = running, 1 = done
    protected final DoubleProperty tourCalculationProgress = new SimpleDoubleProperty(0);
    protected final InvalidableReadOnlyObjectWrapper<Intersection> selectedIntersection = new InvalidableReadOnlyObjectWrapper<>(null);
    protected final BooleanProperty memeModeProperty = new SimpleBooleanProperty(false);
    // State management
    private final CommandManager commandManager = new CommandManager();
    private final InvalidableReadOnlyObjectWrapper<State> currentState = new InvalidableReadOnlyObjectWrapper<>(new StateInitial(this));

    private AppController() {
    }

    /**
     * Returns the singleton instance of the AppController.
     *
     * @return The singleton AppController instance.
     * d
     */
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
     * Gets the description of the next command to be undone.
     *
     * @return The description of the next undoable command, or null if the undo stack is empty.
     */
    public String getNextUndoCommandName() {
        Command cmd = commandManager.getNextUndoCommand();
        return cmd != null ? cmd.getStringDescription() : null;
    }

    /**
     * Gets the description of the next command to be redone.
     *
     * @return The description of the next redoable command, or null if the redo stack is empty.
     */
    public String getNextRedoCommandName() {
        Command cmd = commandManager.getNextRedoCommand();
        return cmd != null ? cmd.getStringDescription() : null;
    }

    /**
     * Returns an observable that is invalidated when the command manager's state changes.
     * This is used to trigger UI updates when commands are executed, undone, or redone.
     *
     * @return An observable that tracks command manager changes.
     */
    public Observable getCommandManagerTriggerChanges() {
        return commandManager.getTriggerChanges();
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
        getState().requestCalculateTour(null);
    }

    /**
     * Request to calculate the tour for a specific courier.
     *
     * @param courier The courier to calculate the tour for
     */
    public void requestCalculateCourierTour(Courier courier) {
        getState().requestCalculateTour(courier);
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

    /**
     * Request to load a tour solution from a file.
     *
     * @param file The file containing the tour solution.
     */
    /**
     * Request to load a tour solution from a file.
     *
     * @param file The file containing the tour solution.
     */
    public void requestLoadTourSolution(File file) {
        CommandResult result = getState().createLoadTourCommand(cityMap.get(), deliveriesDemand.get(), file.getAbsolutePath());
        requestCommand(result);
    }

    /**
     * Request to save the current tour to a file.
     *
     * @param file The file to save the tour to
     */
    public void requestSaveTourFile(File file) {
        if (file == null) {
            showError("Save Error", "No file specified for saving.");
            return;
        }
        try {
            getState().saveTour(file.getAbsolutePath());
            //System.out.println("Tour saved to " + file.getAbsolutePath());
        } catch (Exception e) {
            showError("Save Error", "Failed to save the tour: " + e.getMessage());
            e.printStackTrace();
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
     * Restore a previous city map state.
     * Called by commands during undo.
     *
     * @param cityMap The city map to restore
     */
    protected void doRestoreCityMap(CityMap cityMap) {
        this.cityMap.set(cityMap);
        if (cityMap != null) {
            JavaFXApp.getCalculateTourUseCase().provideCityMap(cityMap);
        }
    }

    protected void doRestoreCouriers(List<Courier> courierList) {
        couriersProperty().setAll(courierList);
        couriers.invalidate();
        deliveriesDemand.invalidate();
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
            courier.setDeliveriesDemand(restoredDemand);
        }
        deliveriesDemand.invalidate();
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
     * Calculate the optimal tour for all couriers
     * Called by commands.
     */
    protected void doCalculateTour() {
        if (couriers.isEmpty()) {
            showError("Cannot calculate tour", "No couriers available");
            return;
        }
        calculateTourForCouriers(new ArrayList<>(couriers), false);
    }

    protected void doCalculateTourForCourier(Courier courier) {
        calculateTourForCouriers(List.of(courier), true);
    }

    private void calculateTourForCouriers(List<Courier> couriers, boolean singleCourier) {
        //System.out.println("Requesting calculation of tour for " + couriers.size() + " courier(s)");
        if (tourCalculationProgress.get() > 0 && tourCalculationProgress.get() < 1) {
            showError("Cannot calculate tour", "Already running");
            return;
        }
        double previousProgress = tourCalculationProgress.get();
        this.tourCalculationProgress.set(0.0001);

        new Thread(() -> {
            try {
                boolean isThereAtLeastOneCourierWithDeliveries = false;
                boolean isThereAtLeastOneCourierWithTourNotAlreadyCalculated = false;
                HashMap<Courier, TourSolution> courierTourMap = new HashMap<>();
                for (Courier courier : couriers) {
                    if (courier.getDeliveriesDemand() != null && !courier.getDeliveriesDemand().deliveries().isEmpty()) {
                        //System.out.println("Calculating tour for courier " + courier.getId());

                        try {
                            if (courier.getTourSolution() == null) {
                                isThereAtLeastOneCourierWithTourNotAlreadyCalculated = true;
                                JavaFXApp.getCalculateTourUseCase().findOptimalTour(courier.getDeliveriesDemand(), false);
                                TourSolution solution = JavaFXApp.getCalculateTourUseCase().getOptimalTour();
                                courierTourMap.put(courier, solution);
                            }
                        } catch (RuntimeException e) {
                            Platform.runLater(() -> {
                                showError("Error while calculating tour",
                                        (couriers.size() == 1 ? "Cannot calculate tour" : "Cannot calculate tour of courier " + courier.getId())
                                                + ". Are both pickup and deposit adresses reachable?");
                                this.tourCalculationProgress.set(previousProgress);
                            });
                            return;
                        }
                        isThereAtLeastOneCourierWithDeliveries = true;
                    }
                }
                if (!isThereAtLeastOneCourierWithDeliveries) {
                    Platform.runLater(() -> {
                        showError("Cannot calculate tour", couriers.size() == 1 ? "Courier has no deliveries assigned." : "No couriers have deliveries assigned.");
                        this.tourCalculationProgress.set(previousProgress);
                    });
                    return;
                } else if (!isThereAtLeastOneCourierWithTourNotAlreadyCalculated) {
                    Platform.runLater(() -> {
                        showError("Already calculated", couriers.size() == 1 ? "Courier tour is already up to date." : "All couriers have up to date tours.");
                        this.tourCalculationProgress.set(previousProgress);
                    });
                    return;
                }
                Platform.runLater(() -> {
                    requestCommand(CommandResult.success(new CommandCalculateTour(this, courierTourMap, singleCourier, previousProgress)));
                    this.tourCalculationProgress.set(1.0);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Error while calculating tour", e.getMessage() == null ? e.toString() : e.getMessage());
                    this.tourCalculationProgress.set(previousProgress);
                });
                e.printStackTrace();
            }
        }).start();
    }

    protected void doSaveTourSolution(String filename) {
        TourSolutionSerialiserIO tourSolutionSerialiserIO = new TourSolutionSerialiserIO();
        List<Courier> courierList = new ArrayList<>();
        for (Courier c : couriers.get()) {
            courierList.add(c);
        }
        try {
            TourSolutionSerialiser serial = new TourSolutionSerialiser(cityMap.get(), deliveriesDemand.get(), courierList);
            tourSolutionSerialiserIO.saveTourSolutionSerialization(serial, filename);
        } catch (IOException e) {
            showError("Tour saving error", "Unable to save Tour");
        }
    }


    // ============================================================================
    // PACKAGE-PRIVATE API - State Management (for states and commands)
    // ============================================================================

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
    /**
     * Provides read-only access to the list of couriers.
     * This list should not be modified directly by the UI.
     *
     * @return The read-only list property for couriers.
     */
    public ReadOnlyListProperty<Courier> couriersProperty() {
        return couriers.getReadOnlyProperty();
    }

    /**
     * Invalidates the couriers list, forcing a refresh in the UI.
     */
    public void invalidateCouriers() {
        couriers.invalidate();
    }

    /**
     * Provides read-only access to the current city map.
     *
     * @return The read-only property for the city map.
     */
    public ReadOnlyProperty<CityMap> cityMapProperty() {
        return cityMap.getReadOnlyProperty();
    }

    /**
     * Provides read-only access to the current deliveries demand.
     *
     * @return The read-only property for the deliveries demand.
     */
    public ReadOnlyProperty<DeliveriesDemand> deliveriesDemandProperty() {
        return deliveriesDemand.getReadOnlyProperty();
    }

    /**
     * Provides read-only access to the currently selected intersection.
     *
     * @return The read-only property for the selected intersection.
     */
    public ReadOnlyProperty<Intersection> selectedIntersectionProperty() {
        return selectedIntersection.getReadOnlyProperty();
    }

    /**
     * Gets the current state of the application.
     *
     * @return The current state.
     */
    public State getState() {
        return currentState.get();
    }

    /**
     * Provides read-only access to the application's current state.
     *
     * @return The read-only property for the current state.
     */
    public ReadOnlyProperty<State> stateProperty() {
        return currentState.getReadOnlyProperty();
    }

    /**
     * Provides access to the tour calculation progress property.
     *
     * @return The double property representing tour calculation progress (0.0 to 1.0).
     */
    public DoubleProperty tourCalculationProgressProperty() {
        return tourCalculationProgress;
    }

    /**
     * A boolean binding that is true when a tour is currently being calculated.
     *
     * @return A boolean binding for the tour calculation status.
     */
    public BooleanBinding tourBeingCalculatedBinding() {
        return tourCalculationProgress.greaterThan(0.0).and(tourCalculationProgress.lessThan(1.0));
    }

    /**
     * A boolean binding that is true when a tour has been calculated.
     *
     * @return A boolean binding for the tour calculated status.
     */
    public BooleanBinding tourCalculatedBinding() {
        return tourCalculationProgress.greaterThanOrEqualTo(1.0);
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

    // ============================================================================
    // PRIVATE/UTILITY METHODS
    // ============================================================================

    /**
     * Displays an error dialog with a specified title and message.
     *
     * @param title   The title of the error dialog.
     * @param message The error message to display.
     */
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

    /**
     * Enum representing the default map and delivery files available for loading.
     */
    public enum DefaultMapFilesType {
        /**
         * Small map 1.
         */
        SMALL_1("Small 1", "petitPlan", "demandePetit1"),
        /**
         * Small map 2.
         */
        SMALL_2("Small 2", "petitPlan", "demandePetit2"),
        /**
         * Medium map 1.
         */
        MEDIUM_1("Medium 1", "moyenPlan", "demandeMoyen3"),
        /**
         * Medium map 2.
         */
        MEDIUM_2("Medium 2", "moyenPlan", "demandeMoyen5"),
        /**
         * Large map 1.
         */
        LARGE_1("Large 1", "grandPlan", "demandeGrand7"),
        /**
         * Large map 2.
         */
        LARGE_2("Large 2", "grandPlan", "demandeGrand9");

        /**
         * The display name of the file set.
         */
        public final String name;
        /**
         * The map filename (without extension).
         */
        public final String map;
        /**
         * The deliveries filename (without extension).
         */
        public final String deliveries;

        DefaultMapFilesType(String name, String map, String deliveries) {
            this.name = name;
            this.map = map;
            this.deliveries = deliveries;
        }
    }
}
