package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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

