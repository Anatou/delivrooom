package fr.delivrooom.application.model;

public interface TourCalculator {
    public void findOptimalTour(DeliveriesDemand demand);
    public TourSolution getOptimalTour();
    public float getTourLength();
    public boolean doesCalculatedTourNeedsToBeChanged(DeliveriesDemand demand);

}
