package fr.delivrooom.application.model;

import java.util.HashMap;
import java.util.List;


public class CityGraph implements Graphe {
    //cost[i][j] = cost of the road from intersection i to intersection j
    protected HashMap<Long, HashMap<Long, Float>> cost;
    protected HashMap<Long, List<Long>> adjacencyList;
    protected int intersectionsCount;

    public CityGraph(HashMap<Long, HashMap<Long, Float>> cost, HashMap<Long, List<Long>> adjacencyList, int intersectionsCount) {
        this.cost = cost;
        this.adjacencyList = adjacencyList;
        this.intersectionsCount = intersectionsCount;
    }

    public CityGraph(CityMap cityMap) {
        this.intersectionsCount = cityMap.getIntersections().size();
        this.cost = new HashMap<>();
        this.adjacencyList = new HashMap<>();

        for (Road road : cityMap.getRoads()) {
            long fromId = road.getOrigin().getId();
            long toId = road.getDestination().getId();
            // the cost of a road is its length
            float roadCost = road.getLength();

            // If the fromd or toId are not in the map, add them
            this.cost.putIfAbsent(fromId, new HashMap<>());
            this.cost.putIfAbsent(toId, new HashMap<>());
            // Add the cost of the road
            this.cost.get(fromId).put(toId, roadCost);
            this.cost.get(toId).put(fromId, roadCost);

            // Ajouter l'arc dans la liste d'adjacence
            this.adjacencyList.putIfAbsent(fromId, new java.util.ArrayList<>());
            this.adjacencyList.get(fromId).add(toId);
            this.adjacencyList.putIfAbsent(toId, new java.util.ArrayList<>());
            this.adjacencyList.get(toId).add(fromId);
        }
    }

    @Override
    public int getNbSommets() {
        return intersectionsCount;
    }

    @Override
    public float getCout(long i, long j) {
        return this.cost.get(i).get(j);
    }

    @Override
    public boolean estArc(long i, long j) {
        return this.cost.containsKey(i) && this.cost.get(i).containsKey(j);
    }

    @Override
    public List<Long> arcs(long i) {
        if (this.adjacencyList.containsKey(i)) {
            return this.adjacencyList.get(i);
        }
        return null;
    }
}
