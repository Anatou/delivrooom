package fr.delivrooom.application.model.tsp;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Graphe;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;

public interface TSP {

    /**
     * Cherche une solution au TSP pour le graphe <code>g</code> dans la limite de <code>tpsLimite</code> millisecondes
     * Attention : la solution calculee commence necessairement par le sommet 0
     * @param tpsLimite
     * @param g
     */
    public void searchSolution(int tpsLimite, Graphe g, DeliveriesDemand demand, NotifyTSPProgressToGui notifyTSPProgressToGui);

    /**
     * @return le ieme sommet visite dans la solution calculee par <code>chercheSolution</code>
     * (-1 si <code>chercheSolution</code> n'a pas encore ete appele, ou si i < 0 ou i >= g.getNbSommets())
     */
    public Long[] getBestSolution();

    /**
     * @return la somme des couts des arcs de la solution calculee par <code>chercheSolution</code>
     * (-1 si <code>chercheSolution</code> n'a pas encore ete appele).
     */
    public float getBestCost();

}
