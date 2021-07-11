package NetworkAnalysis;

import it.stilo.g.algo.ReachabilityScore;
import it.stilo.g.algo.UnionDisjoint;
import it.stilo.g.structures.DoubleValues;
import it.stilo.g.structures.WeightedDirectedGraph;

import java.util.*;

public class Rank {
    static long zero = 0;
    static long one = 1;
    static int steps = 5;

    HashMap<Long, ArrayList<Long>> HITS(HashMap<Long, ArrayList<ArrayList<Long>>> graph) {
        HashMap<Long, ArrayList<Long>> hits = new HashMap<>();

        //id, (auth, hubs score setting to one)
        for (Long id : graph.keySet()) {
            ArrayList<Long> scores = new ArrayList<>();
            scores.add(id);
            scores.add(one);        //p.auth = 1 // p.auth is the authority score of the page p
            scores.add(one);        //p.hub = 1 // p.hub is the hub score of the page p
            hits.put(id, scores);
        }
        for (int i = 1; i <= steps; i++) {// run the algorithm for k steps
            //int norm = 0;

            for (long id : hits.keySet()) {    // update all authority values first
                ArrayList<Long> new_score = hits.get(id);
                new_score.set(1, zero);//p.auth = 0     new_score = p

                for (long key : graph.keySet()) { //for each page q in p.incomingNeighbors do // p.incomingNeighbors is the set of pages that link to p
                    for (ArrayList<Long> arcs : graph.get(key)) {
                        if (id == arcs.get(1)) {
                            ArrayList<Long> scores = hits.get(arcs.get(0));
                            long qhub = scores.get(2);
                            new_score.set(1,new_score.get(1) + (qhub * arcs.get(2)));//p.auth += q.hub *weight of arc
                        }
                    }
                }
                //norm += Math.sqrt(new_score.get(1)); // norm += square(p.auth) // calculate the sum of the squared auth values to normalise
                //norm=1;
            }
           /* for (long id : hits.keySet()) { // for each page p in G do  // update the auth scores
                ArrayList<Long> new_score = hits.get(id);
                //new_score.set(1,new_score.get(1)/norm); //p.auth = p.auth / norm  // normalise the auth values
            }*/
            /// simetrico
           // norm = 0;
            for (long id : hits.keySet()) {    // update all hub values first
                ArrayList<Long> new_score = hits.get(id);
                new_score.set(2, zero);//p.hyb = 0

                ArrayList<ArrayList<Long>> arcs = graph.get(id);
                for (ArrayList<Long> arc : arcs){   //for each page r in p.outgoingNeighbors do // p.outgoingNeighbors is the set of pages that p links to
                    ArrayList<Long> scores = hits.get(arc.get(1));
                    long rauth = scores.get(2);
                    new_score.set(2, new_score.get(2) + (rauth * arc.get(2)));//p.auth += q.hub *weight of arc
                }
                /*
                for (long key : graph.keySet()) { //for each page r in p.outgoingNeighbors do // p.outgoingNeighbors is the set of pages that p links to
                    for (ArrayList<Long> arcs : graph.get(key)) {
                        if (id == arcs.get(0)) {
                            ArrayList<Long> scores = hits.get(arcs.get(1));
                            long rauth = scores.get(2);
                            new_score.set(1, rauth * arcs.get(2));//p.auth += q.hub *weight of arc
                        }
                    }
                }*/
                //norm += Math.sqrt(new_score.get(2)); // norm += square(p.auth) // calculate the sum of the squared auth values to normalise
               // norm=1;
            }
            /*for (long id : hits.keySet()) { // for each page p in G do  // update the auth scores
                ArrayList<Long> new_score = hits.get(id);
              //  new_score.set(1,new_score.get(2)/norm); //p.auth = p.auth / norm  // normalise the auth values
            }*/
        }
        return hits;
    }
/*
// pseudocode given from wikipedia https://en.wikipedia.org/wiki/HITS_algorithm

   *     G := set of pages
   *     for each page p in G do
   *         p.auth = 1 // p.auth is the authority score of the page p
   *         p.hub = 1 // p.hub is the hub score of the page p
   *     for step from 1 to k do // run the algorithm for k steps
   *         norm = 0
   *         for each page p in G do  // update all authority values first
   *             p.auth = 0
   *             for each page q in p.incomingNeighbors do // p.incomingNeighbors is the set of pages that link to p
   *                 p.auth += q.hub
   *             norm += square(p.auth) // calculate the sum of the squared auth values to normalise
   *         norm = sqrt(norm)
   *         for each page p in G do  // update the auth scores
   *             p.auth = p.auth / norm  // normalise the auth values
   *         norm = 0
   *         for each page p in G do  // then update all hub values
   *             p.hub = 0
   *             for each page r in p.outgoingNeighbors do // p.outgoingNeighbors is the set of pages that p links to
   *                 p.hub += r.auth
   *             norm += square(p.hub) // calculate the sum of the squared hub values to normalise
   *         norm = sqrt(norm)
   *         for each page p in G do  // then update all hub values
   *             p.hub = p.hub / norm   // normalise the hub values */

//devo prendere i nodi che arrivano a un certo nodo

