package fr.delivrooom.application.port.in;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;

public interface GuiUseCase {
    CityMap getCityMap(String mapName);
    DeliveriesDemand getDeliveriesDemand(CityMap citymap, String deliveriesName);
    TourSolution getTourSolution();
}
