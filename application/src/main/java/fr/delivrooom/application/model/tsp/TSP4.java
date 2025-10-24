package fr.delivrooom.application.model.tsp;

import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Graphe;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;

import java.util.*;

public class TSP4 extends TemplateTSP {

    @Override
    protected float bound(Long currentVertex, Collection<Long> unseen) {
        /**
         Approche exacte basée sur un principe de programmation dynamique, qui a une complexité en temps
         inférieure à celle de l’algorithme par séparation et évaluation (même si cette complexité reste exponentielle),
         mais qui n’est pas anytime (elle calcule une seule solution, qui est la solution optimale) ;

         La propriété d’optimalité des sous-chemins vue en cours nous permet de définir D(i, S)
         récursivement :
         — si S = ∅, alors D(i, S) = cout[i][0] ;
         — si S̸ = ∅, alors D(i, S) = minj∈S (cout[i][j] + D(j, S \ {j}).
         */

        // lists to store costs and predecessors for Prim's algorithm


        float bound = 0.F;
        float minOutgoing = Float.MAX_VALUE;
        float minToZero = Float.MAX_VALUE;
        for (Long unseenVertex : unseen) {
            float length = this.g.getCout(currentVertex, unseenVertex);

            // cost of the smallest arc from the last visited vertex to an unseen vertex
            if (length != -1 && length < minOutgoing) {
                minOutgoing = length;
            }
            float lengthToZero = this.g.getCout(unseenVertex, demand.store().getId());
            // cost of the smallest arc from an unseen vertex to the warehouse
            if (lengthToZero != -1 && lengthToZero < minToZero) {
                minToZero = lengthToZero;
            }
        }

        // cost of the minimum spanning tree of the subgraph induced by unseen vertices
        /**
         * fonction prim(G, s)
         *        pour tout sommet t
         *               cout[t] := +∞
         *               pred[t] := null
         *        cout[s] := 0
         *        F := file de priorité contenant les sommets de G avec cout[.] comme priorité
         *        tant que F ≠ vide
         *             t := F.defiler
         *             pour toute arête t--u avec u appartenant à F
         *                 si cout[u] >= poids de l'arête entre les sommets t et u
         *                        pred[u] := t
         *                        cout[u] := poids de l'arête entre les sommets t et u
         *                        F.notifierDiminution(u)
         *
         *        retourner pred
         */

        // retrieve all the edges from the unseen vertices and store their cost
        HashMap<Long, HashMap<Long, Float>> edgesFromUnseenVertices = new HashMap<>();
        for (Long v : unseen) {
            HashMap<Long, Float> m = new HashMap<>();
            for (Long u : unseen) {
                if (!v.equals(u)) {
                    float len = this.g.getCout(v, u);
                    if (len != -1) m.put(u, len);
                }
            }
            edgesFromUnseenVertices.put(v, m);
        }

        if (unseen.isEmpty()) {
            return 0f;
        }

        // best known edge weight to connect each vertex to the growing MST
        Map<Long, Float> best = new HashMap<>();
        for (Long v : unseen) best.put(v, Float.POSITIVE_INFINITY);

        PriorityQueue<AbstractMap.SimpleEntry<Long, Float>> pq =
                new PriorityQueue<>(Comparator.comparing(AbstractMap.SimpleEntry::getValue));

        // arbitrary start vertex from unseen
        Long start = unseen.iterator().next();
        best.put(start, 0f);
        pq.add(new AbstractMap.SimpleEntry<>(start, 0f));

        Set<Long> visited = new HashSet<>();
        float mstCost = 0f;

        while (!pq.isEmpty()) {
            // get the vertex with the smallest edge weight to the MST
            AbstractMap.SimpleEntry<Long, Float> entry = pq.poll();

            Long v = entry.getKey();
            float weight = entry.getValue();

            if (visited.contains(v)) {
                continue;
            }
            visited.add(v);

            // add the accepted edge weight (0 for the root)
            mstCost += weight;

            // update the best known edge weights for neighbors
            HashMap<Long, Float> neighbors = edgesFromUnseenVertices.getOrDefault(v, new HashMap<>());
            for (Map.Entry<Long, Float> neigh : neighbors.entrySet()) {
                Long u = neigh.getKey();
                float w = neigh.getValue();
                if (!visited.contains(u) && w < best.get(u)) {
                    best.put(u, w);
                    // reinsert the neighbors with updated weight
                    pq.add(new AbstractMap.SimpleEntry<>(u, w));
                }
            }
        }
        return bound + minOutgoing + minToZero + mstCost;
    }

    @Override
    protected Iterator<Long> iterator(Long currentVertex, Collection<Long> unseen, Graphe g) {
        return new IteratorSeq(unseen, currentVertex, g);
    }
    
}
