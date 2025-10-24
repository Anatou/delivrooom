package fr.delivrooom.application.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record TourSolutionSerialiser(CityMap cityMap, DeliveriesDemand demand, List<TourSolution> tourSolutionList) implements Serializable {
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
    public List<TourSolution> tourSolutionList() {
        return tourSolutionList;
    }
}
