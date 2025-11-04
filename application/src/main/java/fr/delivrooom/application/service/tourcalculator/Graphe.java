package fr.delivrooom.application.service.tourcalculator;

import java.util.List;

/**
 * Interface representing a directed graph.
 */
public interface Graphe {

    /**
     * @return the number of vertices in <code>this</code>
     */
    int getNbSommets();

    /**
     * @param i the origin vertex.
     * @param j the destination vertex.
     * @return the cost of the arc (i,j) if (i,j) is an arc; -1 otherwise
     */
    float getCout(long i, long j);

    /**
     * @param i the origin vertex.
     * @param j the destination vertex.
     * @return true if <code>(i,j)</code> is an arc of <code>this</code>
     */
    boolean estArc(long i, long j);

    /**
     * @param i the vertex.
     * @return the list of successors of vertex <code>i</code>
     */
    List<Long> arcs(long i);

}
