package fr.delivrooom.application.model;

public class Courier {
    private final int id;
    private DeliveriesDemand deliveriesDemand;

    public Courier(int id, DeliveriesDemand deliveriesDemand) {
        this.id = id;
        this.deliveriesDemand = deliveriesDemand;
    }
}
