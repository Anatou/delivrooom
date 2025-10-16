package fr.delivrooom.application.tsp;

import fr.delivrooom.application.model.Graphe;

import java.util.List;

public class CompleteGraph implements Graphe {

    private static final int MAX_COST = 40;
    private static final int MIN_COST = 10;
    final int vertexCount;
    final long[][] cost;

    /**
     * Creates a complete graph whose edges have a cost between MIN_COST and MAX_COST.
     */
    public CompleteGraph(int vertexCount) {
        this.vertexCount = vertexCount;
        int seed = 1;
        cost = new long[vertexCount][vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                if (i == j) {
                    cost[i][j] = -1;
                } else {
                    int t = 16807 * (seed % 127773) - 2836 * (seed / 127773);
                    if (t > 0) seed = t;
                    else seed = 2147483647 + t;
                    cost[i][j] = MIN_COST + seed % (MAX_COST - MIN_COST + 1);
                }
            }
        }
    }

    @Override
    public int getNbSommets() {
        return vertexCount;
    }

    @Override
    public float getCout(long i, long j) {
        if (i < 0 || i >= vertexCount || j < 0 || j >= vertexCount)
            return -1;
        return cost[(int) i][(int) j];
    }

    @Override
    public boolean estArc(long i, long j) {
        if (i < 0 || i >= vertexCount || j < 0 || j >= vertexCount)
            return false;
        return i != j;
    }

    @Override
    public List<Long> arcs(long i) {
        return List.of();
    }

}
