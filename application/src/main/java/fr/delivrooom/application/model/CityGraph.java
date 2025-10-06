package fr.delivrooom.application.model;

import java.util.HashMap;


public class CityGraph implements Graphe {

    protected HashMap<Integer, HashMap<Integer, Integer>> adjacencyMatrix;
    protected int intersectionsCount;

    public CityGraph(HashMap<Integer, HashMap<Integer, Integer>> adjacencyMatrix, int intersectionsCount) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.intersectionsCount = intersectionsCount;
    }

    @Override
    public int getNbSommets() {
        return intersectionsCount;
    }

    @Override
    public int getCout(int i, int j) {
        return this.adjacencyMatrix.get(i).get(j);
    }

    @Override
    public boolean estArc(int i, int j) {
        return this.adjacencyMatrix.containsKey(i) && this.adjacencyMatrix.get(i).containsKey(j);
    }

}
