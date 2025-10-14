package fr.delivrooom.application.model;

import java.util.HashMap;
import java.util.List;

public class CityMap {

    private final HashMap<Long, Intersection> intersections;
    private final HashMap<Long, HashMap<Long, Road>> roads;

    public CityMap(HashMap<Long, Intersection> intersections, HashMap<Long, HashMap<Long, Road>> roads) {
        this.intersections = intersections;
        this.roads = roads;
    }

    public HashMap<Long, Intersection> getIntersections() {
        return intersections;
    }

    public HashMap<Long, HashMap<Long, Road>> getRoads() {
        return roads;
    }

    public boolean HasRoad(long origin, long destination) {
        return (roads.containsKey(origin) && roads.get(origin).containsKey(destination));
    }

    public Road getRoad(long origin, long destination) {
        return  roads.get(origin).get(destination);
    }
}
