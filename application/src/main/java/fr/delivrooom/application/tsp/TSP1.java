package fr.delivrooom.application.tsp;

import fr.delivrooom.application.model.Graphe;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

	@Override
	/*
	Cette fonction d’évaluation calcule une borne inférieure de la longueur
	du plus court chemin allant du dernier sommet visité jusqu’à 0 en passant par chaque sommet non visité exactement
	une fois 1. Si la longueur du chemin correspondant aux sommets déjà visités ajoutée à cette borne est supérieure ou
	égale à la longueur du plus court circuit trouvé jusqu’ici (i.e., best), alors nous pouvons en déduire qu’il n’existe pas
	de solution améliorante commençant par ce chemin, et il n’est pas nécessaire d’appeler permut récursivement.

	 */
	protected int bound(Integer sommetCourant, Collection<Integer> nonVus) {
		return 0;
	}

	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, Collection<Integer> nonVus, Graphe g) {
		return new IteratorSeq(nonVus, sommetCrt, g);
	}

}
