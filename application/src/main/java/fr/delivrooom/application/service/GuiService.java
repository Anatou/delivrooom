package fr.delivrooom.application.service;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;
import fr.delivrooom.application.port.in.GuiUseCase;
import fr.delivrooom.application.port.out.CityMapRepository;
import fr.delivrooom.application.port.out.DeliveriesRepository;

public class GuiService implements GuiUseCase {

    private final CityMapRepository cityMapRepository;
    private final DeliveriesRepository deliveriesRepository;

    public GuiService(CityMapRepository cityMapRepository, DeliveriesRepository deliveriesRepository) {
        this.cityMapRepository = cityMapRepository;
        this.deliveriesRepository = deliveriesRepository;
    }

    @Override
    public CityMap getCityMap() {
        return cityMapRepository.getCityMap();
    }

    @Override
    public DeliveriesDemand getDeliveriesDemand() {
        return deliveriesRepository.getDeliveriesDemand();
    }

    @Override
    public TourSolution getTourSolution() {
        return null;
    }
}
