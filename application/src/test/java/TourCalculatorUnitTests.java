import fr.delivrooom.application.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TourCalculatorUnitTests {

    static CityMap GetCompleteSquareMap() {
        Intersection inter1 = new Intersection(1, 0.0, 0.0);
        Intersection inter2 = new Intersection(2, 1.0, 0.0);
        Intersection inter3 = new Intersection(3, 1.0, 1.0);
        Intersection inter4 = new Intersection(4, 0.0, 1.0);

        Road road1to2 = new Road(inter1, inter2, 1.f, "road1to2");
        Road road1to4 = new Road(inter1, inter4, 1.f, "road1to4");
        Road road2to1 = new Road(inter2, inter1, 1.f, "road2to1");
        Road road2to3 = new Road(inter2, inter3, 1.f, "road2to3");
        Road road3to2 = new Road(inter3, inter2, 1.f, "road3to2");
        Road road3to4 = new Road(inter3, inter4, 1.f, "road3to4");
        Road road4to1 = new Road(inter4, inter1, 1.f, "road4to1");
        Road road4to3 = new Road(inter4, inter3, 1.f, "road4to3");

        HashMap<Long, Road> roads_from_1 = new HashMap<>();
        roads_from_1.put(2L, road1to2);
        roads_from_1.put(4L, road1to4);
        HashMap<Long, Road> roads_from_2 = new HashMap<>();
        roads_from_2.put(1L, road2to1);
        roads_from_2.put(3L, road2to3);
        HashMap<Long, Road> roads_from_3 = new HashMap<>();
        roads_from_3.put(2L, road3to2);
        roads_from_3.put(4L, road3to4);
        HashMap<Long, Road> roads_from_4 = new HashMap<>();
        roads_from_4.put(1L, road4to1);
        roads_from_4.put(3L, road4to3);

        return new CityMap(
                new HashMap<>(Map.of(
                        1L, inter1,
                        2L, inter2,
                        3L, inter3,
                        4L, inter4
                )),
                new HashMap<>(Map.of(
                        1L, roads_from_1,
                        2L, roads_from_2,
                        3L, roads_from_3,
                        4L, roads_from_4
                ))
        );
    }
    static CityMap GetBrokenSquareMap() {
            Intersection inter1 = new Intersection(1, 0.0, 0.0);
            Intersection inter2 = new Intersection(2, 1.0, 0.0);
            Intersection inter3 = new Intersection(3, 1.0, 1.0);
            Intersection inter4 = new Intersection(4, 0.0, 1.0);

            Road road1to2 = new Road(inter1, inter2, 1.f, "road1to2");
            Road road1to4 = new Road(inter1, inter4, 1.f, "road1to4");
            Road road2to1 = new Road(inter2, inter1, 1.f, "road2to1");
            Road road2to3 = new Road(inter2, inter3, 1.f, "road2to3");
            Road road3to2 = new Road(inter3, inter2, 1.f, "road3to2");
            Road road3to4 = new Road(inter3, inter4, 1.f, "road3to4");
            Road road4to1 = new Road(inter4, inter1, 1.f, "road4to1");
            Road road4to3 = new Road(inter4, inter3, 1.f, "road4to3");

            HashMap<Long, Road> roads_from_1 = new HashMap<>();
            //roads_from_1.put(2L, road1to2);
            roads_from_1.put(4L, road1to4);
            HashMap<Long, Road> roads_from_2 = new HashMap<>();
            //roads_from_2.put(1L, road2to1);
            roads_from_2.put(3L, road2to3);
            HashMap<Long, Road> roads_from_3 = new HashMap<>();
            roads_from_3.put(2L, road3to2);
            roads_from_3.put(4L, road3to4);
            HashMap<Long, Road> roads_from_4 = new HashMap<>();
            roads_from_4.put(1L, road4to1);
            roads_from_4.put(3L, road4to3);

            return new CityMap(
                    new HashMap<>(Map.of(
                            1L, inter1,
                            2L, inter2,
                            3L, inter3,
                            4L, inter4
                    )),
                    new HashMap<>(Map.of(
                            1L, roads_from_1,
                            2L, roads_from_2,
                            3L, roads_from_3,
                            4L, roads_from_4
                    ))
            );
        }

    @Test
    public void test_tsp_for_one_delivery_1() {
        CityMap cityMap = TourCalculatorUnitTests.GetCompleteSquareMap();
        CityGraph cityGraph = new CityGraph(cityMap);
        Intersection inter_pickup = new Intersection(2, 1.0, 0.0);
        Intersection inter_deposit = new Intersection(3, 1.0, 1.0);
        Intersection inter_warehouse = new Intersection(1, 1.0, 1.0);
        Delivery delivery = new Delivery(inter_pickup, inter_deposit, 1,1);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);

        TourCalculator tourCalculator = new TourCalculator(cityGraph);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(4.f, tourSolution.totalLength());
    }

    @Test
    public void test_tsp_for_one_delivery_2() {
        CityMap cityMap = TourCalculatorUnitTests.GetBrokenSquareMap();
        CityGraph cityGraph = new CityGraph(cityMap);
        Intersection inter_pickup = new Intersection(2, 1.0, 0.0);
        Intersection inter_deposit = new Intersection(3, 1.0, 1.0);
        Intersection inter_warehouse = new Intersection(1, 1.0, 1.0);
        Delivery delivery = new Delivery(inter_pickup, inter_deposit, 1,1);
        List<Delivery> deliveries = new ArrayList<>(List.of(delivery));
        DeliveriesDemand deliveriesDemand = new DeliveriesDemand(deliveries, inter_warehouse);

        TourCalculator tourCalculator = new TourCalculator(cityGraph);
        tourCalculator.findOptimalTour(deliveriesDemand, false);
        TourSolution tourSolution = tourCalculator.getOptimalTour();
        Assertions.assertEquals(6.f, tourSolution.totalLength());
    }
}
