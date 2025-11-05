package fr.delivrooom.application.service;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.port.in.GuiUseCase;
import fr.delivrooom.application.port.out.CityMapRepository;
import fr.delivrooom.application.port.out.DeliveriesRepository;

import java.net.URL;

/**
 * Service for handling GUI-related use cases, such as loading data.
 * This class implements the {@link GuiUseCase} and uses repositories to fetch data.
 *
 * @param cityMapRepository    The repository for loading city map data.
 * @param deliveriesRepository The repository for loading delivery demand data.
 */
public record GuiService(CityMapRepository cityMapRepository, DeliveriesRepository deliveriesRepository) implements GuiUseCase {

    /**
     * Loads a {@link CityMap} from the given URL using the configured repository.
     *
     * @param mapURL the URL pointing to the city map resource; must not be {@code null}
     * @return the parsed {@link CityMap}
     * @throws Exception if the resource cannot be read or parsed
     */
    @Override
    public CityMap getCityMap(URL mapURL) throws Exception {
        return cityMapRepository.getCityMap(mapURL);
    }

    /**
     * Loads a {@link DeliveriesDemand} for the provided {@link CityMap} from the given URL.
     *
     * @param cityMap       the city map used to validate and resolve intersections; must not be {@code null}
     * @param deliveriesURL the URL pointing to the deliveries demand resource; must not be {@code null}
     * @return the parsed {@link DeliveriesDemand}
     * @throws Exception if the resource cannot be read, parsed, or is inconsistent with the map
     */
    @Override
    public DeliveriesDemand getDeliveriesDemand(CityMap cityMap, URL deliveriesURL) throws Exception {
        return deliveriesRepository.getDeliveriesDemand(cityMap, deliveriesURL);
    }
}
