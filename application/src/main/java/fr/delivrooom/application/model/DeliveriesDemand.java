package fr.delivrooom.application.model;

import java.util.Collection;
import java.util.HashSet;
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

    public int arePickups(Collection<Long> intersections) {
        // returns how many given intersections are pickups
        int count = 0;
        for (Delivery d : this.deliveries) {
            if (intersections.contains(d.takeoutIntersection().getId())) {
                ++count;
            }
        }
        return count;
    }
}
