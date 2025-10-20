package fr.delivrooom.application.model.tsp;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Graphe;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

    @Override
    protected float bound(Long sommetCourant, Collection<Long> nonVus) {
        return 0;
    }

    @Override
    protected Iterator<Long> iterator(Long sommetCrt, Collection<Long> nonVus, Graphe g) {
        return new IteratorSeq(nonVus, sommetCrt, g);
    }


    @Override
    public void chercheSolution(int tpsLimite, Graphe g, DeliveriesDemand demand, NotifyTSPProgressToGui notifyTSPProgressToGui) {

    }
}
