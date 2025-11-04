package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a directed road segment between two intersections.
 */
public class Road implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final Intersection origin;
    protected final Intersection destination;
    protected final float length;
    protected final String name;

    /**
     * Constructs a new Road.
     *
     * @param origin      The starting intersection.
     * @param destination The ending intersection.
     * @param length      The length of the road in meters.
     * @param name        The name of the road.
     */
    public Road(Intersection origin, Intersection destination, float length, String name) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.name = name;
    }

    /**
     * @return The origin intersection of the road.
     */
    public Intersection getOrigin() {
        return origin;
    }

    /**
     * @return The destination intersection of the road.
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * @return The length of the road in meters.
     */
    public float getLength() {
        return length;
    }

    /**
     * @return The name of the road.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Road{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", length=" + length +
                ", name='" + name + '\'' +
                '}';
    }
}
