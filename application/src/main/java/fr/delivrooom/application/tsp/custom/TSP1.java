package fr.delivrooom.application.tsp.custom;

import fr.delivrooom.application.model.Graphe;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

    @Override
    protected int bound(Long sommetCourant, Collection<Long> nonVus) {
        return 0;
    }

    @Override
    protected Iterator<Long> iterator(Long sommetCrt, Collection<Long> nonVus, Graphe g) {
        return new IteratorSeq(nonVus, sommetCrt, g);
    }





}
