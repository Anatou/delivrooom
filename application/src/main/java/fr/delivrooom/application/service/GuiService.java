package fr.delivrooom.application.service;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;
import fr.delivrooom.application.port.in.GuiUseCase;
import fr.delivrooom.application.port.out.CityMapRepository;

public class GuiService implements GuiUseCase {

    private final CityMapRepository cityMapRepository;

    public GuiService(CityMapRepository cityMapRepository) {
        this.cityMapRepository = cityMapRepository;
    }

    @Override
    public CityMap getCityMap() {
        return cityMapRepository.getCityMap();
    }

    @Override
    public DeliveriesDemand getDeliveriesDemand() {
        return null;
    }

    @Override
    public TourSolution getTourSolution() {
        return null;
    }
}
