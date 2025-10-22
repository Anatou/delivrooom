package fr.delivrooom.application.model;

public class Courier {
    private final int id;
    private DeliveriesDemand deliveriesDemand;
    private TourSolution tourSolution;

    public Courier(int id, DeliveriesDemand deliveriesDemand) {
        this.id = id;
        this.deliveriesDemand = deliveriesDemand;
        this.tourSolution = null;
    }

    public TourSolution getTourSolution() {
        return tourSolution;
    }

    public int getId() {
        return id;
    }

}
