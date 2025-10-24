package fr.delivrooom.application.service.tourcalculator;

import java.util.List;

public interface Graphe {

    /**
     * @return le nombre de sommets de <code>this</code>
     */
    int getNbSommets();

    /**
     * @return le cout de l'arc (i,j) si (i,j) est un arc ; -1 sinon
     */
    float getCout(long i, long j);

    /**
     * @return true si <code>(i,j)</code> est un arc de <code>this</code>
     */
    boolean estArc(long i, long j);

    List<Long> arcs(long i);


}
