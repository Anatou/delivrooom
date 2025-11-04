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

    @Override
    public CityMap getCityMap(URL mapURL) throws Exception {
        return cityMapRepository.getCityMap(mapURL);
    }

    @Override
    public DeliveriesDemand getDeliveriesDemand(CityMap cityMap, URL deliveriesURL) throws Exception {
        return deliveriesRepository.getDeliveriesDemand(cityMap, deliveriesURL);
    }
}
