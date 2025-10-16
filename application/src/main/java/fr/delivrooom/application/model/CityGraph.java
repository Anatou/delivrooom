package fr.delivrooom.application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CityGraph implements Graphe {
    //cost[i][j] = cost of the road from intersection i to intersection j
    protected final HashMap<Long, HashMap<Long, Float>> cost;
    protected final HashMap<Long, List<Long>> adjacencyList;
    protected final int intersectionsCount;
    protected final CityMap cityMap;

    public CityGraph(CityMap cityMap) {
        this.cityMap = cityMap;
        this.intersectionsCount = cityMap.intersections().size();
        this.cost = new HashMap<>();
        this.adjacencyList = new HashMap<>();

        for (HashMap<Long, Road> subMap : cityMap.roads().values()) {
            for (Road road : subMap.values()) {
                long fromId = road.getOrigin().getId();
                long toId = road.getDestination().getId();
                // the cost of a road is its length
                float roadCost = road.getLength();

                // If the "from" or toId are not in the map, add them
                this.cost.putIfAbsent(fromId, new HashMap<>());
                // Add the cost of the road
                this.cost.get(fromId).put(toId, roadCost);

                // Ajouter l'arc dans la liste d'adjacence
                this.adjacencyList.putIfAbsent(fromId, new java.util.ArrayList<>());
                this.adjacencyList.get(fromId).add(toId);
            }
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
        return new ArrayList<>();
    }

    public CityMap getCityMap() {
        return cityMap;
    }
}
