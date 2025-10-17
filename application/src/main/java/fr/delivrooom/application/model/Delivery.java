package fr.delivrooom.application.model;

import java.util.Objects;

public record Delivery(Intersection takeoutIntersection, Intersection deliveryIntersection, int takeoutDuration, int deliveryDuration) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Delivery d)) return false;
        return takeoutIntersection.equals(d.takeoutIntersection)
                && deliveryIntersection.equals(d.deliveryIntersection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(takeoutIntersection, deliveryIntersection);
    }

    public Intersection getTakeoutIntersection() {
        return takeoutIntersection;
    }

    public Intersection getDeliveryIntersection() {
        return deliveryIntersection;
    }

    @Override
    public int takeoutDuration() {
        return takeoutDuration;
    }

    @Override
    public int deliveryDuration() {
        return deliveryDuration;
    }
}
