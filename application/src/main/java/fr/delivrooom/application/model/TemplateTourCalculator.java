package fr.delivrooom.application.model;

import java.util.*;

public class TemplateTourCalculator implements TourCalculator{

    protected CityGraph graph;
    protected TourSolution tourSolution;
    protected DeliveriesDemand calculatedDemand;

    public TemplateTourCalculator(CityGraph g) {
        // create a Tour calculator with a graph
        this.graph = g;
        this.tourSolution = null;
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

    @Override
    public void findOptimalTour(DeliveriesDemand demand) {
        // first : find all shortest paths between any pair of nodes in the graph
        if (tourSolution == null || !calculatedDemand.equals(demand)) {
            Delivery delivery = demand.getDeliveries().getFirst();
            Intersection warehouse = demand.getStore();
            Long firstPickupId = delivery.getTakeoutIntersection().getId();
            HashSet<Long> targets = new HashSet<>(Set.of(firstPickupId));
            HashMap<Long, Path> solution = findShortestPaths(warehouse.getId(), targets);
            Path pathToFirstPickup = solution.get(firstPickupId);
            tourSolution = new TourSolution(new ArrayList<>(List.of(pathToFirstPickup)), pathToFirstPickup.getTotalLength());
        }
        // todo: recalculate only the needed deliveries
    }

    @Override
    public TourSolution getOptimalTour() {
        return tourSolution;
    }

    @Override
    public float getTourLength() {
        if (tourSolution != null) {
            return tourSolution.getTotalLength();
        } else {
            return  -1f;
        }
    }

    protected HashMap<Long, Path> findShortestPaths(long startIntersectionId , HashSet<Long> targets) {
        /* find the shortest paths from startIntersectionId to all targets
         * using Dijkstra's algorithm (for now)
         */

        // as of now, only one target is acceptable
        if (targets.size() > 1) {
            throw new RuntimeException("Only single-target search is currently implemented");
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
        HashMap<Long, Long> predecessors = new HashMap<Long, Long>();
        PriorityQueue<Long> queue = new PriorityQueue<Long>(Comparator.comparing(distances::get)); // might be unsafe, TODO: test
        HashSet<Long> settled = new HashSet<Long>();
        int targetsRemaining = targets.size();

        // init
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
                        float newDistance = distances.get(selectedIntersectionId) + graph.getCout(selectedIntersectionId, newIntersectionId);
                        if (!distances.containsKey(newIntersectionId) || newDistance < distances.get(newIntersectionId)) {
                            predecessors.put(newIntersectionId, selectedIntersectionId);
                            distances.put(newIntersectionId, newDistance);
                            queue.add(newIntersectionId);
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
            float pathLentgh = 0.f;
            while (nodeId != startIntersectionId) {
                long parentId = predecessors.get(nodeId);
                Road road = graph.getCityMap().getRoad(parentId, nodeId);
                roads.add(road);
                pathLentgh += road.getLength();

                nodeId = parentId;
            }
            pathToTarget.put(targetId, new Path(roads, pathLentgh));
        }

        return pathToTarget;
    }
}