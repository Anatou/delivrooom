package fr.delivrooom.application.model;

import fr.delivrooom.application.model.tsp.TSP1;
import fr.delivrooom.application.model.tsp.TemplateTSP;

import java.util.*;

public class TourCalculator {

    protected CityGraph graph;
    protected TourSolution tourSolution;
    protected DeliveriesDemand calculatedDemand;

    public TourCalculator(CityGraph g) {
        // create a Tour calculator with a graph
        this.graph = g;
        this.tourSolution = null;
        this.calculatedDemand = null;
    }

    public Graphe getGraph() { return graph; }

    public boolean doesCalculatedTourNeedsToBeChanged(DeliveriesDemand demand) {
        // checks if the demand is different from the last calculated one or if no tour has been calculated yet
        if (calculatedDemand == null){
            if (demand == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return !calculatedDemand.equals(demand);
        }
    }

    public void findOptimalTour(DeliveriesDemand demand, boolean useTimeAsCost) throws RuntimeException {
        // first : find all shortest paths between any pair of nodes in the graph

        calculatedDemand = demand;

        // Create a complete graph of g by running djikstra from each node to get the shortest path to every other node
        ShortestPathsGraph shortestPathsGraph;
        HashMap<Long, HashMap<Long, Path>> shortestPathsMatrix = new HashMap<>();

        // initialize targets;
        HashSet<Long> targets = new HashSet<>();
        for (Delivery d : demand.deliveries()) {
            targets.add(d.takeoutIntersection().getId());
            targets.add(d.deliveryIntersection().getId());
        }
        targets.add(demand.store().getId());

        // for each intersection in the graph, run dijkstra to find the shortest path to every other intersection

        for (Long intersectionId : targets) {
            // remove self from targets
            HashSet<Long> targetWithoutIntersectionId = new HashSet<>(targets);
            targetWithoutIntersectionId.remove(intersectionId);

            System.out.println("Calculating shortest paths from intersection " + intersectionId);
            System.out.println("Targets: " + targetWithoutIntersectionId);
            HashMap<Long, Path> pathsFromIntersection = findShortestPaths(intersectionId, targetWithoutIntersectionId, useTimeAsCost);
            shortestPathsMatrix.put(intersectionId, pathsFromIntersection);
            System.out.println("Shortest paths from intersection " + intersectionId + " calculated");
        }
        System.out.println("Shortest paths matrix calculated");

        shortestPathsGraph = new ShortestPathsGraph(shortestPathsMatrix);

        // Create a TSP solver and run it on the complete graph
        TemplateTSP tspSolver = new TSP1();
        int timeLimitMs = 10000; // 10 seconds time limit for TSP solving
        tspSolver.chercheSolution(timeLimitMs, shortestPathsGraph, demand);
        Long[] tspSolution = tspSolver.getSolution();
        float tspSolutionCost = tspSolver.getCoutSolution();
        System.out.println("TSP solution calculated");

        // Convert the TSP solution to a TourSolution by replacing each edge with the corresponding path in the original graph
        List<Path> tourPaths = new ArrayList<>();
        System.out.println("Solution intersections order : ");
        List<Long> solutionList = Arrays.asList(tspSolution);
        // TODO : replace bestTime with actual time calculation (has to be done in TSP solver)
        float bestTime = 0.f;
        for (int i = 0; i < tspSolution.length; i++) {
            long fromId = tspSolution[i];
            long toId = tspSolution[(i + 1) % tspSolution.length]; // wrap around to form a cycle
            Path path = shortestPathsMatrix.get(fromId).get(toId);
            bestTime += path.getTotalTime();
            tourPaths.add(path);
            if (useTimeAsCost){
                System.out.print(fromId + " -> " + toId + " (time cost: " + path.getTotalTime() + " seconds) |  length : " + path.getTotalLength() + " meters\n");
            }else {

                System.out.println(fromId + " -> " + toId + " (path length: " + path.getTotalLength() + ")" + " | " + path.getTotalTime() + " seconds\n");
            }
        }
        System.out.println("tour paths constructed :" + tourPaths.size() + " paths");
        System.out.println("Total tour cost (distance): " + tspSolutionCost);
        System.out.println("Total tour cost (time): " + bestTime + " seconds | distance "+ tspSolutionCost/4.17 + " + waiting " + (bestTime - tspSolutionCost/4.17) + " seconds");

        tourSolution = new TourSolution(tourPaths, tspSolutionCost, solutionList);





    }

    public TourSolution getOptimalTour() {
        return tourSolution;
    }

    public float getTourLength() {
        if (tourSolution != null) {
            return tourSolution.getTotalLength();
        } else {
            return  -1f;
        }
    }

    protected HashMap<Long, Path> findShortestPaths(long startIntersectionId , HashSet<Long> targets, boolean useTimeAsCost) throws RuntimeException {
        /* find the shortest paths from startIntersectionId to all targets
         * using Dijkstra's algorithm (for now)
         */

        // as of now, only one target is acceptable
//        if (targets.size() > 1) {
//            throw new RuntimeException("Only single-target search is currently implemented");
//        }
        if (useTimeAsCost) {
            return targetedDijkstraSearchTime(startIntersectionId, targets);
        }
        return targetedDijkstraSearch(startIntersectionId, targets);
    }

    public HashMap<Long, Path> targetedDijkstraSearchTest(long startIntersectionId, HashSet<Long> targets) throws RuntimeException {
        return this.targetedDijkstraSearch(startIntersectionId, targets);
    }


    protected HashMap<Long, Path> targetedDijkstraSearch(long startIntersectionId, HashSet<Long> targets) throws RuntimeException {
        //
        int n = graph.getNbSommets();
        HashMap<Long, Float> distances = new HashMap<Long, Float>();
        HashMap<Long, Float> time = new HashMap<Long, Float>();

        HashMap<Long, Long> predecessors = new HashMap<Long, Long>();
        PriorityQueue<Long> queue = new PriorityQueue<Long>(Comparator.comparing(distances::get)); // might be unsafe, TODO: test
        HashSet<Long> settled = new HashSet<Long>();
        int targetsRemaining = targets.size();

        // init
        time.put(startIntersectionId, 0.f);
        distances.put(startIntersectionId, 0.f);
        predecessors.put(startIntersectionId, null);
        queue.add(startIntersectionId);

        long selectedIntersectionId = startIntersectionId;
        while (!queue.isEmpty() && targetsRemaining > 0) {
            selectedIntersectionId = queue.poll();
            if (!settled.contains(selectedIntersectionId)) {
                settled.add(selectedIntersectionId);
                if (targets.contains(selectedIntersectionId)) {
                    --targetsRemaining;
                }
                for (Long newIntersectionId : graph.arcs(selectedIntersectionId)) {
                    if (!settled.contains(newIntersectionId)) {
                        // if this new intersection is a pickup point -> retrieve its takeoutDuration
                        float newDistance = distances.get(selectedIntersectionId) + graph.getCout(selectedIntersectionId, newIntersectionId);
                        float newTime = time.get(selectedIntersectionId) + graph.getCout(selectedIntersectionId, newIntersectionId)/4.17F; // assuming average speed of 15 km/h = 4.17 m/s

                        for(Delivery d : calculatedDemand.deliveries()) {
                            if (d.takeoutIntersection().getId() == newIntersectionId) {
                                float takeoutDuration = d.takeoutDuration();
                                newTime += takeoutDuration;
                            }
                            // if this new intersection is a delivery point -> retrieve its deliveryDuration
                            if (d.deliveryIntersection().getId() == newIntersectionId) {
                                float deliveryDuration = d.deliveryDuration();
                                newTime += deliveryDuration;
                            }
                        }
                        if (!distances.containsKey(newIntersectionId) || newDistance < distances.get(newIntersectionId)) {
                            predecessors.put(newIntersectionId, selectedIntersectionId);
                            distances.put(newIntersectionId, newDistance);
                            queue.add(newIntersectionId);
                            time.put(newIntersectionId, newTime);

                        }
                    }
                }
            }
        }

        if (targetsRemaining > 0) {
            throw new RuntimeException("Input graph is not connex, no path could be found for at least a target");
        }

        // TODO : since the predecessors are stored in a hashmap, they are not ordered which then leads to paths being constructed in wrong order
        // build Path objects to each target from predecessors
        HashMap<Long, Path> pathToTarget = new HashMap<>();

        for (long targetId : targets) {
            List<Road> roads = new ArrayList<>();
            long nodeId = targetId;
            float pathLentgh = 0.f;
            float totalTime = 0.f;
            while (nodeId != startIntersectionId) {
                long parentId = predecessors.get(nodeId);
                Road road = graph.getCityMap().getRoad(parentId, nodeId);
                roads.add(road);
                pathLentgh += road.getLength();


                for(Delivery d : calculatedDemand.deliveries()) {
                    if (d.takeoutIntersection().getId() == nodeId) {
                        float takeoutDuration = d.takeoutDuration();
                        totalTime += takeoutDuration;
                    }
                    // if this new intersection is a delivery point -> retrieve its deliveryDuration
                    if (d.deliveryIntersection().getId() == nodeId) {
                        float deliveryDuration = d.deliveryDuration();
                        totalTime += deliveryDuration;
                    }
                }
                totalTime += road.getLength()/4.17F ;

                nodeId = parentId;
            }
            pathToTarget.put(targetId, new Path(roads, pathLentgh, totalTime)); // assuming average speed of 15 km/h = 4.17 m/s
        }

        return pathToTarget;
    }

    protected HashMap<Long, Path> targetedDijkstraSearchTime(long startIntersectionId, HashSet<Long> targets) throws RuntimeException {
        //
        int n = graph.getNbSommets();
        HashMap<Long, Float> distances = new HashMap<Long, Float>();
        HashMap<Long, Float> time = new HashMap<Long, Float>();

        HashMap<Long, Long> predecessors = new HashMap<Long, Long>();
        PriorityQueue<Long> queue = new PriorityQueue<Long>(Comparator.comparing(distances::get)); // might be unsafe, TODO: test
        HashSet<Long> settled = new HashSet<Long>();
        int targetsRemaining = targets.size();

        // init
        time.put(startIntersectionId, 0.f);
        distances.put(startIntersectionId, 0.f);
        predecessors.put(startIntersectionId, null);
        queue.add(startIntersectionId);

        long selectedIntersectionId = startIntersectionId;
        while (!queue.isEmpty() && targetsRemaining > 0) {
            selectedIntersectionId = queue.poll();
            if (!settled.contains(selectedIntersectionId)) {
                settled.add(selectedIntersectionId);
                if (targets.contains(selectedIntersectionId)) {
                    --targetsRemaining;
                }
                for (Long newIntersectionId : graph.arcs(selectedIntersectionId)) {
                    if (!settled.contains(newIntersectionId)) {
                        // if this new intersection is a pickup point -> retrieve its takeoutDuration
                        float newDistance = distances.get(selectedIntersectionId) + graph.getCout(selectedIntersectionId, newIntersectionId);
                        float newTime = time.get(selectedIntersectionId) + graph.getCout(selectedIntersectionId, newIntersectionId)/4.17F; // assuming average speed of 15 km/h = 4.17 m/s

                        for(Delivery d : calculatedDemand.deliveries()) {
                            if (d.takeoutIntersection().getId() == newIntersectionId) {
                                float takeoutDuration = d.takeoutDuration();
                                newTime += takeoutDuration;
                            }
                            // if this new intersection is a delivery point -> retrieve its deliveryDuration
                            if (d.deliveryIntersection().getId() == newIntersectionId) {
                                float deliveryDuration = d.deliveryDuration();
                                newTime += deliveryDuration;
                            }
                        }
                        if (!time.containsKey(newIntersectionId) || newTime < time.get(newIntersectionId)) {
                            predecessors.put(newIntersectionId, selectedIntersectionId);
                            distances.put(newIntersectionId, newDistance);
                            queue.add(newIntersectionId);
                            time.put(newIntersectionId, newTime);

                        }
                    }
                }
            }
        }

        if (targetsRemaining > 0) {
            throw new RuntimeException("Input graph is not connex, no path could be found for at least a target");
        }

        // TODO : since the predecessors are stored in a hashmap, they are not ordered which then leads to paths being constructed in wrong order
        // build Path objects to each target from predecessors
        HashMap<Long, Path> pathToTarget = new HashMap<>();

        for (long targetId : targets) {
            List<Road> roads = new ArrayList<>();
            long nodeId = targetId;
            float pathLentgh = 0.f;
            float totalTime = 0.f;
            while (nodeId != startIntersectionId) {
                long parentId = predecessors.get(nodeId);
                Road road = graph.getCityMap().getRoad(parentId, nodeId);
                roads.add(road);
                pathLentgh += road.getLength();


                for(Delivery d : calculatedDemand.deliveries()) {
                    if (d.takeoutIntersection().getId() == nodeId) {
                        float takeoutDuration = d.takeoutDuration();
                        totalTime += takeoutDuration;
                    }
                    // if this new intersection is a delivery point -> retrieve its deliveryDuration
                    if (d.deliveryIntersection().getId() == nodeId) {
                        float deliveryDuration = d.deliveryDuration();
                        totalTime += deliveryDuration;
                    }
                }
                totalTime += road.getLength()/4.17F ;

                nodeId = parentId;
            }
            pathToTarget.put(targetId, new Path(roads, pathLentgh, totalTime)); // assuming average speed of 15 km/h = 4.17 m/s
        }

        return pathToTarget;
    }
}

