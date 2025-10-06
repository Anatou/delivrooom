package fr.delivrooom.application.model;

import java.util.HashMap;

public class CityGraph {

    protected HashMap<Long, HashMap<Long, Integer>> adjacencyMatrix;

    public CityGraph(HashMap<Long, HashMap<Long, Integer>> adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }
}
