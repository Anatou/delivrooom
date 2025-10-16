package fr.delivrooom.application.tsp;

import fr.delivrooom.application.model.Graphe;

public class RunTSP {

    public static void main(String[] args) {
        TSP tsp = new TSP1();
        for (int vertexCount = 8; vertexCount <= 16; vertexCount += 2) {
            System.out.println("Graphs with " + vertexCount + " vertices:");
            Graphe g = new CompleteGraph(vertexCount);
            long startTime = System.currentTimeMillis();
            tsp.searchSolution(60000, g);
            System.out.print("Solution of length " + tsp.getSolutionCost() + " found in "
                    + (System.currentTimeMillis() - startTime) + " ms: ");
            for (int i = 0; i < vertexCount; i++)
                System.out.print(tsp.getSolution(i) + " ");
            System.out.println();
        }
    }
}
