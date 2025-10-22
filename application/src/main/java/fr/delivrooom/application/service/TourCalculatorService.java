package fr.delivrooom.application.service;

import fr.delivrooom.application.model.*;
import fr.delivrooom.application.model.tsp.DynamicProgrammingTSP;
import fr.delivrooom.application.port.in.CalculateTourUseCase;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;

import java.util.*;

public class TourCalculatorService implements CalculateTourUseCase {

    protected boolean hasMapChangedSinceLastCompute;

    protected CityGraph graph;
    protected TourSolution tourSolution;
    protected DeliveriesDemand calculatedDemand;

    protected final NotifyTSPProgressToGui notifyTSPProgressToGui;

    public TourCalculatorService(NotifyTSPProgressToGui notifyTSPProgressToGui) {
        // create a Tour calculator with a graph
        this.notifyTSPProgressToGui = notifyTSPProgressToGui;
        this.tourSolution = null;
        this.calculatedDemand = null;
        this.graph = null;
        this.hasMapChangedSinceLastCompute = false;
    }

    @Override
    public void provideCityMap(CityMap m) {
        this.graph = new CityGraph(m);
        hasMapChangedSinceLastCompute = true;

    }

    @Override
    public boolean doesCalculatedTourNeedsToBeChanged(DeliveriesDemand demand) {
        // checks if the demand is different from the last calculated one or if no tour has been calculated yet
        if (calculatedDemand == null) {
            return demand != null && !demand.deliveries().isEmpty();
        }
        else {
            return hasMapChangedSinceLastCompute || !calculatedDemand.equals(demand);
        }
    }

    @Override
    public TourSolution getOptimalTour() {
        return tourSolution;
    }

    @Override
    public void findOptimalTour(DeliveriesDemand demand, boolean useTimeAsCost) throws RuntimeException {
        // first : find all shortest paths between any pair of nodes in the graph
        hasMapChangedSinceLastCompute = false;

        calculatedDemand = new DeliveriesDemand(new ArrayList<>(demand.deliveries()), demand.store());

        if (graph == null) {
            throw new RuntimeException("CityGraph must be in TourCalculator before attempting to find optimal tour");
        }

        // Create a complete graph of g by running dijkstra from each node to get the shortest path to every other node
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
            HashMap<Long, Path> pathsFromIntersection;
            if (useTimeAsCost) {
                pathsFromIntersection = targetedDijkstraSearchTime(intersectionId, targetWithoutIntersectionId);

            }
            else {
                pathsFromIntersection = findShortestPaths(intersectionId, targetWithoutIntersectionId, useTimeAsCost);

            }

            shortestPathsMatrix.put(intersectionId, pathsFromIntersection);
        }
        System.out.println("Shortest paths matrix calculated");

        shortestPathsGraph = new ShortestPathsGraph(shortestPathsMatrix);

        long tpsDebut = System.currentTimeMillis();
        // Create a TSP solver and run it on the complete graph
        DynamicProgrammingTSP tspSolver = new DynamicProgrammingTSP();
        int timeLimitMs = 10000; // 10 seconds time limit for TSP solving
        tspSolver.searchSolution(timeLimitMs, shortestPathsGraph, demand, this.notifyTSPProgressToGui);
        Long[] tspSolution = tspSolver.getSolution();
        float tspSolutionCost = tspSolver.getBestCost();

