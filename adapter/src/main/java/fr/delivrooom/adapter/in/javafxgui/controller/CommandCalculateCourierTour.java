package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.CouriersTourSolution;
import fr.delivrooom.application.model.TourSolution;

import java.util.HashMap;
import java.util.Map;

/**
 * Command to calculate the optimal tour for a specific courier.
 * Supports undo by restoring the previous tour solution.
 */
public class CommandCalculateCourierTour implements Command {

    private final AppController controller;
    private final Courier courier;
    private TourSolution previousTourSolution;

    public CommandCalculateCourierTour(AppController controller, Courier courier) {
        this.controller = controller;
        this.courier = courier;
    }

    /**
     * Execute the command: capture the current aggregated tour solutions and
     * store the tour solution previously associated with this courier (if any),
     * then request calculation of a new tour for the courier.
     * <p>
     * The saved value is stored in {@code previousTourSolution} so the command
     * can be undone later by {@link #undo()}.
     */
    @Override
    public void execute() {
        CouriersTourSolution couriersTourSolution = controller.tourSolutionProperty().getValue();
        previousTourSolution = (couriersTourSolution == null)
                ? null
                : couriersTourSolution.couriersTours().get(courier.getId());
        controller.doCalculateTourForCourier(courier);
    }

    /**
     * Undo the command by restoring the previously saved tour solution for this courier.
     *
     * This method reads the current aggregated {@code CouriersTourSolution} (which may be null),
     * copies its internal map, and either removes the courier entry if {@code previousTourSolution}
     * is {@code null}, or restores the saved {@code TourSolution}. Finally it calls
     * {@code controller.doRestoreTourSolution(...)} with a new {@code CouriersTourSolution}
     * constructed from the modified map.
     */
    @Override
    public void undo() {
        CouriersTourSolution current = controller.tourSolutionProperty().getValue();
        Map<Integer, TourSolution> map = (current != null)
                ? new HashMap<>(current.couriersTours())
                : new HashMap<>();

        Integer id = courier.getId();
        if (previousTourSolution == null) {
            map.remove(id);
        } else {
            map.put(id, previousTourSolution);
        }

        controller.doRestoreTourSolution(new CouriersTourSolution(new HashMap<>(map)));
    }


    @Override
    public String toString() {
        return "Calculate tour for courier " + courier.getId();
    }
}
