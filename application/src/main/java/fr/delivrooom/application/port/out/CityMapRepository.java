package fr.delivrooom.application.port.out;
import fr.delivrooom.application.model.CityMap;

import java.net.URL;

public interface CityMapRepository {
    CityMap getCityMap(URL mapURL) throws Exception;
}
