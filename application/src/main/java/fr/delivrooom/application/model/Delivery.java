package fr.delivrooom.application.model;

import java.util.Objects;

public class Delivery {

    private final Intersection takeoutIntersection;
    private final Intersection deliveryIntersection;
    private final int takeoutDuration;
    private final int deliveryDuration;

    public Delivery(Intersection takeoutIntersection, Intersection deliveryIntersection,
                   int takeoutDuration, int deliveryDuration) {
        this.takeoutIntersection = takeoutIntersection;
        this.deliveryIntersection = deliveryIntersection;
        this.takeoutDuration = takeoutDuration;
        this.deliveryDuration = deliveryDuration;
    }

    public Intersection getTakeoutIntersection() {
        return takeoutIntersection;
    }

    public Intersection getDeliveryIntersection() {
        return deliveryIntersection;
    }

    public int getTakeoutDuration() {
        return takeoutDuration;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Delivery)) return false;
        Delivery d = (Delivery) o;
        return takeoutIntersection.equals(d.takeoutIntersection)
                && deliveryIntersection.equals(d.deliveryIntersection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(takeoutIntersection, deliveryIntersection);
    }

}
