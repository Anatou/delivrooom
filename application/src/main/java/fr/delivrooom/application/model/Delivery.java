package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;

public record Delivery(Intersection takeoutIntersection, Intersection deliveryIntersection, int takeoutDuration, int deliveryDuration) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
