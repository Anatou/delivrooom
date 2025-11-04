package fr.delivrooom.application.service.tourcalculator;

import fr.delivrooom.application.model.Path;

import java.util.HashMap;
import java.util.List;

/**
 * A graph where edges represent the shortest paths between a subset of intersections
 * in a larger city map. This is typically used as the input for a TSP solver.
 */
public class ShortestPathsGraph implements Graphe {

    protected HashMap<Long, HashMap<Long, Path>> adjacencyMatrix;
    protected int intersectionsCount;

    /**
     * Constructs a ShortestPathsGraph from a pre-computed matrix of shortest paths.
     *
     * @param shortestPathsMatrix A nested map where the outer key is the origin, the inner key is the destination,
     *                            and the value is the {@link Path} object representing the shortest path.
     */
    public ShortestPathsGraph(HashMap<Long, HashMap<Long, Path>> shortestPathsMatrix) {
        this.adjacencyMatrix = shortestPathsMatrix;
        this.intersectionsCount = adjacencyMatrix.size();
    }

    public int getNbSommets() {
        return intersectionsCount;
    }

    public float getCout(long i, long j) {
        if (adjacencyMatrix.containsKey(i) && adjacencyMatrix.get(i).containsKey(j)) {
            return adjacencyMatrix.get(i).get(j).totalLength();
        }
        return -1;
    }

    public boolean estArc(long i, long j) {
        return (adjacencyMatrix.containsKey(i) && adjacencyMatrix.get(i).containsKey(j));
    }

    /**
     *
     * @return The adjacency matrix of the graph.
     */
    public HashMap<Long, HashMap<Long, Path>> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public List<Long> arcs(long i) {
        if (this.adjacencyMatrix.containsKey(i)) {
            return this.adjacencyMatrix.get(i).keySet().stream().toList();
        }
        return List.of();
    }


}
