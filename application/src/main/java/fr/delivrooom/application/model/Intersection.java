package fr.delivrooom.application.model;

public class Intersection {

     protected long id;
     protected double latitude;
     protected double longitude;
     protected double normalizedX;
     protected double normalizedY;

     public Intersection(long id, double latitude, double longitude) {
         this.id = id;
         this.latitude = latitude;
         this.longitude = longitude;
         // Calculate normalized coordinates once at initialization for performance
         double mercatorX = Math.toRadians(longitude);
         double mercatorY = Math.log(Math.tan(Math.toRadians(latitude)) + (1 / Math.cos(Math.toRadians(latitude))));
         this.normalizedX = (1 + (mercatorX / Math.PI)) / 2;
         this.normalizedY = (1 - (mercatorY / Math.PI)) / 2;
     }
     /**
      * Get normalized X coordinate (0-1 range) for tile mapping
      * Pre-calculated at initialization for performance
      */
     public double getNormalizedX() {
         return normalizedX;
     }

     /**
      * Get normalized Y coordinate (0-1 range) for tile mapping
      * Pre-calculated at initialization for performance
      */
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

    /*
     * Compute distance between two GPS points on Earth
     * Result is in meters
     */
    public double distanceTo(Intersection other) {
        final double R = 6371000; // Earth's radius in meters

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
}
