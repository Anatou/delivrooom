// java
package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a point on the map, typically a road intersection.
 * It stores geographical coordinates (latitude, longitude) as well as normalized
 * coordinates for easier rendering on a 2D plane.
 */
public class Intersection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final long id;
    protected final double latitude;
    protected final double longitude;
    protected final double normalizedX;
    protected final double normalizedY;
    protected float timeArrivedSeconds;

    /**
     * Constructs a new Intersection.
     *
     * @param id        The unique identifier of the intersection.
     * @param latitude  The latitude of the intersection.
     * @param longitude The longitude of the intersection.
     */
    public Intersection(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        double mercatorX = Math.toRadians(longitude);
        double mercatorY = Math.log(Math.tan(Math.toRadians(latitude)) + (1 / Math.cos(Math.toRadians(latitude))));
        this.normalizedX = (1 + (mercatorX / Math.PI)) / 2;
        this.normalizedY = (1 - (mercatorY / Math.PI)) / 2;
        this.timeArrivedSeconds = -1f;
    }

    /**
     * @return The normalized X coordinate (between 0 and 1).
     */
    public double getNormalizedX() {
        return normalizedX;
    }

    /**
     * @return The normalized Y coordinate (between 0 and 1).
     */
    public double getNormalizedY() {
        return normalizedY;
    }

    /**
     * @return The unique ID of the intersection.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The latitude of the intersection.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return The longitude of the intersection.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Calculates the distance in meters to another intersection using the Haversine formula.
     *
     * @param other The other intersection.
     * @return The distance in meters.
     */
    public double distanceTo(Intersection other) {
        final double R = 6371000; // Earth radius in meters
        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(other.latitude);
        double deltaLatRad = Math.toRadians(other.latitude - this.latitude);
        double deltaLngRad = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLngRad / 2) * Math.sin(deltaLngRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * @return The arrival time at this intersection in seconds, or -1 if not set.
     */
    public float getTimeArrivedSeconds() {
        return timeArrivedSeconds;
    }

    /**
     * Sets the arrival time at this intersection.
     *
     * @param timeArrivedSeconds The arrival time in seconds.
     */
    public void setTimeArrivedSeconds(float timeArrivedSeconds) {
        this.timeArrivedSeconds = timeArrivedSeconds;
    }

    /**
     * @return The arrival time at this intersection in minutes, or -1 if not set.
     */
    public float getTimeArrivedMinutes() {
        return timeArrivedSeconds < 0 ? -1f : timeArrivedSeconds / 60f;
    }

    /**
     * @return The arrival time formatted as HH:mm, or "N/A" if not set. Assumes a starting time of 17:00.
     */
    public String getFormattedTimeArrived() {
        if (timeArrivedSeconds < 0) {
            return "N/A";
        }
        int totalMinutes = (int) (getTimeArrivedMinutes());
        int hours = 8 + (totalMinutes / 60);
        int minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}
