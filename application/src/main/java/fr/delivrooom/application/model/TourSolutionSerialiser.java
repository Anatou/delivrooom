package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * A record used to serialize the entire state of a calculated tour,
 * including the map, the delivery demand, and the couriers with their solutions.
 * This allows for saving and loading the application state.
 *
 * @param cityMap              The city map.
 * @param demand               The delivery demand.
 * @param couriersAndSolutions A list of couriers, each containing their assigned deliveries and calculated tour.
 */
public record TourSolutionSerialiser(CityMap cityMap, DeliveriesDemand demand, List<Courier> couriersAndSolutions) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public CityMap cityMap() {
        return cityMap;
    }

    @Override
    public DeliveriesDemand demand() {
        return demand;
    }

    @Override
    public List<Courier> couriersAndSolutions() {
        return couriersAndSolutions;
    }

    @Override
    public String toString() {
        return "TourSolutionSerialiser{" +
                "cityMap=" + cityMap +
                ", demand=" + demand +
                ", couriersAndSolutions=" + couriersAndSolutions +
                '}';
    }
}

