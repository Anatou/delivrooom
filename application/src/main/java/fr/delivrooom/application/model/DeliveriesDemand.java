package fr.delivrooom.application.model;

import java.util.List;
import java.util.Objects;

public record DeliveriesDemand(List<Delivery> deliveries, Intersection store) {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveriesDemand that)) return false;
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
            Long aTake = a.takeoutIntersection() == null ? null : a.takeoutIntersection().getId();
            Long bTake = b.takeoutIntersection() == null ? null : b.takeoutIntersection().getId();
            Long aDel = a.deliveryIntersection() == null ? null : a.deliveryIntersection().getId();
            Long bDel = b.deliveryIntersection() == null ? null : b.deliveryIntersection().getId();
            if (!Objects.equals(aTake, bTake) || !Objects.equals(aDel, bDel)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(store == null ? null : store.getId());
        if (deliveries != null) {
            for (Delivery d : deliveries) {
                Long t = d == null || d.takeoutIntersection() == null ? null : d.takeoutIntersection().getId();
                Long de = d == null || d.deliveryIntersection() == null ? null : d.deliveryIntersection().getId();
                result = 31 * result + Objects.hash(t, de);
            }
        }
        return result;
    }

    public Delivery getDeliveryByIds(int idTakeOutIntersection, int idDeliveryIntersection) {
        for (Delivery d : this.deliveries) {
            if (d.takeoutIntersection().getId() == idTakeOutIntersection && d.deliveryIntersection().getId() == idDeliveryIntersection) {
                return d;
            }
        }
        return null;
    }
}
