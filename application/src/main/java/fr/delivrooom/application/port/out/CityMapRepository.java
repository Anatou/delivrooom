package fr.delivrooom.application.port.out;
import fr.delivrooom.application.model.CityMap;

import java.net.URL;

/**
 * Defines the output port for loading city map data.
 * This interface is implemented by the adapter layer to provide the application
 * with a way to fetch map data from a specific source (e.g., an XML file).
 */
public interface CityMapRepository {
    /**
     * Loads a city map from a given URL.
     *
     * @param mapURL The URL of the map file.
     * @return A {@link CityMap} object.
     * @throws Exception if an error occurs during loading or parsing.
     */
    CityMap getCityMap(URL mapURL) throws Exception;
}