    HashMap<Long,Integer> KP_NEG(HashMap<Long,ArrayList<ArrayList<Long>>> graph){
        HashMap<Long,Integer> result = new HashMap<>();
         // compute measure of entire graph
        int Cg = 0;

        for(Long pointed : graph.keySet()){     //per ogni utente puntato

            //per ottimizare, per controllare se un nodo precedente ha gia puntato a quello che si cerca (riga 174)
            ArrayList<Long> reached = new ArrayList<>();

            for(Long pointer: graph.keySet()){  // per ogni utente che lo punta - se stesso
                if(pointed.equals(pointer)){ Cg++; continue;}
                if(reach(graph,pointer,pointed,reached)) {
                    reached.add(pointer);
                    Cg++;
                    //(kp_neg.get(pointed)).add(pointer);
                }//implemento bfs fino ad raggiungere nodo
            }
        }
         // compute Cg_vi for each vi
        System.out.println(Cg);
         for (Long vi : graph.keySet()){     //per ogni G - vi

             int cg_vi=0;
             for (Long pointed : graph.keySet()){ //per ogni utente puntato - v
                 if(pointed.equals(vi)) continue;
                 ArrayList<Long> reached = new ArrayList<>();    //for optimization nodes that point to pointed
                 for(Long pointer: graph.keySet()){  // per ogni utente che lo punta - se stesso
                     if (pointer.equals(vi)) continue;
                     if(pointed.equals(pointer)){cg_vi++; continue;}

                     if(reach(graph,pointer,pointed,reached)) {//implemento bfs fino ad raggiungere pointed
                         reached.add(pointer);
                         cg_vi++;
                     }
                 }
             }

             System.out.println(cg_vi);
             result.put(vi,cg_vi);
         }

         for(Long score : result.keySet()){    //compute Cg-cg_vi
             result.put(score,Cg-result.get(score));
         }
        return result;
    }


    public boolean reach(HashMap<Long,ArrayList<ArrayList<Long>>> graph, long start, long end, ArrayList<Long> reached){
        Queue<ArrayList<Long>> queue = new LinkedList<>();

        ArrayList<Long> visited = new ArrayList<>();
        ArrayList<ArrayList<Long>> root = graph.get(start);

        if(root.isEmpty()) return false;        //if root is empty return false

        visited.add(start);
        queue.addAll(root);

        while(!queue.isEmpty()) {
            ArrayList<ArrayList<Long>> current_root = graph.get(start);
            if (graph.get(start) == null) return false;
            for (ArrayList<Long> node : current_root) {
                if(node.get(1) == end || reached.contains(node.get(1))) return true;
                if(!visited.contains(node.get(1)) && graph.containsKey(node.get(1))) {
                    queue.add(node);
                    visited.add(node.get(1));
                }
            }
            queue.poll();
            if (!queue.isEmpty()){
                start=queue.peek().get(1);

            }
    }
    return false;
    }

    void LPA(HashMap<Long, ArrayList<Long>> graph){

    }
}
