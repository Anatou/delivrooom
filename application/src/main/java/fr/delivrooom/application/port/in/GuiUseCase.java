package fr.delivrooom.application.port.in;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;

import java.net.URL;

public interface GuiUseCase {
    CityMap getCityMap(URL mapURL) throws Exception;

    DeliveriesDemand getDeliveriesDemand(CityMap cityMapURL, URL deliveriesURL) throws Exception;

    TourSolution getTourSolution(CityMap cityMap, DeliveriesDemand deliveriesDemand);
}
