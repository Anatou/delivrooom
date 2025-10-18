package fr.delivrooom.application.model;

public record Delivery(Intersection takeoutIntersection, Intersection deliveryIntersection, int takeoutDuration, int deliveryDuration) {

    public Intersection getTakeoutIntersection(){
        return takeoutIntersection;
    }

    public Intersection getDeliveryIntersection(){
        return deliveryIntersection;
    }
}
