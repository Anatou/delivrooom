package fr.delivrooom.application.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;


public class Courier {
    private final int id;
    private DeliveriesDemand deliveriesDemand;
    private TourSolution tourSolution;
    private final Color color;
    private boolean displayTourSolution;

    public Courier(int id) {
        this.id = id;
        this.deliveriesDemand = null;
        this.tourSolution = null;
//        this.color = new Color(Math.random(), Math.random(), Math.random(), 1);
        this.color = Color.hsb((id * 137) % 360, 0.7, 0.9);
        this.displayTourSolution = true;
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
        this.deliveriesDemand = deliveriesDemand;
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

    public Color getColor() {
        return color;
    }

    public boolean isDisplayTourSolution() {
        return displayTourSolution;
    }

    public void setDisplayTourSolution(boolean displayTourSolution) {
        this.displayTourSolution = displayTourSolution;
    }


    public void setTourSolution(TourSolution tourSolution) {
        this.tourSolution = tourSolution;
    }

    @Override
    public String toString() {
        return "Courier " + id;
    }
}
