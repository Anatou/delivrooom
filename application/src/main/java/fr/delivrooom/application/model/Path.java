package fr.delivrooom.application.model;

import java.util.List;

public record Path(List<Road> intersections, float totalLength, float totalTime) {

    @Override
    public String toString() {
        return "Path{" +
                "intersections=" + intersections +
                ", totalLength=" + totalLength +
                '}';
    }
}
