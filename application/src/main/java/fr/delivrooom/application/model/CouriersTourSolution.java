package fr.delivrooom.application.model;

import java.util.HashMap;

public record CouriersTourSolution(HashMap<Integer, TourSolution> couriersTours) {
    @Override
    public String toString() {
        return "CouriersTourSolution{" +
                "couriersTours=" + couriersTours +
                '}';
    }
}
