package fr.delivrooom.application.model;

import java.util.HashMap;
import java.util.List;


public class CityGraph implements Graphe {

    protected HashMap<Integer, HashMap<Integer, Integer>> cost;
    protected HashMap<Integer, List<Integer>> adjacencyList;
    protected int intersectionsCount;

    public CityGraph(HashMap<Integer, HashMap<Integer, Integer>> cost, HashMap<Integer, List<Integer>> adjacencyList, int intersectionsCount) {
        this.cost = cost;
        this.adjacencyList = adjacencyList;
        this.intersectionsCount = intersectionsCount;
    }

    //TODO : créer un constructeur prenant une CityMap

    @Override
    public int getNbSommets() {
        return intersectionsCount;
    }

    @Override
    public int getCout(int i, int j) {
        return this.cost.get(i).get(j);
    }

    @Override
    public boolean estArc(int i, int j) {
        return this.cost.containsKey(i) && this.cost.get(i).containsKey(j);
    }

    @Override
    public List<Integer> arcs(int i) {
        if (this.adjacencyList.containsKey(i)) {
            return this.adjacencyList.get(i);
        }
        return null;
    }
}
