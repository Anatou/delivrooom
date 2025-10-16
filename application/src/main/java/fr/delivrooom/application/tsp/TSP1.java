package fr.delivrooom.application.tsp;

import fr.delivrooom.application.model.Graphe;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

    @Override
    /*
     This evaluation function computes a lower bound of the length of the shortest path
     going from the last visited vertex to 0 while visiting each unvisited vertex exactly once.
     If the length of the path corresponding to the already visited vertices plus this bound is
     greater than or equal to the length of the best tour found so far, then we can deduce that
     no improving solution starts with this path, and recursion is not necessary.
     */
    protected int bound(Integer currentVertex, Collection<Integer> unvisited) {
        return 0;
    }

    @Override
    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graphe g) {
        return new IteratorSeq(unvisited, currentVertex, g);
    }

}
