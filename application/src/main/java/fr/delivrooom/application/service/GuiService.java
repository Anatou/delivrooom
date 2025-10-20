package fr.delivrooom.application.service;

import fr.delivrooom.application.model.*;
import fr.delivrooom.application.port.in.GuiUseCase;
import fr.delivrooom.application.port.out.CityMapRepository;
import fr.delivrooom.application.port.out.DeliveriesRepository;

import java.net.URL;

public record GuiService(CityMapRepository cityMapRepository, DeliveriesRepository deliveriesRepository) implements GuiUseCase {

    @Override
    public CityMap getCityMap(URL mapURL) throws Exception {
        return cityMapRepository.getCityMap(mapURL);
    }

    @Override
    public DeliveriesDemand getDeliveriesDemand(CityMap cityMap, URL deliveriesURL) throws Exception {
        return deliveriesRepository.getDeliveriesDemand(cityMap, deliveriesURL);
    }

    @Override
    public TourSolution getTourSolution(CityMap cityMap, DeliveriesDemand deliveriesDemand) {
        CityGraph cityGraph = new CityGraph(cityMap);
        TourCalculator tourCalculator = new TourCalculator(cityGraph);

        if (tourCalculator.doesCalculatedTourNeedsToBeChanged(deliveriesDemand)) {
            tourCalculator.findOptimalTour(deliveriesDemand, false);
        }

        return tourCalculator.getOptimalTour();
    }
}
