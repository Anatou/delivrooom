package fr.delivrooom.application.model.tsp;

import fr.delivrooom.application.model.Graphe;

import java.util.Collection;
import java.util.Iterator;

public class TSP2 extends TemplateTSP {

    @Override
    protected float bound(Long currentVertex, Collection<Long> unseen) {
        /**
         * Returns the sum, for each unseen vertex, of the length of the shortest arc that
         * connects it to the circuit. More precisely:
         * - let l be the length of the shortest arc leaving the last visited vertex and
         *   arriving at one of the unseen vertices;
         * - for each unseen vertex i, let li be the length of the shortest arc leaving i
         *   and arriving either at 0 or at one of the unseen vertices (other than i).
         * The function returns l + ∑_i li.
         */
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
