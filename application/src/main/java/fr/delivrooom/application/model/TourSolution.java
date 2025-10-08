package fr.delivrooom.application.model;

import java.util.List;

public class TourSolution {
    protected List<Intersection> intersectionsOrder;
    protected DeliveriesDemand deliveriesDemand;

    public TourSolution(List<Intersection> intersectionsOrder, DeliveriesDemand deliveriesDemand) {
        this.intersectionsOrder = intersectionsOrder;
        this.deliveriesDemand = deliveriesDemand;
    }

    public List<Intersection> getIntersectionsOrder() {
        return intersectionsOrder;
    }

    public DeliveriesDemand getDeliveriesDemand(CityMap cityMap, String deliveriesName) {
        return deliveriesDemand;
    }
}

