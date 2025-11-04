package fr.delivrooom.application.service.tourcalculator.tsp;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;
import fr.delivrooom.application.service.tourcalculator.Graphe;

import java.util.*;

// TODO : add progress bar updates in the computeD method

/**
 * An implementation of the Traveling Salesperson Problem (TSP) using dynamic programming
 * with memoization (Held-Karp algorithm). This approach finds the optimal solution but has
 * an exponential time complexity of O(n^2 * 2^n).
 * It handles precedence constraints (pickup before delivery).
 */
public class DynamicProgrammingTSP extends TemplateTSP {
    protected Long[] meilleureSolution;
    protected Graphe g;
    protected float bestCost;
    protected DeliveriesDemand demand;
    protected Map<Long, Long> pickupToDelivery;
    protected Map<Long, Long> deliveryToPickup;

    // memoization table
    protected float[][] memD;
    // table to reconstruct the path
    protected int[][] next;
    protected List<Long> vertices;

    // progress-bar related
    protected double maxPossibilities;
    protected double consideredPossibilities;
    protected NotifyTSPProgressToGui notifyTSPProgressToGui;

    @Override
    public void searchSolution(int tpsLimite, Graphe g, DeliveriesDemand demand, NotifyTSPProgressToGui notifyTSPProgressToGui) {
        this.notifyTSPProgressToGui = notifyTSPProgressToGui;
        this.g = g;
        this.demand = demand;
        this.bestCost = Float.POSITIVE_INFINITY;

        this.vertices = new ArrayList<>();
        // starting point (warehouse)
        vertices.add(demand.store().getId());
        pickupToDelivery = new HashMap<>();
        deliveryToPickup = new HashMap<>();

        for (var d : demand.deliveries()) {
            Long pickup = d.takeoutIntersection().getId();
            Long delivery = d.deliveryIntersection().getId();
            vertices.add(pickup);
            vertices.add(delivery);
            pickupToDelivery.put(pickup, delivery);
            deliveryToPickup.put(delivery, pickup);
        }

        int n = vertices.size();
        // calculate the subsmask and initialize memoization tables
        int nbMasks = 1 << (n - 1);
        memD = new float[n][nbMasks];
        next = new int[n][nbMasks];

        for (int i = 0; i < n; i++) {
            // initialize memD with NaN to indicate uncomputed states
            Arrays.fill(memD[i], Float.NaN);
        }


        int fullMask = (1 << (n - 1)) - 1;
        bestCost = computeD(0, fullMask);
        List<Long> order = reconstructOrder();
        meilleureSolution = order.toArray(new Long[0]);
    }

    /**
     * This method is not used in the dynamic programming approach.
     *
     * @param sommetCourant The current vertex.
     * @param nonVus        The collection of unvisited vertices.
     * @return 0
     */
    @Override
    protected float bound(Long sommetCourant, Collection<Long> nonVus) {
        return 0;
    }

    /**
     * This method is not used in the dynamic programming approach.
     * @param sommetCrt The current vertex.
     * @param nonVus The collection of unvisited vertices.
     * @param g The graph.
     * @return null
     */
    @Override
    protected Iterator<Long> iterator(Long sommetCrt, Collection<Long> nonVus, Graphe g) {
        return null;
    }

    protected float computeD(int i, int mask) {
        /*
        Ce sous-problème est noté D(i, S), et la solution de ce sous-problème est la longueur du plus court
chemin allant de i jusque 0 en passant par chaque sommet de S exactement une fois.
         */
        if (!Float.isNaN(memD[i][mask])) {
            // if this state has already been computed, return the stored value
            return memD[i][mask];
        }
        if (mask == 0) {
            // return to the warehouse
            float cost = g.getCout(vertices.get(i), vertices.get(0));
            memD[i][mask] = cost;
            next[i][mask] = 0;
            return cost;
        }

        float minCost = Float.POSITIVE_INFINITY;
        int bestNext = -1;
        int n = vertices.size();

        // run throuhg every vertices index (j)  in the mask
        for (int j = 1; j < n; j++) {
            int bit = 1 << (j - 1);
            if ((mask & bit) != 0) {
                // get the real vertex id
                Long current = vertices.get(j);

                if (deliveryToPickup.containsKey(current)) {
                    // if current is a delivery, we firstly get its corresponding pickup
                    Long pickup = deliveryToPickup.get(current);
                    // we get the index of the pickup vertex in order to check if its bit position is set in the mask
                    int pickupIndex = vertices.indexOf(pickup);
                    int pickupBit = 1 << (pickupIndex - 1);
                    if ((mask & pickupBit) != 0) {
                        // if the pickup is still in the mask -> means it has not been visited yet, therefore we cannot visit the delivery
                        continue;
                    }
                }

                float costIJ = g.getCout(vertices.get(i), current);


                // compute the next mask without j
                int nextMask = mask & ~bit;
                // recursive call to computeD
                float subCost = computeD(j, nextMask);
                if (!Float.isInfinite(subCost)) {
                    // total cost if we go from i to j and then solve the subproblem from j
                    float total = costIJ + subCost;
                    if (total < minCost) {
                        minCost = total;
                        bestNext = j;
                    }
                }
            }
        }

        // store the computed value in the memoization table
        memD[i][mask] = minCost;
        next[i][mask] = bestNext;
        return minCost;
    }

    protected List<Long> reconstructOrder() {
        List<Long> order = new ArrayList<>();
        int n = vertices.size();
        int mask = (1 << (n - 1)) - 1;
        int current = 0;
        order.add(vertices.get(0)); // add starting point (warehouse)

        while (mask != 0) {
            int nxt = next[current][mask];
            if (nxt <= 0) {
                break;
            }
            order.add(vertices.get(nxt));
            mask &= ~(1 << (nxt - 1));
            current = nxt;
        }

        return order;
    }

    @Override
    public Long[] getBestSolution() {
        return meilleureSolution;
    }

    @Override
    public float getBestCost() {
        return bestCost;
    }
}
