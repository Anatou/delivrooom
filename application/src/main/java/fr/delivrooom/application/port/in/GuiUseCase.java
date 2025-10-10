package fr.delivrooom.application.port.in;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;

import java.net.URL;

public interface GuiUseCase {
    CityMap getCityMap(URL mapURL);

    DeliveriesDemand getDeliveriesDemand(CityMap cityMapURL, URL deliveriesURL);
    TourSolution getTourSolution();
}
