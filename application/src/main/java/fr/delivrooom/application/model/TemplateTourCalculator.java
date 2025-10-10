package fr.delivrooom.application.model;

import java.util.*;

public class TemplateTourCalculator implements TourCalculator{

    protected Graphe graph;
    protected CityMap cityMap;

    public TemplateTourCalculator(Graphe g) {
        // create a Tour calculator with a graph
        this.graph = g;
    }

    @Override
    public void findOptimalTour(DeliveriesDemand demand) {
        // first : find all shortest paths between any pair of nodes in the graph
    }

    @Override
    public TourSolution getOptimalTour() {
        return null;
    }

    @Override
    public float getTourLength() {
        return 0;
    }

    protected HashMap<Long, Path> findShortestPaths(long startIntersectionId , HashSet<Long> targets) {

        // as of now, only one target is acceptable
        if (targets.size() > 1) {
            throw new RuntimeException("Only single-target search is currently implemented");
        }
        return TargetedDijkstraSearch(startIntersectionId, targets);
    }

    protected HashMap<Long, Path> TargetedDijkstraSearch(long startIntersectionId, HashSet<Long> targets) throws RuntimeException {
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

        // add a function to build Path objects to each target from predecessors
        return null;
    }


}