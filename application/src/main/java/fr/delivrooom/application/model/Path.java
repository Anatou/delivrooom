package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a path between two intersections, consisting of a sequence of roads.
 * This class is a record, making it an immutable data carrier.
 *
 * @param intersections The list of {@link Road}s that make up the path.
 * @param totalLength   The total length of the path in meters.
 * @param totalTime     The total time to traverse the path in seconds.
 */
public record Path(List<Road> intersections, float totalLength, float totalTime) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Path{" +
                "intersections=" + intersections +
                ", totalLength=" + totalLength +
                '}';
    }
}
