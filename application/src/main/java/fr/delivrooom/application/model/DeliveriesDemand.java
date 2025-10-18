package fr.delivrooom.application.model;

import java.util.List;

public record DeliveriesDemand(List<Delivery> deliveries, Intersection store) {

    public Delivery getDeliveryByIds(int idTakeOutIntersection, int idDeliveryIntersection) {
        for (Delivery d : this.deliveries) {
            if (d.takeoutIntersection().getId() == idTakeOutIntersection && d.deliveryIntersection().getId() == idDeliveryIntersection) {
                return d;
            }
        }
        return null;
    }

    public List<Delivery> getDeliveries(){
        return this.deliveries;
    }

    public Intersection getStore(){
        return this.store;
    }
}