        // Convert the TSP solution to a TourSolution by replacing each edge with the corresponding path in the original graph
        List<Path> tourPaths = new ArrayList<>();
        //System.out.println("Solution intersections order : ");
        List<Long> solutionList = Arrays.asList(tspSolution);
        // TODO : replace bestTime with actual time calculation (has to be done in TSP solver)
        float bestTime = 0.f;
        for (int i = 0; i < tspSolution.length; i++) {
            long fromId = tspSolution[i];
            long toId = tspSolution[(i + 1) % tspSolution.length]; // wrap around to form a cycle
            System.out.println(fromId + " -> " + toId);
            Path path = shortestPathsMatrix.get(fromId).get(toId);
            bestTime += path.totalTime();
            tourPaths.add(path);
            if (useTimeAsCost){
                //System.out.println(fromId + " -> " + toId + " (time cost: " + path.totalTime() + " seconds) |  length : " + path.totalLength() + " meters");
            }else {

                //System.out.println(fromId + " -> " + toId + " (path length: " + path.totalLength() + ")" + " | " + path.totalTime() + " seconds");
            }
        }
//        System.out.println("tour paths constructed :" + tourPaths.size() + " paths");
//        System.out.println("Total tour cost (distance): " + tspSolutionCost);
//        System.out.println("Total tour cost (time): " + bestTime + " seconds | distance "+ tspSolutionCost/4.17 + " + waiting " + (bestTime - tspSolutionCost/4.17) + " seconds");
        long temps = System.currentTimeMillis() - tpsDebut;
        System.out.println("Time used to calculate TSP (ms): " + temps);

//
        tourSolution = new TourSolution(tourPaths, tspSolutionCost, solutionList);
    }

    protected HashMap<Long, Path> findShortestPaths(long startIntersectionId , HashSet<Long> targets, boolean useTimeAsCost) throws RuntimeException {
        /* find the shortest paths from startIntersectionId to all targets
         * using Dijkstra's algorithm (for now)
         */

        // as of now, only one target is acceptable
//        if (targets.size() > 1) {
//            throw new RuntimeException("Only single-target search is currently implemented");
//        }
        return targetedDijkstraSearch(startIntersectionId, targets, useTimeAsCost);
    }

    protected HashMap<Long, Path> targetedDijkstraSearch(long startIntersectionId, HashSet<Long> targets, boolean useTime) throws RuntimeException {
        int n = graph.getNbSommets();
        HashMap<Long, Float> distances = new HashMap<>();
        HashMap<Long, Float> time = new HashMap<>();

        HashMap<Long, Long> predecessors = new HashMap<>();
        // comparator: utilise distances ou time selon useTime et traite les clés absentes comme distance/time infinie
        PriorityQueue<Long> queue = new PriorityQueue<>(Comparator.comparingDouble(k ->
                useTime ? time.getOrDefault(k, Float.POSITIVE_INFINITY) : distances.getOrDefault(k, Float.POSITIVE_INFINITY)
        ));
        HashSet<Long> settled = new HashSet<>();
        int targetsRemaining = targets.size();

        // init
        time.put(startIntersectionId, 0f);
        distances.put(startIntersectionId, 0f);
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
                        float currentDistance = distances.getOrDefault(selectedIntersectionId, Float.POSITIVE_INFINITY);
                        float currentTime = time.getOrDefault(selectedIntersectionId, Float.POSITIVE_INFINITY);

                        float newDistance = currentDistance + graph.getCout(selectedIntersectionId, newIntersectionId);
                        float newTime = currentTime + graph.getCout(selectedIntersectionId, newIntersectionId) / 4.17F;

                        for (Delivery d : calculatedDemand.deliveries()) {
                            if (d.takeoutIntersection().getId() == newIntersectionId) {
                                newTime += d.takeoutDuration();
                            }
                            if (d.deliveryIntersection().getId() == newIntersectionId) {
                                newTime += d.deliveryDuration();
                            }
                        }

                        if (!useTime) {
                            if (!distances.containsKey(newIntersectionId) || newDistance < distances.get(newIntersectionId)) {
                                predecessors.put(newIntersectionId, selectedIntersectionId);
                                distances.put(newIntersectionId, newDistance);
                                time.put(newIntersectionId, newTime);
                                queue.add(newIntersectionId);
                            }
                        } else {
                            if (!time.containsKey(newIntersectionId) || newTime < time.get(newIntersectionId)) {
                                predecessors.put(newIntersectionId, selectedIntersectionId);
                                time.put(newIntersectionId, newTime);
                                distances.put(newIntersectionId, newDistance);
                                queue.add(newIntersectionId);
                            }
                        }
                    }
                }
            }
        }

        if (targetsRemaining > 0) {
            throw new RuntimeException("Input graph is not connex, no path could be found for at least a target");
        }

        // build Path objects to each target from predecessors
        HashMap<Long, Path> pathToTarget = new HashMap<>();

        for (long targetId : targets) {
            List<Road> roads = new ArrayList<>();
            long nodeId = targetId;
            float pathLentgh = 0f;
            float totalTime = 0f;
            while (nodeId != startIntersectionId) {
                Long parentIdObj = predecessors.get(nodeId);
                if (parentIdObj == null) {
                    throw new RuntimeException("No predecessor found for node " + nodeId + " (graph not connected?)");
                }
                long parentId = parentIdObj;
                Road road = graph.getCityMap().getRoad(parentId, nodeId);
                roads.add(road);
                pathLentgh += road.getLength();

                for (Delivery d : calculatedDemand.deliveries()) {
                    if (d.takeoutIntersection().getId() == nodeId) {
                        totalTime += d.takeoutDuration();
                    }
                    if (d.deliveryIntersection().getId() == nodeId) {
                        totalTime += d.deliveryDuration();
                    }
                }
                totalTime += road.getLength() / 4.17F;

                nodeId = parentId;
            }
            pathToTarget.put(targetId, new Path(roads, pathLentgh, totalTime));
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

