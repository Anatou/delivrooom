package fr.delivrooom.application.tsp.custom;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Graphe;

public interface TSP {

    /**
     * Cherche une solution au TSP pour le graphe <code>g</code> dans la limite de <code>tpsLimite</code> millisecondes
     * Attention : la solution calculee commence necessairement par le sommet 0
     * @param tpsLimite
     * @param g
     */
    public void chercheSolution(int tpsLimite, Graphe g, DeliveriesDemand demand);

    /**
     * @return le ieme sommet visite dans la solution calculee par <code>chercheSolution</code>
     * (-1 si <code>chercheSolution</code> n'a pas encore ete appele, ou si i < 0 ou i >= g.getNbSommets())
     */
    public Long[] getSolution();

    /**
     * @return la somme des couts des arcs de la solution calculee par <code>chercheSolution</code>
     * (-1 si <code>chercheSolution</code> n'a pas encore ete appele).
     */
    public float getCoutSolution();

}
