package fr.delivrooom.application.model;

import java.util.List;

public class Path {
    protected final List<Road> intersections;
    protected final float totalLength;

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

    @Override
    public String toString() {
        return "Path{" +
                "intersections=" + intersections +
                ", totalLength=" + totalLength +
                '}';
    }
}
