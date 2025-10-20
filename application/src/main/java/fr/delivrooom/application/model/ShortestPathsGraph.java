package fr.delivrooom.application.model;

import java.util.HashMap;
import java.util.List;

public class ShortestPathsGraph implements Graphe{

    protected HashMap<Long, HashMap<Long, Path>> adjacencyMatrix;
    protected int intersectionsCount;

    public ShortestPathsGraph(HashMap<Long, HashMap<Long, Path>> shortestPathsMatrix) {
        this.adjacencyMatrix = shortestPathsMatrix;
        this.intersectionsCount = adjacencyMatrix.size();
    }

    public int getNbSommets() {
        return intersectionsCount;
    }

    public float getCout(long i, long j) {
        if (adjacencyMatrix.containsKey(i) && adjacencyMatrix.get(i).containsKey(j)) {
            return adjacencyMatrix.get(i).get(j).totalLength();
        }
        return -1;
    }

    public boolean estArc(long i, long j){
        return (adjacencyMatrix.containsKey(i) && adjacencyMatrix.get(i).containsKey(j));
    }

    public HashMap<Long, HashMap<Long, Path>> getAdjacencyMatrix(){
        return adjacencyMatrix;
    }

    public List<Long> arcs(long i){
        if (this.adjacencyMatrix.containsKey(i)) {
            return this.adjacencyMatrix.get(i).keySet().stream().toList();
        }
        return List.of();
    }


}
