import fr.delivrooom.application.model.CityGraph;
import fr.delivrooom.application.model.Path;
import fr.delivrooom.application.model.TemplateTourCalculator;
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

    @Test
    public void simple_test() {
        ManualGraph graph = ManualGraph.getUnitSquare();
        CityGraph cityGraph = new CityGraph(graph.costs, graph.adjacencyList, graph.intersectionsCount);
        HashSet<Long> targets = new HashSet<>(Set.of(2L));

        TemplateTourCalculator templateTourCalculator = new TemplateTourCalculator(cityGraph);
        System.out.println(templateTourCalculator.getGraph().getNbSommets());
        HashMap<Long, Path> out;
        out = templateTourCalculator.targetedDijkstraSearchTest(1, targets);
        System.out.println("AAAAAAA");
        System.out.println(out);
        System.out.println(out.get(2L).getTotalLength());
        Assertions.assertEquals(1.f, out.get(2L).getTotalLength());
    }
}
