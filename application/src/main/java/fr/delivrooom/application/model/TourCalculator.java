package fr.delivrooom.application.model;

public interface TourCalculator {
    void findOptimalTour(DeliveriesDemand demand);

    TourSolution getOptimalTour();

    float getTourLength();

    boolean doesCalculatedTourNeedsToBeChanged(DeliveriesDemand demand);

}
