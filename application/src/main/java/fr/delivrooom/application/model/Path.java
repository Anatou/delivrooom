package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
