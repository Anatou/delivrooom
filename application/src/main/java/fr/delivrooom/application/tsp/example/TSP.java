package fr.delivrooom.application.tsp.example;

import fr.delivrooom.application.model.Graphe;

public interface TSP {

    /**
     * Search for a TSP solution for the graph g within timeLimit milliseconds.
     * Note: the computed solution necessarily starts with vertex 0.
     */
    void searchSolution(int timeLimit, Graphe g);

    /**
     * @return the i-th vertex visited in the solution computed by searchSolution
     * (-1 if searchSolution has not been called yet, or if i < 0 or i >= g.getNbSommets())
     */
    Integer getSolution(int i);

    /**
     * @return the sum of the costs of the arcs of the solution computed by searchSolution
     * (-1 if searchSolution has not been called yet).
     */
    int getSolutionCost();
}
