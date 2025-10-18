package fr.delivrooom.application.model;

import java.util.List;

public class TourSolution {
    protected final List<Path> paths;
    protected final float totalLength;
    protected final List<Long> deliveryOrder; // List of delivery IDs in the order they are served

    public TourSolution(List<Path> paths, float totalLength, List<Long> deliveryOrder) {
        this.paths = paths;
        this.totalLength = totalLength;
        this.deliveryOrder = deliveryOrder;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public float getTotalLength() {
        return totalLength;
    }

    public List<Long> getDeliveryOrder() {
        return deliveryOrder;
    }

    @Override
    public String toString() {
        return "TourSolution{" +
                "paths=" + paths +
                ", totalLength=" + totalLength +
                '}';
    }
}

