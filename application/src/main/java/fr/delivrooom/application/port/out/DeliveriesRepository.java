package fr.delivrooom.application.port.out;
import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;

import java.net.URL;

public interface DeliveriesRepository {
    DeliveriesDemand getDeliveriesDemand(CityMap cityMap, URL deliveriesURL);

}
