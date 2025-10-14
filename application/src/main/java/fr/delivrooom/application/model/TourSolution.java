package fr.delivrooom.application.model;

import java.util.List;

public class TourSolution {
    protected List<Path> paths;
    protected float totalLength;

    public TourSolution(List<Path> paths, float totalLength) {
        this.paths = paths;
        this.totalLength = totalLength;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public float getTotalLength() {
        return totalLength;
    }
}

