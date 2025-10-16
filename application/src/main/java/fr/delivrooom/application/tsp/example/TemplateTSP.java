package fr.delivrooom.application.tsp.example;

import fr.delivrooom.application.model.Graphe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {

    protected Graphe g;
    private Integer[] bestSolution;
    private int bestSolutionCost;
    private int timeLimit;
    private long startTime;

    /**
     * Search for a TSP solution for graph g within the given time limit in milliseconds.
     * Note: the computed solution necessarily starts with vertex 0.
     */
    @Override
    public void searchSolution(int timeLimit, Graphe g) {
        if (timeLimit <= 0) return;
        startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        this.g = g;
        bestSolution = new Integer[g.getNbSommets()];
        Collection<Integer> unvisited = new ArrayList<>(g.getNbSommets() - 1);
        for (int i = 1; i < g.getNbSommets(); i++) unvisited.add(i);
        Collection<Integer> visited = new ArrayList<>(g.getNbSommets());
        visited.add(0); // the first visited vertex is 0
        bestSolutionCost = Integer.MAX_VALUE;
        branchAndBound(0, unvisited, visited, 0);
    }

    @Override
    public Integer getSolution(int i) {
        if (g != null && i >= 0 && i < g.getNbSommets())
            return bestSolution[i];
        return -1;
    }

    @Override
    public int getSolutionCost() {
        if (g != null)
            return bestSolutionCost;
        return -1;
    }

    /**
     * Must be implemented by subclasses.
     *
     * @return a lower bound of the cost of paths in g starting from currentVertex,
     * visiting all vertices in unvisited exactly once, then returning to vertex 0.
     */
    protected abstract int bound(Integer currentVertex, Collection<Integer> unvisited);

    /**
     * Must be implemented by subclasses.
     *
     * @return an iterator over all vertices in unvisited that are successors of currentVertex
     */
    protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graphe g);

    /**
     * Defines the template of a branch-and-bound resolution of the TSP for graph g.
     * @param currentVertex the last visited vertex
     * @param unvisited the list of vertices not yet visited
     * @param visited the list of already visited vertices (including currentVertex)
     * @param costVisited the sum of the costs of the arcs along the path visiting vertices in 'visited' in order
     */
    private void branchAndBound(int currentVertex,
                                Collection<Integer> unvisited,
                                Collection<Integer> visited,
                                int costVisited) {
        if (System.currentTimeMillis() - startTime > timeLimit) return;
        if (unvisited.isEmpty()) { // all vertices have been visited
            if (g.estArc(currentVertex, 0)) { // can return to start vertex (0)
                if (costVisited + g.getCout(currentVertex, 0) < bestSolutionCost) { // found a better solution
                    visited.toArray(bestSolution);
                    bestSolutionCost = (int) (costVisited + g.getCout(currentVertex, 0));
                }
            }
        } else if (costVisited + bound(currentVertex, unvisited) < bestSolutionCost) {
            Iterator<Integer> it = iterator(currentVertex, unvisited, g);
            while (it.hasNext()) {
                Integer nextVertex = it.next();
                visited.add(nextVertex);
                unvisited.remove(nextVertex);
                branchAndBound(nextVertex, unvisited, visited,
                        costVisited + (int) g.getCout(currentVertex, nextVertex));
                visited.remove(nextVertex);
                unvisited.add(nextVertex);
            }
        }
    }
}
