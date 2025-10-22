package fr.delivrooom.application.model;

import java.util.ArrayList;

public class Courier {
    private final int id;
    private DeliveriesDemand deliveriesDemand;
    private TourSolution tourSolution;

    public Courier(int id) {
        this.id = id;
        this.deliveriesDemand = null;
        this.tourSolution = null;
    }

    public TourSolution getTourSolution() {
        return tourSolution;
    }

    public int getId() {
        return id;
    }

    public DeliveriesDemand getDeliveriesDemand() {
        return deliveriesDemand;
    }

    public void setDeliveriesDemand(DeliveriesDemand deliveriesDemand) {
        deliveriesDemand = deliveriesDemand;
    }

    public void deleteTourSolution() {
        this.tourSolution = null;
    }

    public void addDelivery(Delivery delivery, Intersection store) {
        if (this.deliveriesDemand == null) {
            this.deliveriesDemand = new DeliveriesDemand(new ArrayList<>(), store);
        }
        this.deliveriesDemand.deliveries().add(delivery);
    }

    public void removeDelivery(Delivery delivery) {
        if (this.deliveriesDemand != null) {
            this.deliveriesDemand.deliveries().remove(delivery);
        }
    }

    @Override
    public String toString() {
        return "Courier " + id;
    }
}
