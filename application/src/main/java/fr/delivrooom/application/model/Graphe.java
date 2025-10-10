package fr.delivrooom.application.model;

import java.util.List;

public interface Graphe {

	/**
	 * @return le nombre de sommets de <code>this</code>
	 */
	public abstract int getNbSommets();

	/**
	 * @param i 
	 * @param j 
	 * @return le cout de l'arc (i,j) si (i,j) est un arc ; -1 sinon
	 */
	public abstract float getCout(long i, long j);

	/**
	 * @param i 
	 * @param j 
	 * @return true si <code>(i,j)</code> est un arc de <code>this</code>
	 */
	public abstract boolean estArc(long i, long j);

    public abstract List<Long> arcs(long i);


}