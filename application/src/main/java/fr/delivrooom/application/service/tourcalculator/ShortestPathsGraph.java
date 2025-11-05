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

    /**
     * Returns the number of vertices (origins) represented in this reduced graph.
     * Note that this corresponds to the number of entries in the adjacency matrix,
     * not necessarily the number of unique intersection ids present across both rows and columns.
     *
     * @return the number of vertices in this graph.
     */
    public int getNbSommets() {
        return intersectionsCount;
    }

    /**
     * Returns the cost of the directed edge from {@code i} to {@code j}.
     * In this reduced graph, the cost is the total length of the precomputed shortest {@link Path}.
     * <p>
     * If no entry exists for the pair \({i, j}\), {@code -1} is returned.
     *
     * @param i the origin intersection id.
     * @param j the destination intersection id.
     * @return the total length of the shortest path from {@code i} to {@code j}, or {@code -1} if absent.
     */
    public float getCout(long i, long j) {
        if (adjacencyMatrix.containsKey(i) && adjacencyMatrix.get(i).containsKey(j)) {
            return adjacencyMatrix.get(i).get(j).totalLength();
        }
        return -1;
    }

    /**
     * Indicates whether a shortest path from {@code i} to {@code j} is present in the matrix.
     *
     * @param i the origin intersection id.
     * @param j the destination intersection id.
     * @return {@code true} if an entry exists for \({i, j}\); {@code false} otherwise.
     */
    public boolean estArc(long i, long j) {
        return (adjacencyMatrix.containsKey(i) && adjacencyMatrix.get(i).containsKey(j));
    }

    /**
     * Returns the adjacency matrix of this reduced graph.
     *
     * @return the adjacency matrix mapping origin ids to destination ids and their {@link Path}.
     */
    public HashMap<Long, HashMap<Long, Path>> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    /**
     * Returns the list of destination intersection ids directly reachable from {@code i}
     * in this reduced graph (i.e., all keys present in the row for {@code i}).
     *
     * If {@code i} has no outgoing entries, an empty list is returned.
     *
     * @param i the origin intersection id.
     * @return a list of destination ids reachable from {@code i}.
     */
    public List<Long> arcs(long i) {
        if (this.adjacencyMatrix.containsKey(i)) {
            return this.adjacencyMatrix.get(i).keySet().stream().toList();
        }
        return List.of();
    }
}
