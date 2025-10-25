// java
package fr.delivrooom.application.model;

public class Intersection {

    protected final long id;
    protected final double latitude;
    protected final double longitude;
    protected final double normalizedX;
    protected final double normalizedY;
    protected float timeArrivedSeconds;

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

    public double getNormalizedX() {
        return normalizedX;
    }

    public double getNormalizedY() {
        return normalizedY;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double distanceTo(Intersection other) {
        final double R = 6371000;
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


    public float getTimeArrivedSeconds() {
        return timeArrivedSeconds;
    }

    public void setTimeArrivedSeconds(float timeArrivedSeconds) {
        this.timeArrivedSeconds = timeArrivedSeconds;
    }

    public float getTimeArrivedMinutes() {
        return timeArrivedSeconds < 0 ? -1f : timeArrivedSeconds / 60f;
    }

    public String getFormattedTimeArrived() {
        if (timeArrivedSeconds < 0) {
            return "N/A";
        }
        int totalMinutes = (int) (getTimeArrivedMinutes());
        int hours = 17 + (totalMinutes / 60);
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
