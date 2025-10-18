package fr.delivrooom.application.model;

import java.util.List;

public class Path {
    protected final List<Road> intersections;
    protected final float totalLength;
    protected final float totalTime;

    public Path(List<Road> intersections, float totalLength, float totalTime) {
        this.intersections = intersections;
        this.totalLength = totalLength;
        this.totalTime = totalTime;
    }

    public List<Road> getIntersections() {
        return intersections;
    }

    public float getTotalLength() {
        return totalLength;
    }

    public float getTotalTime() {
        return totalTime;
    }

    @Override
    public String toString() {
        return "Path{" +
                "intersections=" + intersections +
                ", totalLength=" + totalLength +
                '}';
    }
}
