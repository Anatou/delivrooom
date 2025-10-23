package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;

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
     * Create a command to select an intersection.
     *
     * @param intersection The intersection to select
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createSelectIntersectionCommand(Intersection intersection);

    /**
     * Create a command to request intersection selection mode.
     *
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createRequestIntersectionSelectionCommand();

    /**
     * Create a command to calculate the tour for all deliveries.
     *
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createCalculateTourCommand();

    /**
     * Create a command to calculate the tour for a specific courier.
     *
     * @param courier The courier to calculate the tour for
     * @return A CommandResult with either a command or an error message
     */
    CommandResult createCalculateCourierTourCommand(Courier courier);

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
}
