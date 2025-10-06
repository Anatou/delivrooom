package fr.delivrooom.application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class TemplateTourCalculator implements TourCalculator{

    protected Graphe g;

    public TemplateTourCalculator(CityGraph g) {
        // create a Tour calculator with a graph
        this.g = g;
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

    protected Path findShortestPath(int i, int j) {
        return djikstraSearch(i, j);
    }

    protected Path djikstraSearch(int i, int j) {
        int n = g.getNbSommets();
        HashMap<Integer, Float> distances = new HashMap<Integer, Float>();
        HashMap<Integer, Integer> predecessors = new HashMap<Integer, Integer>();
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>();

        // init
        distances.put(i, 0.f);
        predecessors.put(i, null);
        queue.add(i);

        int reachedIntersection = i;
        while (!queue.isEmpty() && reachedIntersection != j) {
            reachedIntersection = queue.poll();
            for (Integer newIntersectionId : g.arcs(reachedIntersection)) {
                //if (distances.get()
            }
        }


        return null;
    }

}
