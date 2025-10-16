package fr.delivrooom.application.tsp.custom;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Graphe;

import java.util.*;

public abstract class TemplateTSP implements TSP {

    private Long[] meilleureSolution;
    protected Graphe g;
    private float bestCost;
    private long tpsLimite;
    private long tpsDebut;
    private DeliveriesDemand demand;
    private HashMap<Long, Long> pickupToDelivery;


    public void chercheSolution(int tpsLimite, Graphe g, DeliveriesDemand demand){
        if (tpsLimite <= 0) return;
        tpsDebut = System.currentTimeMillis();
        this.tpsLimite = tpsLimite;
        this.demand = demand;
        this.g = g;

        meilleureSolution = new Long[g.getNbSommets()];
        HashSet<Long> reachableNodes = new HashSet<>();
        HashSet<Long> visitedNodes = new HashSet<>();
        pickupToDelivery = new HashMap<>();
        bestCost = Float.MAX_VALUE;

        // hashset of all reachable nodes (pickup points)
        for (Delivery d : demand.getDeliveries()) {
            reachableNodes.add(d.getTakeoutIntersection().getId());
            pickupToDelivery.put(d.getTakeoutIntersection().getId(), d.getDeliveryIntersection().getId());
        }

        // add the warehouse as a visited node
        visitedNodes.add(demand.getStore().getId());
        branchAndBound(demand.getStore().getId(), reachableNodes, visitedNodes, 0.f);

    }


    private void branchAndBound(long currentNode, HashSet<Long> reachableNodes, HashSet<Long> visitedNodes, float visitedCost){
        if (System.currentTimeMillis() - tpsDebut > tpsLimite) return;
        if (reachableNodes.size() == 0){ // every delivery point has been visited
            long warehouseId = demand.getStore().getId();
            if (g.estArc(currentNode, warehouseId)){ // return to warehouse
                if (visitedCost + g.getCout(currentNode, warehouseId) < bestCost){ // we found a solution better  than the best one
                    visitedNodes.toArray(meilleureSolution);
                    bestCost = (float) (visitedCost + g.getCout(currentNode, warehouseId));
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
                branchAndBound((Long) nextNode, reachableNodes, visitedNodes, visitedCost + (float) g.getCout(currentNode, nextNode));
                visitedNodes.remove(nextNode);
                reachableNodes.add(nextNode);
                if (pickupToDelivery.containsKey(nextNode)) {
                    Long deliveryPoint = pickupToDelivery.get(nextNode);
                    reachableNodes.remove(deliveryPoint);
                }
            }
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

}

