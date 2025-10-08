package fr.delivrooom.application.port.out;
import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;

public interface DeliveriesRepository {
    DeliveriesDemand getDeliveriesDemand(CityMap cityMap, String deliveriesName);

}
