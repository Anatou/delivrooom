package fr.delivrooom.application.tsp.example;

import fr.delivrooom.application.model.Graphe;

import java.util.Collection;
import java.util.Iterator;

public class IteratorSeq implements Iterator<Integer> {

    private final Integer[] candidates;
    private int candidateCount;

    /**
     * Creates an iterator over the vertices in 'unvisited' that are successors
     * of currentVertex in graph g, in the same order as they appear in 'unvisited'.
     */
    public IteratorSeq(Collection<Integer> unvisited, int currentVertex, Graphe g) {
        this.candidates = new Integer[unvisited.size()];
        Iterator<Integer> it = unvisited.iterator();
        while (it.hasNext()) {
            Integer v = it.next();
            if (g.estArc(currentVertex, v)) {
                candidates[candidateCount++] = v;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return candidateCount > 0;
    }

    @Override
    public Integer next() {
        candidateCount--;
        return candidates[candidateCount];
    }

    @Override
    public void remove() {
        // no-op
    }
}
