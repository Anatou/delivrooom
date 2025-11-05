package fr.delivrooom.application.service.tourcalculator;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.Road;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Represents the city map as a graph, where intersections are vertices and roads are edges.
 * This class implements the {@link Graphe} interface.
 */
public class CityGraph implements Graphe {
    //cost[i][j] = cost of the road from intersection i to intersection j
    protected final HashMap<Long, HashMap<Long, Float>> cost;
    protected final HashMap<Long, List<Long>> adjacencyList;
    protected final int intersectionsCount;
    protected final CityMap cityMap;

    /**
     * Constructs a CityGraph from a {@link CityMap} object.
     *
     * @param cityMap The city map to represent as a graph.
     */
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

    /**
     * Returns the number of intersections (vertices) in this graph.
     *
     * @return the number of intersections.
     */
    @Override
    public int getNbSommets() {
        return intersectionsCount;
    }

    /**
     * Returns the cost associated with the directed edge from intersection {@code i} to {@code j}.
     * <p>
     * Note: this method assumes an edge exists from {@code i} to {@code j}. If no such edge
     * exists, a {@link NullPointerException} may be thrown by the current implementation.
     *
     * @param i the id of the origin intersection.
     * @param j the id of the destination intersection.
     * @return the cost (weight) of the edge from {@code i} to {@code j}.
     */
    @Override
    public float getCout(long i, long j) {
        return this.cost.get(i).get(j);
    }

    /**
     * Determines whether there is a directed edge from intersection {@code i} to {@code j}.
     *
     * @param i the id of the origin intersection.
     * @param j the id of the destination intersection.
     * @return {@code true} if an edge from {@code i} to {@code j} exists; {@code false} otherwise.
     */
    @Override
    public boolean estArc(long i, long j) {
        return this.cost.containsKey(i) && this.cost.get(i).containsKey(j);
    }

    /**
     * Returns the list of destination intersection ids that are directly reachable from intersection {@code i}.
     *
     * If the intersection {@code i} has no outgoing arcs, an empty list is returned.
     *
     * @param i the id of the intersection whose outgoing arcs are requested.
     * @return a list of intersection ids reachable from {@code i}.
     */
    @Override
    public List<Long> arcs(long i) {
        if (this.adjacencyList.containsKey(i)) {
            return this.adjacencyList.get(i);
        }
        return new ArrayList<>();
    }

    /**
     * Returns the underlying {@link CityMap} used to build this graph.
     *
     * @return the city map.
     */
    public CityMap getCityMap() {
        return cityMap;
    }
}
