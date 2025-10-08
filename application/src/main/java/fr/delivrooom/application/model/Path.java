package fr.delivrooom.application.model;

import java.util.List;

public class Path {
    protected List<Road> intersections;
    protected float totalLength;

    public Path(List<Road> intersections, float totalLength) {
        this.intersections = intersections;
        this.totalLength = totalLength;
    }

    public List<Road> getIntersections() {
        return intersections;
    }

    public float getTotalLength() {
        return totalLength;
    }
}
