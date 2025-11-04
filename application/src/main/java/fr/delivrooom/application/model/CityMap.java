package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents a city map, containing all its intersections and roads.
 * This class is a record, making it an immutable data carrier.
 *
 * @param intersections A map of intersection IDs to {@link Intersection} objects.
 * @param roads         A nested map representing the road network. The outer key is the origin intersection ID,
 *                      and the inner map's key is the destination intersection ID, with the value being the {@link Road} object.
 */
public record CityMap(HashMap<Long, Intersection> intersections, HashMap<Long, HashMap<Long, Road>> roads) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Checks if a direct road exists between two intersections.
     *
     * @param origin      The ID of the origin intersection.
     * @param destination The ID of the destination intersection.
     * @return {@code true} if a road exists, {@code false} otherwise.
     */
    public boolean hasRoad(long origin, long destination) {
        return (roads.containsKey(origin) && roads.get(origin).containsKey(destination));
    }

    /**
     * Retrieves the road between two intersections.
     *
     * @param origin      The ID of the origin intersection.
     * @param destination The ID of the destination intersection.
     * @return The {@link Road} object, or {@code null} if no direct road exists.
     */
    public Road getRoad(long origin, long destination) {
        return roads.get(origin).get(destination);
    }
}
