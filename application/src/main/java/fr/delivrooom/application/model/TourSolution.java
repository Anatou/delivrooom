package fr.delivrooom.application.model;

import java.util.List;

/**
 * @param deliveryOrder List of delivery IDs in the order they are served
 */
public record TourSolution(List<Path> paths, float totalLength, List<Long> deliveryOrder) {

    @Override
    public String toString() {
        return "TourSolution{" +
                "paths=" + paths +
                ", totalLength=" + totalLength +
                '}';
    }
}

