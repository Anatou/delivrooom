package fr.delivrooom.application.port.out;
import fr.delivrooom.application.model.CityMap;

public interface CityMapRepository {
    CityMap getCityMap(String mapName);
}
