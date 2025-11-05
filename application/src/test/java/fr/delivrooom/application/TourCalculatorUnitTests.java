// language: java
package fr.delivrooom.application;

import fr.delivrooom.application.model.*;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;
import fr.delivrooom.application.service.TourCalculatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Mock implementation of {@link NotifyTSPProgressToGui} for unit testing.
 * Verifies that the reported TSP progress percentage is strictly increasing.
 */
class NotifyTSPProgressMock implements NotifyTSPProgressToGui {
    Double lastPercentage;

    /**
     * Records the latest progress percentage and asserts it is strictly increasing.
     *
     * @param percentage the current TSP computation progress in \[0.0, 100.0]
     */
    @Override
    public void notifyTSPProgressToGui(double percentage) {
        if (lastPercentage != null) {
            Assertions.assertTrue(percentage > lastPercentage);
        }
        lastPercentage = percentage;
    }
}

/**
 * Unit tests for {@link TourCalculatorService}.
 */
public class TourCalculatorUnitTests {

    /**
     * Computes a 1-based linear intersection id from grid coordinates.
     *
     * @param x     the x index in the grid \[0..width-1]
     * @param y     the y index in the grid \[0..height-1]
     * @param width the total grid width
     * @return the 1-based intersection id
     */
    static Long getIdFromGridPos(int x, int y, int width) {
        return (long) y *width + x + 1;
    }

    /**
     * Builds a Manhattan-like grid {@link CityMap} of given dimensions.
     * Each node connects to its 4-neighbors with unit-length bidirectional roads.
     *
     * @param width  number of columns
     * @param height number of rows
     * @return a new {@link CityMap} instance
     */
    static CityMap getManhattanMap(int width, int height) {
        HashMap<Long, Intersection> intersections = new HashMap<>();
        for (int x=0; x < width; x++) {
            for (int y=0; y < height; y++) {
                long id = getIdFromGridPos(x,y,width);
                Intersection intersection = new Intersection(id, y, x);
                intersections.put(id, intersection);
            }
        }
        HashMap<Long, HashMap<Long, Road>> roadsFromInters = new HashMap<>();
        for (int x=0; x < width; x++) {
            for (int y=0; y < height; y++) {
                HashMap<Long, Road> roadsFromInter = new HashMap<>();
                Long fromId = getIdFromGridPos(x,y,width);
                Intersection fromInter = intersections.get(fromId);
                if (x<width-1) {
                    Long toId = getIdFromGridPos(x+1,y,width);
                    Intersection toInter = intersections.get(toId);
                    String roadName = "road"+fromId+"to"+toId;
                    Road road = new Road(fromInter, toInter, 1.0f, roadName);
                    roadsFromInter.put(toId, road);
                }
                if (y<height-1) {
                    Long toId = getIdFromGridPos(x,y+1,width);
                    Intersection toInter = intersections.get(toId);
                    String roadName = "road"+fromId+"to"+toId;
                    Road road = new Road(fromInter, toInter, 1.0f, roadName);
                    roadsFromInter.put(toId, road);
                }
                if (x>0) {
                    Long toId = getIdFromGridPos(x-1,y,width);
                    Intersection toInter = intersections.get(toId);
                    String roadName = "road"+fromId+"to"+toId;
                    Road road = new Road(fromInter, toInter, 1.0f, roadName);
                    roadsFromInter.put(toId, road);
                }
                if (y>0) {
                    Long toId = getIdFromGridPos(x,y-1,width);
                    Intersection toInter = intersections.get(toId);
                    String roadName = "road"+fromId+"to"+toId;
                    Road road = new Road(fromInter, toInter, 1.0f, roadName);
                    roadsFromInter.put(toId, road);
                }
                roadsFromInters.put(fromId, roadsFromInter);
            }
        }
        return new CityMap(intersections, roadsFromInters);
    }

    /**
     * Removes a directed road from the given city map.
     *
     * @param c         the original {@link CityMap}
     * @param fromInter origin intersection id
     * @param toInter   destination intersection id
     * @return a new {@link CityMap} instance without the specified directed road
     */
    static CityMap removeRoadFromCityMap(CityMap c, long fromInter, long toInter) {
        HashMap<Long, Intersection> intersections = c.intersections();
        HashMap<Long, HashMap<Long, Road>> roadsFromInters = c.roads();
        roadsFromInters.get(fromInter).remove(toInter);
        return new CityMap(intersections, roadsFromInters);
    }

