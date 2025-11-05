package fr.delivrooom.application.service.tourcalculator.tsp;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;
import fr.delivrooom.application.service.tourcalculator.Graphe;

import java.util.*;

/**
 * An abstract template for Traveling Salesperson Problem (TSP) solvers.
 * This class provides the basic structure for branch and bound algorithms.
 * Subclasses must implement the {@code bound} and {@code iterator} methods.
 */
public abstract class TemplateTSP implements TSP {

    protected Long[] meilleureSolution;
    protected Graphe g;
    protected float bestCost;
    protected long tpsLimite;
    protected long tpsDebut;
    protected DeliveriesDemand demand;
    protected HashMap<Long, Long> pickupToDelivery;
    protected int calls;

    // progress-bar related
    protected double maxPossibilities;
    protected double consideredPossibilities;
    protected NotifyTSPProgressToGui notifyTSPProgressToGui;
    protected double lastProgress;


    public TemplateTSP() {
        this.calls = 0;
        this.maxPossibilities = Double.MAX_VALUE;
        this.consideredPossibilities = 0;
        this.lastProgress = 0.;
    }

    public void searchSolution(int tpsLimite, Graphe g, DeliveriesDemand demand, NotifyTSPProgressToGui notifyTSPProgressToGui) {
        if (tpsLimite <= 0) return;
        tpsDebut = System.currentTimeMillis();
        this.notifyTSPProgressToGui = notifyTSPProgressToGui;
        this.tpsLimite = tpsLimite;
        this.demand = demand;
        this.g = g;
        this.calls = 0;
        this.consideredPossibilities = 0;
        this.maxPossibilities = this.orderedPermutationCount(g.getNbSommets() - 1, demand.deliveries().size());
        System.out.println("Calculated max possibilities : " + maxPossibilities);

        meilleureSolution = new Long[g.getNbSommets()];
        HashSet<Long> reachableNodes = new HashSet<>();
        List<Long> visitedNodes = new ArrayList<>();
        pickupToDelivery = new HashMap<>();
        bestCost = Float.MAX_VALUE;

        // hashset of all reachable nodes (pickup points)
        for (Delivery d : demand.deliveries()) {
            reachableNodes.add(d.takeoutIntersection().getId());
            pickupToDelivery.put(d.takeoutIntersection().getId(), d.deliveryIntersection().getId());
        }

        // add the warehouse as a visited node
        visitedNodes.add(demand.store().getId());
        branchAndBound(demand.store().getId(), reachableNodes, visitedNodes, 0.f);
        System.out.println("Number of calls: " + calls);

        System.out.println("Total number of possibilities considered : " + this.consideredPossibilities);
        System.out.println("Final Progress " + (float) (this.consideredPossibilities / this.maxPossibilities * 100) + "%");
    }


    private void branchAndBound(long currentNode, HashSet<Long> reachableNodes, List<Long> visitedNodes, float visitedCost) {

        ++calls;
        //System.out.println("branchAndBound call " + calls + " (" + (double)consideredPossibilities/maxPossibilities*100 + "%) : \n - currentNode : " + currentNode + "\n - reachableNodes : "+ reachableNodes + "\n - visitedNodes : " + visitedNodes + "\n - visitedCost : " + visitedCost + "\n - bestCost : " + bestCost);

        if (System.currentTimeMillis() - tpsDebut > tpsLimite) {
            System.out.println("Timeout reached after " + tpsLimite + " ms and " + calls + " calls.");
            return;
        }
        if (reachableNodes.isEmpty()) { // every delivery point has been visited
            ++consideredPossibilities;
            notifyProgress(notifyTSPProgressToGui);
            long warehouseId = demand.store().getId();
            if (g.estArc(currentNode, warehouseId)) { // return to warehouse
                if (visitedCost + g.getCout(currentNode, warehouseId) < bestCost) { // we found a solution better  than the best one
                    visitedNodes.toArray(meilleureSolution);
                    bestCost = (visitedCost + g.getCout(currentNode, warehouseId));
                }
            }
        } else if (visitedCost + bound(currentNode, reachableNodes) < bestCost){
            Iterator<Long> it = iterator(currentNode, reachableNodes, g);
            while (it.hasNext()){
                Long nextNode = it.next();
                visitedNodes.add(nextNode);
                reachableNodes.remove(nextNode);
                // add the delivery point associated with the pickup point if the point was a pickup point
                if (pickupToDelivery.containsKey(nextNode)) {
                    Long deliveryPoint = pickupToDelivery.get(nextNode);
                    reachableNodes.add(deliveryPoint);
                }
                branchAndBound(nextNode, reachableNodes, visitedNodes, visitedCost + g.getCout(currentNode, nextNode));
                visitedNodes.remove(nextNode);
                reachableNodes.add(nextNode);
                if (pickupToDelivery.containsKey(nextNode)) {
                    Long deliveryPoint = pickupToDelivery.get(nextNode);
                    reachableNodes.remove(deliveryPoint);
                }
            }
        } else {
            // we managed to skip a certain number of possibilities thanks to bound, but we must account for them in the progress bar

            // we need to count how many constraints are left, i.e. how many pickups are not done
            int leftToVisit = g.getNbSommets() - visitedNodes.size();
            int pickupsDone = demand.arePickups(visitedNodes);
            int constraints = (g.getNbSommets() - 1) / 2 - pickupsDone;
            this.consideredPossibilities += this.orderedPermutationCount(leftToVisit, constraints);

            notifyProgress(notifyTSPProgressToGui);
        }
    }


    /**
     * @param sommetCourant
     * @param nonVus
     * @return a lower bound of the cost of paths in <code>g</code> starting from <code>sommetCourant</code>, visiting
     * all the vertices of <code>nonVus</code> exactly once, and returning to the vertex <code>0</code>.
     */
    protected abstract float bound(Long sommetCourant, Collection<Long> nonVus);

    /**
     * Abstract method to be defined by subclasses of TemplateTSP.
     * @param sommetCrt
     * @param nonVus
     * @param g
     * @return an iterator allowing to iterate over all the vertices of <code>nonVus</code> which are successors of <code>sommetCourant</code>
     */
    protected abstract Iterator<Long> iterator(Long sommetCrt, Collection<Long> nonVus, Graphe g);

    public Long[] getBestSolution() {
        return meilleureSolution;
    }

    public float getBestCost() {
        if (meilleureSolution == null) return -1;
        return bestCost;
    }

    protected double factorial(long n) {
        long res = 1;
        for (long i = 2; i <= n; ++i) {
            res *= i;
        }
        return res;
    }

    protected double orderedPermutationCount(long n, long k) {
        // returns the number of possible permutations of n elements with k constraints of precedence in a pair
        return (this.factorial(n) / Math.pow(2, k));
    }

    protected void notifyProgress(NotifyTSPProgressToGui notifyTSPProgressToGui) {
        float progress = (float) (int) (this.consideredPossibilities / this.maxPossibilities * 100);
        if ((int) progress != (int) this.lastProgress) {
            notifyTSPProgressToGui.notifyTSPProgressToGui(progress / 100);
            this.lastProgress = progress;
        }
    }

}

