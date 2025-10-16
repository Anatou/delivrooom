package fr.delivrooom.application.model;

import java.util.HashMap;

public record CityMap(HashMap<Long, Intersection> intersections, HashMap<Long, HashMap<Long, Road>> roads) {

    public boolean HasRoad(long origin, long destination) {
        return (roads.containsKey(origin) && roads.get(origin).containsKey(destination));
    }

    public Road getRoad(long origin, long destination) {
        return roads.get(origin).get(destination);
    }
}
