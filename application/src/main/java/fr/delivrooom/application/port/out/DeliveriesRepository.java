package fr.delivrooom.application.port.out;
import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;

import java.net.URL;

/**
 * Defines the output port for loading delivery demand data.
 * This interface is implemented by the adapter layer to provide the application
 * with a way to fetch delivery data from a specific source (e.g., an XML file).
 */
public interface DeliveriesRepository {
    /**
     * Loads a deliveries demand from a given URL, associating it with a city map.
     *
     * @param cityMap       The city map to which the deliveries belong.
     * @param deliveriesURL The URL of the deliveries file.
     * @return A {@link DeliveriesDemand} object.
     * @throws Exception if an error occurs during loading or parsing.
     */
    DeliveriesDemand getDeliveriesDemand(CityMap cityMap, URL deliveriesURL) throws Exception;

}
