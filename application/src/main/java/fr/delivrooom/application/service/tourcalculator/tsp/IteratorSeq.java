package fr.delivrooom.application.service.tourcalculator.tsp;

import fr.delivrooom.application.service.tourcalculator.Graphe;

import java.util.Collection;
import java.util.Iterator;

/**
 * A sequential iterator over the successors of a vertex in a graph.
 * This iterator is used in the TSP solvers to explore possible next vertices.
 */
public class IteratorSeq implements Iterator<Long> {

	private Long[] candidats;
	private int nbCandidats;

    /**
     * Creates an iterator over the vertices in 'unvisited' that are successors
     * of currentVertex in graph g, in the same order as they appear in 'unvisited'.
     * @param nonVus The collection of unvisited vertices.
     * @param sommetCrt The current vertex.
     * @param g The graph.
     */
	public IteratorSeq(Collection<Long> nonVus, long sommetCrt, Graphe g){
		this.candidats = new Long[nonVus.size()];
		Iterator<Long> it = nonVus.iterator();
		while (it.hasNext()){
			long s = it.next();
			if (g.estArc(sommetCrt, s))
				candidats[nbCandidats++] = s;
		}
	}

	@Override
	public boolean hasNext() {
		return nbCandidats > 0;
	}

	@Override
	public Long next() {
		nbCandidats--;
		return candidats[nbCandidats];
	}

	@Override
	public void remove() {}
}
