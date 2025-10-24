package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public record CityMap(HashMap<Long, Intersection> intersections, HashMap<Long, HashMap<Long, Road>> roads) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public boolean hasRoad(long origin, long destination) {
        return (roads.containsKey(origin) && roads.get(origin).containsKey(destination));
    }

    public Road getRoad(long origin, long destination) {
        return roads.get(origin).get(destination);
    }
}
