import fr.delivrooom.application.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TemplateTourCalculatorTests {

    private record ManualGraph(
            HashMap<Long, HashMap<Long, Float>> costs,
            HashMap<Long, List<Long>> adjacencyList,
            int intersectionsCount
    ) {
        static ManualGraph getUnitSquare() {
            return new ManualGraph(
                new HashMap<>(Map.of(
                    1L, new HashMap<>(Map.of(2L, 1.f, 4L,1.f)),
                    2L, new HashMap<>(Map.of(3L, 1.f, 1L, 1.f)),
                    3L, new HashMap<>(Map.of(4L, 1.f, 2L,1.f)),
                    4L, new HashMap<>(Map.of(1L, 1.f, 3L, 1.f))
                )),
                new HashMap<>(Map.of(
                        1L, List.of(2L,4L),
                        2L, List.of(1L,3L),
                        3L, List.of(2L,4L),
                        4L, List.of(1L,3L)
                )),
            4
            );
        }
    }

    private record ManualCityMapFactory(
            HashMap<Long, Intersection> intersections,
            HashMap<Long, HashMap<Long, Road>> roads
    ) {
        static CityMap UnitSquare() {
            Intersection inter1 = new Intersection(1, 0.0, 0.0);
            Intersection inter2 = new Intersection(2, 1.0, 0.0);
            Intersection inter3 = new Intersection(3, 1.0, 1.0);
            Intersection inter4 = new Intersection(4, 0.0, 1.0);

            Road road1to2 = new Road(inter1, inter2, 1.f, "road1to2");
            Road road1to4 = new Road(inter1, inter4, 1.f, "road1to4");
            Road road2to3 = new Road(inter2, inter3, 1.f, "road2to3");
            Road road3to4 = new Road(inter3, inter4, 1.f, "road3to4");
            Road road4to1 = new Road(inter4, inter1, 1.f, "road4to1");

            HashMap<Long, Road> roads_from_1 = new HashMap<>();
            roads_from_1.put(2L, road1to2);
            roads_from_1.put(4L, road1to4);
            HashMap<Long, Road> roads_from_2 = new HashMap<>();
            roads_from_2.put(3L, road2to3);
            HashMap<Long, Road> roads_from_3 = new HashMap<>();
            roads_from_3.put(4L, road3to4);
            HashMap<Long, Road> roads_from_4 = new HashMap<>();
            roads_from_4.put(1L, road4to1);

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
    }

    @Test
    public void simple_test() {
        CityMap cityMap = ManualCityMapFactory.UnitSquare();
        CityGraph cityGraph = new CityGraph(cityMap);
        HashSet<Long> targets = new HashSet<>(Set.of(2L));

        TemplateTourCalculator templateTourCalculator = new TemplateTourCalculator(cityGraph);
        System.out.println(templateTourCalculator.getGraph().getNbSommets());
        HashMap<Long, Path> out;
        out = templateTourCalculator.targetedDijkstraSearchTest(1, targets);
        System.out.println("AAAAAAA");
        System.out.println(out);
        System.out.println(out.get(2L).getTotalLength());
        Assertions.assertEquals(1.0, out.get(2L).getTotalLength());
    }
}
