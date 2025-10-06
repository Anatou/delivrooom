package fr.delivrooom.application.model;

import java.util.List;

public class Path {
    protected List<Intersection> intersections;
    protected int length;

    public Path(List<Intersection> intersections, int length) {
        this.intersections = intersections;
        this.length = length;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public int getLength() {
        return length;
    }
}
