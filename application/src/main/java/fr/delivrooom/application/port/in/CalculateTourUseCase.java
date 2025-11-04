package fr.delivrooom.application.port.in;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;

/**
 * Defines the input port for tour calculation operations.
 * This interface is implemented by the application's core logic (service layer)
 * and used by the adapter layer (e.g., the GUI) to request tour calculations.
 */
public interface CalculateTourUseCase {
    /**
     * Retrieves the most recently calculated optimal tour.
     *
     * @return The calculated {@link TourSolution}, or {@code null} if no tour has been calculated.
     */
    TourSolution getOptimalTour();

    /**
     * Calculates the optimal tour for a given set of deliveries.
     *
     * @param demand        The deliveries to be included in the tour.
     * @param useTimeAsCost If {@code true}, the cost of a path is its travel time; otherwise, it's its length.
     * @throws RuntimeException if the tour cannot be calculated (e.g., due to a disconnected graph).
     */
    void findOptimalTour(DeliveriesDemand demand, boolean useTimeAsCost) throws RuntimeException;

    /**
     * Provides the city map to the tour calculation service.
     *
     * @param m The {@link CityMap} to be used for calculations.
     */
    void provideCityMap(CityMap m);

    /**
     * Checks if the tour needs to be recalculated for the given demand.
     * This is typically true if the demand has changed since the last calculation
     * or if the underlying map has been modified.
     *
     * @param demand The current deliveries demand.
     * @return {@code true} if a recalculation is needed, {@code false} otherwise.
     */
    boolean doesCalculatedTourNeedsToBeChanged(DeliveriesDemand demand);
}
