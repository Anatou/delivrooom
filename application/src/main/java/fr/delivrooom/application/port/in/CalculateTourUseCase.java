package fr.delivrooom.application.port.in;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;

public interface CalculateTourUseCase {
    TourSolution getOptimalTour();
    void findOptimalTour(DeliveriesDemand demand, boolean useTimeAsCost) throws RuntimeException;
    void provideCityMap(CityMap m);
    boolean doesCalculatedTourNeedsToBeChanged(DeliveriesDemand demand);
}
