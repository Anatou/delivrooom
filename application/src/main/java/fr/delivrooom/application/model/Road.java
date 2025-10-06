package fr.delivrooom.application.model;

public class Road {

    protected Intersection origin;
    protected Intersection destination;
    protected float length;
    protected String name;

    public Road(Intersection origin, Intersection destination, float length, String name) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.name = name;
    }

    public Intersection getOrigin() {
        return origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public float getLength() {
        return length;
    }

    public String getName() {
        return name;
    }
}
