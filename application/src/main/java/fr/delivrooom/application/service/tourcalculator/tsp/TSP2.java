package fr.delivrooom.application.service.tourcalculator.tsp;

import fr.delivrooom.application.service.tourcalculator.Graphe;

import java.util.Collection;
import java.util.Iterator;

/**
 * A TSP solver that improves upon {@link TSP1} by using a more effective lower bound calculation.
 * The bound is the sum of the shortest arc from the current vertex to an unvisited vertex,
 * and for each unvisited vertex, the shortest arc from it to another unvisited vertex or the depot.
 * This allows for more effective pruning of the search tree.
 */
public class TSP2 extends TemplateTSP {

    @Override
    protected float bound(Long currentVertex, Collection<Long> unseen) {
        float bound = 0.F;
        float minOutgoing = Float.MAX_VALUE;
        for (Long unseenVertex : unseen) {
            float length = this.g.getCout(currentVertex, unseenVertex);
            if (length != -1 && length < minOutgoing) {
                minOutgoing = length;
            }
        }
        bound += minOutgoing;
        for (Long unseenVertex : unseen) {
            float minLink = this.g.getCout(unseenVertex, 0L);
            for (Long otherUnseen : unseen) {
                if (!unseenVertex.equals(otherUnseen)) {
                    float length = this.g.getCout(unseenVertex, otherUnseen);
                    if (length != -1 && length < minLink) {
                        minLink = length;
                    }
                }
            }
            bound += minLink;
        }
        return bound;
    }

    @Override
    protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unseen, Graphe g) {
        return new IteratorSeq(unseen, currentVertex, g);
    }

}
