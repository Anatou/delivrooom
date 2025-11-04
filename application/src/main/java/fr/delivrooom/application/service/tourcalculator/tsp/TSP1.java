package fr.delivrooom.application.service.tourcalculator.tsp;

import fr.delivrooom.application.service.tourcalculator.Graphe;

import java.util.Collection;
import java.util.Iterator;

/**
 * A basic implementation of the TSP solver.
 * This version uses a trivial bound function (always returns 0), resulting in a simple
 * brute-force exploration of all possible hamiltonian circuits.
 */
public class TSP1 extends TemplateTSP {

    @Override
    protected float bound(Long sommetCourant, Collection<Long> nonVus) {
        return 0;
    }

    @Override
    protected Iterator<Long> iterator(Long sommetCrt, Collection<Long> nonVus, Graphe g) {
        return new IteratorSeq(nonVus, sommetCrt, g);
    }

}
