package fr.delivrooom.application.port.in;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;

import java.net.URL;

/**
 * Defines the input port for GUI-related use cases, such as loading map and delivery data.
 */
public interface GuiUseCase {
    /**
     * Loads a city map from a given URL.
     *
     * @param mapURL The URL of the map file.
     * @return A {@link CityMap} object.
     * @throws Exception if an error occurs during loading or parsing.
     */
    CityMap getCityMap(URL mapURL) throws Exception;

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
