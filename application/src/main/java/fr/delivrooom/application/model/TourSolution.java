package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a calculated tour for a set of deliveries.
 * This class is a record, making it an immutable data carrier.
 *
 * @param paths         The list of {@link Path} objects that make up the tour.
 * @param totalLength   The total length of the tour in meters.
 * @param deliveryOrder A list of intersection IDs in the order they are visited in the tour.
 */
public record TourSolution(List<Path> paths, float totalLength, List<Long> deliveryOrder) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "TourSolution{" +
                //"paths=" + paths +
                ", totalLength=" + totalLength +
                ", deliveryOrder=" + deliveryOrder +
                '}';
    }

}