    /**
     * Removes both directed roads between two intersections.
     *
     * @param c      the original {@link CityMap}
     * @param interA first intersection id
     * @param interB second intersection id
     * @return a new {@link CityMap} without the bidirectional connection
     */
    static CityMap removeBidirectionalRoadFromCityMap(CityMap c, long interA, long interB) {
        c = removeRoadFromCityMap(c, interA, interB);
        c = removeRoadFromCityMap(c, interB, interA);
        return c;
    }

    /**
     * Creates an {@link Intersection} with unknown coordinates for the given id.
     *
     * @param id the intersection id
     * @return a new {@link Intersection} with coordinates \(-1, -1\)
     */
    static Intersection makeIntersectionFromId(long id) {
        return new Intersection(id, -1,-1);
    }

    /**
     * Creates a {@link Delivery} with 1s pickup and 1s delivery durations.
     *
     * @param pickup  pickup intersection id
     * @param deposit delivery intersection id
     * @return a new {@link Delivery}
     */
    static Delivery makeDelivery(long pickup, long deposit) {
        return new Delivery(
                makeIntersectionFromId(pickup),
                makeIntersectionFromId(deposit),
                1,1
        );
    }

    /**
     * Ensures that with zero deliveries, no tour is computed and solution remains null.
     */
    @Test
    public void test_tsp_for_zero_delivery() {
        /* Ville utilisée
        1Wa
        */
        CityMap cityMap = getManhattanMap(1,1);
        Intersection inter_warehouse = new Intersection(1, -1, -1);
        List<Delivery> deliveries = new ArrayList<>();
        NotifyTSPProgressToGui notifyTSPProgressToGui = new NotifyTSPProgressMock();
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);
        TourCalculatorService tourCalculator = new TourCalculatorService(notifyTSPProgressToGui);
        tourCalculator.provideCityMap(cityMap);
        Assertions.assertFalse(tourCalculator.doesCalculatedTourNeedsToBeChanged(deliveriesDemand));
        if (tourCalculator.doesCalculatedTourNeedsToBeChanged(deliveriesDemand)) {
            tourCalculator.findOptimalTour(deliveriesDemand, false);
        }
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertNull(tourSolution);
    }

    /**
     * Verifies shortest tour length for a single delivery in a 2x2 grid.
     */
    @Test
    public void test_tsp_for_one_delivery_1() {
        /* Ville utilisée
        1Wa ══ 2P1
         ║      ║
        3D1 ══  4
        */
        CityMap cityMap = getManhattanMap(2,2);
        Intersection inter_pickup = new Intersection(2, -1, -1);
        Intersection inter_deposit = new Intersection(3, -1, -1);
        Intersection inter_warehouse = new Intersection(1, -1, -1);
        Delivery delivery = new Delivery(inter_pickup, inter_deposit, 1,1);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);
        NotifyTSPProgressToGui notifyTSPProgressToGui = new NotifyTSPProgressMock();

        TourCalculatorService tourCalculator = new TourCalculatorService(notifyTSPProgressToGui);
        tourCalculator.provideCityMap(cityMap);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(4.f, tourSolution.totalLength());
    }

    /**
     * Verifies tour length increases when a direct path is removed.
     */
    @Test
    public void test_tsp_for_one_delivery_2() {
        /* Ville utilisée
        1Wa    2P1
         ║      ║
        3D1 ══  4
        */
        CityMap cityMap = getManhattanMap(2,2);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 1, 2);
        Intersection inter_pickup = new Intersection(2, -1, -1);
        Intersection inter_deposit = new Intersection(3, -1, -1);
        Intersection inter_warehouse = new Intersection(1, -1, -1);
        Delivery delivery = new Delivery(inter_pickup, inter_deposit, 1,1);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);
        NotifyTSPProgressToGui notifyTSPProgressToGui = new NotifyTSPProgressMock();

        TourCalculatorService tourCalculator = new TourCalculatorService(notifyTSPProgressToGui);
        tourCalculator.provideCityMap(cityMap);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(6.f, tourSolution.totalLength());
    }

    /**
     * Verifies tour length on a larger 4x4 grid with a single delivery.
     */
    @Test
    public void test_tsp_for_one_delivery_3() {
        /* Ville utilisée
        1Wa ══  2  ══  3  ══  4
         ║      ║      ║      ║
         5  ══  6  ══ 7P1  ══  8
         ║      ║      ║      ║
         9  ══ 10  ══ 11  ══ 12
         ║      ║      ║      ║
        13  ══14D1  ══ 15  ══ 12
        */
        CityMap cityMap = getManhattanMap(4,4);
        Intersection inter_pickup = new Intersection(7, -1, -1);
        Intersection inter_deposit = new Intersection(14, -1, -1);
        Intersection inter_warehouse = new Intersection(1, -1, -1);
        Delivery delivery = new Delivery(inter_pickup, inter_deposit, 1,1);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);
        NotifyTSPProgressToGui notifyTSPProgressToGui = new NotifyTSPProgressMock();

        TourCalculatorService tourCalculator = new TourCalculatorService(notifyTSPProgressToGui);
        tourCalculator.provideCityMap(cityMap);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(10.f, tourSolution.totalLength());
    }

    /**
     * Stress test on a 40x40 grid for one delivery; validates expected tour length.
     */
    @Test
    public void test_tsp_for_one_delivery_4() {
        /* Ville utilisée
        1Wa ... 40P1
         :       :
       1561 ... 1600D1
        */
        CityMap cityMap = getManhattanMap(40,40);
        Intersection inter_warehouse = makeIntersectionFromId(1);
        Delivery delivery = makeDelivery(40,1600);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);
        NotifyTSPProgressToGui notifyTSPProgressToGui = new NotifyTSPProgressMock();

        TourCalculatorService tourCalculator = new TourCalculatorService(notifyTSPProgressToGui);
        tourCalculator.provideCityMap(cityMap);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(156.f, tourSolution.totalLength());
    }

    /**
     * Verifies optimal tour for two deliveries on a 4x4 grid without road removals.
     */
    @Test
    public void test_tsp_for_two_deliveries_1() {
        /* Ville utilisée
        1Wa ══  2  ══  3  ══ 4P2
         ║      ║      ║      ║
         5  ══  6  ══ 7P1 ══  8
         ║      ║      ║      ║
         9  ══ 10  ══ 11  ══ 12
         ║      ║      ║      ║
        13  ══14D1 ══ 15  ══16D2
        */
        CityMap cityMap = getManhattanMap(4,4);
        Intersection inter_warehouse = makeIntersectionFromId(1);
        Delivery delivery1 = makeDelivery(7,14);
        Delivery delivery2 = makeDelivery(4,16);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery1,delivery2));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);
        NotifyTSPProgressToGui notifyTSPProgressToGui = new NotifyTSPProgressMock();

        TourCalculatorService tourCalculator = new TourCalculatorService(notifyTSPProgressToGui);
        tourCalculator.provideCityMap(cityMap);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(14.f, tourSolution.totalLength());
    }

    /**
     * Verifies impact of removing several bottom-row connections on the optimal tour.
     */
    @Test
    public void test_tsp_for_two_deliveries_2() {
        /* Ville utilisée
        1Wa ══  2  ══  3  ══ 4P2
         ║      ║      ║      ║
         5  ══  6  ══ 7P1 ══  8
         ║      ║      ║      ║
         9  ══ 10  ══ 11  ══ 12
         ║
        13  ══14D1 ══ 15  ══ 16D2
        */
        CityMap cityMap = getManhattanMap(4,4);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 12, 16);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 11, 15);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 10, 14);
        Intersection inter_warehouse = makeIntersectionFromId(1);
        Delivery delivery1 = makeDelivery(7,14);
        Delivery delivery2 = makeDelivery(4,16);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery1,delivery2));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);
        NotifyTSPProgressToGui notifyTSPProgressToGui = new NotifyTSPProgressMock();

        TourCalculatorService tourCalculator = new TourCalculatorService(notifyTSPProgressToGui);
        tourCalculator.provideCityMap(cityMap);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(18.f, tourSolution.totalLength());
    }

    /**
     * Verifies optimal tour with disconnected vertical connections and two deliveries.
     */
    @Test
    public void test_tsp_for_two_deliveries_3() {
        /* Ville utilisée
        1Wa ══ 2D2 ══  3  ══ 4P2
         ║
         5  ══  6  ══ 7P1 ══  8
         ║      ║      ║      ║
         9  ══ 10  ══ 11  ══ 12
         ║
        13  ══14D1 ══ 15  ══ 16P1
        */
        CityMap cityMap = getManhattanMap(4,4);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 10, 14);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 11, 15);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 12, 16);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 2, 6);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 3, 7);
        cityMap = removeBidirectionalRoadFromCityMap(cityMap, 4, 8);
        Intersection inter_warehouse = makeIntersectionFromId(1);
        Delivery delivery1 = makeDelivery(4,2);
        Delivery delivery2 = makeDelivery(16,14);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery1,delivery2));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);
        NotifyTSPProgressToGui notifyTSPProgressToGui = new NotifyTSPProgressMock();

        TourCalculatorService tourCalculator = new TourCalculatorService(notifyTSPProgressToGui);
        tourCalculator.provideCityMap(cityMap);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(18.f, tourSolution.totalLength());
    }
}
