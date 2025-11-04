package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Represents a set of deliveries to be made, including the list of individual deliveries
 * and the central warehouse or store. This class is a record, making it an immutable data carrier.
 *
 * @param deliveries The list of {@link Delivery} objects.
 * @param store      The starting/ending {@link Intersection} for the deliveries (the warehouse).
 */
public record DeliveriesDemand(List<Delivery> deliveries, Intersection store) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Finds a delivery by the IDs of its pickup and delivery intersections.
     *
     * @param idTakeOutIntersection  The ID of the pickup intersection.
     * @param idDeliveryIntersection The ID of the delivery intersection.
     * @return The matching {@link Delivery}, or {@code null} if not found.
     */
    public Delivery getDeliveryByIds(int idTakeOutIntersection, int idDeliveryIntersection) {
        for (Delivery d : this.deliveries) {
            if (d.takeoutIntersection().getId() == idTakeOutIntersection && d.deliveryIntersection().getId() == idDeliveryIntersection) {
                return d;
            }
        }
        return null;
    }

    /**
     * Counts how many of the given intersection IDs correspond to pickup locations in this demand.
     *
     * @param intersections A collection of intersection IDs to check.
     * @return The number of intersections that are pickup locations.
     */
    public int arePickups(Collection<Long> intersections) {
        // returns how many given intersections are pickups
        int count = 0;
        for (Delivery d : this.deliveries) {
            if (intersections.contains(d.takeoutIntersection().getId())) {
                ++count;
            }
        }
        return count;
    }

    /**
     * Deletes a delivery from the list of deliveries.
     *
     * @param delivery The delivery to remove.
     */
    public void deleteDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
    }

}
