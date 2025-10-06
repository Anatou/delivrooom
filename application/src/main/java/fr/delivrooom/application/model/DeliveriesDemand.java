package fr.delivrooom.application.model;

import java.util.List;

public class DeliveriesDemand {

    private final List<Delivery> deliveries;
    private final Intersection store;

    public DeliveriesDemand(List<Delivery> deliveries, Intersection store) {
        this.deliveries = deliveries;
        this.store = store;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public Intersection getStore() {
        return store;
    }
}
