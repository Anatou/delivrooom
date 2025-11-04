package fr.delivrooom.application.service.tourcalculator.tsp;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;
import fr.delivrooom.application.service.tourcalculator.Graphe;

/**
 * Interface for Traveling Salesperson Problem (TSP) solvers.
 */
public interface TSP {

    /**
     * Search for a solution to the TSP for the given graph within a time limit.
     * Note: The calculated solution must start from vertex 0.
     *
     * @param tpsLimite              The time limit in milliseconds.
     * @param g                      The graph to solve the TSP for.
     * @param demand                 The delivery demand, containing constraints.
     * @param notifyTSPProgressToGui A callback to notify the GUI of progress.
     */
    void searchSolution(int tpsLimite, Graphe g, DeliveriesDemand demand, NotifyTSPProgressToGui notifyTSPProgressToGui);

    /**
     * @return An array of vertex IDs representing the best tour found.
     * Returns {@code null} or an empty array if no solution has been found.
     */
    Long[] getBestSolution();

    /**
     * @return The cost of the best solution found.
     * Returns -1 if no solution has been found.
     */
    float getBestCost();

}
