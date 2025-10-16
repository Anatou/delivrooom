package fr.delivrooom.application.model;

import java.util.List;
import java.util.Objects;

public class DeliveriesDemand {

    private final List<Delivery> deliveries;
    private final Intersection store;

    public DeliveriesDemand(List<Delivery> deliveries, Intersection store) {
        this.deliveries = deliveries;
        this.store = store;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveriesDemand)) return false;
        DeliveriesDemand that = (DeliveriesDemand) o;
        if (!Objects.equals(store == null ? null : store.getId(), that.store == null ? null : that.store.getId()))
            return false;
        if (deliveries == null && that.deliveries == null) return true;
        if (deliveries == null || that.deliveries == null) return false;
        if (deliveries.size() != that.deliveries.size()) return false;
        for (int i = 0; i < deliveries.size(); i++) {
            Delivery a = deliveries.get(i);
            Delivery b = that.deliveries.get(i);
            if (a == null && b == null) continue;
            if (a == null || b == null) return false;
            Long aTake = a.getTakeoutIntersection() == null ? null : a.getTakeoutIntersection().getId();
            Long bTake = b.getTakeoutIntersection() == null ? null : b.getTakeoutIntersection().getId();
            Long aDel = a.getDeliveryIntersection() == null ? null : a.getDeliveryIntersection().getId();
            Long bDel = b.getDeliveryIntersection() == null ? null : b.getDeliveryIntersection().getId();
            if (!Objects.equals(aTake, bTake) || !Objects.equals(aDel, bDel)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(store == null ? null : store.getId());
        if (deliveries != null) {
            for (Delivery d : deliveries) {
                Long t = d == null || d.getTakeoutIntersection() == null ? null : d.getTakeoutIntersection().getId();
                Long de = d == null || d.getDeliveryIntersection() == null ? null : d.getDeliveryIntersection().getId();
                result = 31 * result + Objects.hash(t, de);
            }
        }
        return result;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public Intersection getStore() {
        return store;
    }

    public Delivery getDeliveryByIds (int idTakeOutIntersection, int idDeliveryIntersection){
        for (Delivery d : this.deliveries){
            if (d.getTakeoutIntersection().getId() == idTakeOutIntersection && d.getDeliveryIntersection().getId() == idDeliveryIntersection){
                return d;
            }
        }
        return null;
    }
}
