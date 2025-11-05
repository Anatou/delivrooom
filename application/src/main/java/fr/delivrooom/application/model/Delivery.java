package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a single delivery, including the pickup and drop-off locations and the time
 * required at each stop. This class is a record, making it an immutable data carrier.
 *
 * @param takeoutIntersection  The pickup location.
 * @param deliveryIntersection The drop-off location.
 * @param takeoutDuration      The time in seconds required for pickup.
 * @param deliveryDuration     The time in seconds required for drop-off.
 */
public record Delivery(Intersection takeoutIntersection, Intersection deliveryIntersection, int takeoutDuration, int deliveryDuration) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
