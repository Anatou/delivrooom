package fr.delivrooom.application.model;

import java.util.List;

public class CityMap {

    private final List<Intersection> intersections;
    private final List<Road> roads;

    public CityMap(List<Intersection> intersections, List<Road> roads) {
        this.intersections = intersections;
        this.roads = roads;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public List<Road> getRoads() {
        return roads;
    }
}
