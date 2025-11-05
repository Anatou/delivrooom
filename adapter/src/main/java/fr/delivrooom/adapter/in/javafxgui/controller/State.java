package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.*;

import java.net.URL;

/**
 * State interface for the State design pattern implementing Command Factory pattern.
 *
 * <p>States act as command factories - they create commands for operations that are valid
 * in the current state. If an operation is not allowed, the method returns a CommandResult
 * with a state-specific error message.</p>
 *
 * <p><b>Architecture Pattern: State-Driven Command Factory</b></p>
 * <pre>
 * UI Component → Controller.requestX() → State.createXCommand() → CommandResult
 *                                                                     ↓
 *                Controller.doX() ← Command.execute() ← CommandManager
 * </pre>
 *
 * @see Command
 * @see CommandResult
 * @see AppController
 */
public interface State {

    /**
     * Create a command to open a map file.
     *
     * @param url The map file url to open
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createOpenMapCommand(URL url);

    /**
     * Create a command to open a deliveries demand file.
     *
     * @param url The deliveries file url to open
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createOpenDeliveriesCommand(URL url);

    /**
     * Create a command to add a delivery.
     *
     * @param delivery The delivery to add
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createAddDeliveryCommand(Delivery delivery);

    /**
     * Create a command to remove a delivery.
     *
     * @param delivery The delivery to remove
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createRemoveDeliveryCommand(Delivery delivery);

    /**
     * Select an intersection.
     * Shows an error dialog if the state is not StateSelectIntersection.
     *
     * @param intersection The intersection to select. Can be null.
     */
    void selectIntersection(Intersection intersection);

    /**
     * Create a request intersection, switching to the StateSelectIntersection.
     * Shows an error dialog if the state is not StateDeliveriesLoaded.
     */
    void requestIntersectionSelection();


    /**
     * Request to calculate the tour for a specific courier, or for all couriers if the provided courier is null.
     *
     * @param courier The courier to calculate the tour for, or null for all couriers.
     */
    void requestCalculateTour(Courier courier);

    /**
     * Create a command to assign a courier to a delivery.
     *
     * @param delivery The delivery to assign
     * @param courier  The courier to assign to the delivery
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createAssignCourierCommand(Delivery delivery, Courier courier);

    /**
     * Create a command to add a courier to the system.
     *
     * @param courier The courier to add
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createAddCourierCommand(Courier courier);

    /**
     * Create a command to remove a courier from the system.
     *
     * @param courier The courier to remove
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createRemoveCourierCommand(Courier courier);

    /**
     * Get the name of the current state for debugging/logging purposes.
     *
     * @return The state name
     */
    String getStateName();

    /**
     * Saves the current tour solution to a file.
     * The behavior may vary depending on the state.
     *
     * @param filename The path to the file where the tour will be saved.
     */
    void saveTour(String filename);

    /**
     * Creates a command to load a tour solution from a file.
     *
     * @param sourceCityMap          The city map before loading, for undo.
     * @param sourceDeliveriesDemand The deliveries demand before loading, for undo.
     * @param sourceCouriers         The list of couriers before loading, for undo.
     * @param filename               The path to the file to load the tour solution from.
     * @return A {@link CommandResult} containing the load command or an error.
     */
    CommandResult createLoadTourCommand(CityMap sourceCityMap, DeliveriesDemand sourceDeliveriesDemand, String filename);
}
