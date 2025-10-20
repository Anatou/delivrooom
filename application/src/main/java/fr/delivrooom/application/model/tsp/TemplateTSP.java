package fr.delivrooom.application.model.tsp;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Graphe;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;

import java.util.*;

public abstract class TemplateTSP implements TSP {

    protected Long[] meilleureSolution;
    protected Graphe g;
    protected float bestCost;
    protected long tpsLimite;
    protected long tpsDebut;
    protected DeliveriesDemand demand;
    protected HashMap<Long, Long> pickupToDelivery;
    protected int calls;

    protected double maxPossibilities;
    protected double consideredPossibilities;


    public TemplateTSP() {
        this.calls = 0;
        this.maxPossibilities = Double.MAX_VALUE;
        this.consideredPossibilities = 0;
    }

    public void chercheSolution(int tpsLimite, Graphe g, DeliveriesDemand demand, NotifyTSPProgressToGui notifyTSPProgressToGui){
        if (tpsLimite <= 0) return;
        tpsDebut = System.currentTimeMillis();
        this.tpsLimite = tpsLimite;
        this.demand = demand;
        this.g = g;
        this.calls = 0;
        this.consideredPossibilities = 0;
        this.maxPossibilities = this.orderedPermutationCount(g.getNbSommets(), demand.deliveries().size());
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
        branchAndBound(demand.store().getId(), reachableNodes, visitedNodes, 0.f, notifyTSPProgressToGui);

        System.out.println("Total number of possibilities considered : " + this.consideredPossibilities);
    }


    private void branchAndBound(long currentNode, HashSet<Long> reachableNodes, List<Long> visitedNodes, float visitedCost, NotifyTSPProgressToGui notifyTSPProgressToGui){

        ++calls;
        //System.out.println("branchAndBound call " + calls + " (" + (double)consideredPossibilities/maxPossibilities*100 + "%) : \n - currentNode : " + currentNode + "\n - reachableNodes : "+ reachableNodes + "\n - visitedNodes : " + visitedNodes + "\n - visitedCost : " + visitedCost + "\n - bestCost : " + bestCost);

        //if (System.currentTimeMillis() - tpsDebut > tpsLimite) return; //TODO: throw a timeout exception so we know whether or not we have a perfect solution
        if (reachableNodes.isEmpty()){ // every delivery point has been visited
            ++consideredPossibilities;
            notifyProgress(notifyTSPProgressToGui);
            long warehouseId = demand.store().getId();
            if (g.estArc(currentNode, warehouseId)){ // return to warehouse
                if (visitedCost + g.getCout(currentNode, warehouseId) < bestCost){ // we found a solution better  than the best one
                    visitedNodes.toArray(meilleureSolution);
                    bestCost = (float) (visitedCost + g.getCout(currentNode, warehouseId));
                    System.out.println("New best solution : " + Arrays.toString(meilleureSolution) + " cost " + bestCost);
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
                branchAndBound((Long) nextNode, reachableNodes, visitedNodes, visitedCost + (float) g.getCout(currentNode, nextNode), notifyTSPProgressToGui);
                visitedNodes.remove(nextNode);
                reachableNodes.add(nextNode);
                if (pickupToDelivery.containsKey(nextNode)) {
                    Long deliveryPoint = pickupToDelivery.get(nextNode);
                    reachableNodes.remove(deliveryPoint);
                }
            }
        } else {
            // we managed to skip a certain number of possibilities thanks to bound, but we must account for them in the progress bar

            // we need to count how many constraints are left, i.e. how pickups are not done
            int leftToVisit = g.getNbSommets() -1 - visitedNodes.size();
            int pickupsDone = demand.arePickups(visitedNodes);
            int constraints = (g.getNbSommets()-1)/2 - pickupsDone;
            this.consideredPossibilities += this.orderedPermutationCount(leftToVisit, constraints);
            //this.consideredPossibilities += this.orderedPermutationCount(reachableNodes.size(), reachableNodes.size()-pickupsDone);

            notifyProgress(notifyTSPProgressToGui);
        }
    }


    /**
     * Methode devant etre redefinie par les sous-classes de TemplateTSP
     * @param sommetCourant
     * @param nonVus
     * @return une borne inferieure du cout des chemins de <code>g</code> partant de <code>sommetCourant</code>, visitant
     * tous les sommets de <code>nonVus</code> exactement une fois, puis retournant sur le sommet <code>0</code>.
     */
    protected abstract int bound(Long sommetCourant, Collection<Long> nonVus);

    /**
     * Methode devant etre redefinie par les sous-classes de TemplateTSP
     * @param sommetCrt
     * @param nonVus
     * @param g
     * @return un iterateur permettant d'iterer sur tous les sommets de <code>nonVus</code> qui sont successeurs de <code>sommetCourant</code>
     */
    protected abstract Iterator<Long> iterator(Long sommetCrt, Collection<Long> nonVus, Graphe g);

    public Long[] getSolution(){
        return meilleureSolution;
    }

    public float getCoutSolution(){
        if (meilleureSolution == null) return -1;
        return bestCost;
    }

    protected double factorial (long n) {
        long res = 1;
        for (long i = 2; i < n; ++i) {
            res *= i;
        }
        return res;
    }

    protected double orderedPermutationCount(long n, long k) {
        // returns the number of possible permutations of n elements with k constraints of precedence in a pair
        return (double) (this.factorial(n) / Math.pow(2, k ));
    }

    protected void notifyProgress(NotifyTSPProgressToGui notifyTSPProgressToGui) {
        notifyTSPProgressToGui.notifyTSPProgressToGui((float) (this.consideredPossibilities/this.maxPossibilities*100));
    }

}

