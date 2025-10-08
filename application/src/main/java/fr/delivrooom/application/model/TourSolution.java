package fr.delivrooom.application.model;

import java.util.List;

public class TourSolution {
    protected List<Path> paths;
    protected float totalLentgh;

    public TourSolution(List<Path> paths, float totalLentgh) {
        this.paths = paths;
        this.totalLentgh = totalLentgh;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public float getTotalLentgh() {
        return totalLentgh;
    }
}

