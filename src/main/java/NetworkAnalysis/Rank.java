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
            HashMap<Long, ArrayList<Long>> new_hits = hits;
            for (Long id : hits.keySet()) {    // update all authority values first
                ArrayList<Long> new_score = new_hits.get(id);
                ArrayList<Long> old_score = hits.get(id);
                Long actual_score = old_score.get(1);
                new_score.set(1, zero);//p.auth = 0     new_score = p

                for (Long key : graph.keySet()) { //for each page q in p.incomingNeighbors do // p.incomingNeighbors is the set of pages that link to p
                    for (ArrayList<Long> arcs : graph.get(key)) {
                        if (id.equals(arcs.get(1))) {
                            Long qhub = (hits.get(arcs.get(0))).get(2);     //prende valore hub dalla tabella hits per i nodi q che hanno un arco verso  nodo p
                            new_score.set(1,old_score.get(1) + (qhub * arcs.get(2)));//p.auth += q.hub *weight of arc
                        }
                    }
                }
                if (new_score.get(1).equals(zero)) new_score.set(1,actual_score);

                //norm += Math.sqrt(new_score.get(1)); // norm += square(p.auth) // calculate the sum of the squared auth values to normalise
            }
           /* for (long id : hits.keySet()) { // for each page p in G do  // update the auth scores
                ArrayList<Long> new_score = hits.get(id);
                //new_score.set(1,new_score.get(1)/norm); //p.auth = p.auth / norm  // normalise the auth values
            }
            norm = 0;*/
            for (Long id : hits.keySet()) {    // update all hub values first
                ArrayList<Long> new_score = new_hits.get(id);
                ArrayList<Long> old_score = hits.get(id);

                Long actual_score = new_score.get(2);
                new_score.set(2, zero);//p.hyb = 0

                ArrayList<ArrayList<Long>> arcs = graph.get(id);
                for (ArrayList<Long> arc : arcs){   //for each page r in p.outgoingNeighbors do // p.outgoingNeighbors is the set of pages that p links to
                    Long rauth = (hits.get(arc.get(1))).get(2);
                    new_score.set(2, old_score.get(2) + (rauth * arc.get(2)));//p.auth += q.hub *weight of arc
                }
                if (new_score.get(2).equals(zero)) new_score.set(2,actual_score);
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
            }
            /*for (long id : hits.keySet()) { // for each page p in G do  // update the auth scores
                ArrayList<Long> new_score = hits.get(id);
              //  new_score.set(1,new_score.get(2)/norm); //p.auth = p.auth / norm  // normalise the auth values
            }*/
            hits = new_hits;
        }
        return hits;
    }


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


    public boolean reach(HashMap<Long,ArrayList<ArrayList<Long>>> graph, Long start, Long end, ArrayList<Long> reached){
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
                if(node.get(1).equals(end) || reached.contains(node.get(1))) return true;
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

    HashMap<Integer,ArrayList<Long>> LPA(HashMap<Long, ArrayList<ArrayList<Long>>> graph){

        HashMap<Integer,ArrayList<Long>> lpa_nodes = new HashMap<>();
        int initial_size=60;
        int num_of_labels = 2;


        ArrayList<ArrayList<Long>> labels = new ArrayList<>();
        labels.add(new ArrayList<>());
        lpa_nodes.put(0, labels.get(0));

        for (Long user_id : graph.keySet()){        //asegno ad ogni nodo un label 0
            labels.get(0).add(user_id);    //ogni key corrispone ad un label, qui assegno ad label zero tutti i nodi
        }

        // assegno a due nodi random due label differenti
        Long[] node = graph.keySet().toArray(new Long[0]);
        int size = graph.keySet().size();
        Random random = new Random();

        ArrayList<Integer> randoms_label1 = new ArrayList<>();
        while (randoms_label1.size()!=initial_size-1){
            int casuale = random.nextInt(size-1);
            if (!randoms_label1.contains(casuale)) randoms_label1.add(casuale);
        }

        ArrayList<Integer> randoms_label2 = new ArrayList<>();
        while (randoms_label2.size()!=initial_size-1){
            int casuale = random.nextInt(size-1);
            if (!randoms_label1.contains(casuale) && !randoms_label2.contains(casuale)) randoms_label2.add(casuale);
        }
      /*
        int casuale1 = random.nextInt(size-1);
        int casuale2 = random.nextInt(size-1);
        while (casuale1==casuale2){ // nel caso fossero uguali
            casuale2=random.nextInt(size-1);
        }*/


        labels.add ( new ArrayList<>());
        labels.add( new ArrayList<>() );

        for(Integer num : randoms_label1){
            Long nodo = node[num];
            labels.get(1).add(nodo);
        }
        lpa_nodes.put(1,labels.get(1));

        for(Integer num : randoms_label2){
            Long nodo = node[num];
            labels.get(2).add(nodo);
        }
        lpa_nodes.put(2,labels.get(2));


        //Label propagation


        ArrayList<Long> new_label0 = new ArrayList<>();
        for (Long vertex : lpa_nodes.get(0)){
            if(lpa_nodes.get(1).contains(vertex) || lpa_nodes.get(2).contains(vertex)) continue;
            new_label0.add(vertex);
        }
        lpa_nodes.put(0,new_label0);


        ArrayList<Long> nodes_already_labeled = new ArrayList<>();
        nodes_already_labeled.addAll(lpa_nodes.get(1));
        nodes_already_labeled.addAll(lpa_nodes.get(2));

        HashMap<Integer,ArrayList<Long>> nodes_to_visit= new HashMap<>();
        nodes_to_visit.put(1, lpa_nodes.get(1));
        nodes_to_visit.put(2,lpa_nodes.get(2));


        int iter=1;
        boolean something_to_visit=true;
        while (!lpa_nodes.get(0).isEmpty() && something_to_visit) {  //finche ce almeno un nodo in label0

        System.out.println("nodes_to_visit, while : "+ nodes_to_visit);
            ArrayList<ArrayList<Long>> user_to_add_in_this_iteration = new ArrayList<>();
            user_to_add_in_this_iteration.add(new ArrayList<>()); // non si aggiunge niente al label0
            for (int i = 1; i<= num_of_labels; i++){

                //ArrayList<Long> current_label = lpa_nodes.get(i);       //questo si potrebbe ottimizare perche in iterazione +2 chiama nodi gia visti
                ArrayList<Long> current_label = nodes_to_visit.get(i);
                ArrayList<Long> users_to_add = new ArrayList<>();
                for(Long user : current_label){ //per ogni utente nel labbel corrente

                    for(ArrayList<Long> connection : graph.get(user)){  //ogni nodo a cui e' connesso viene aggiunto al label
                        Long current_user = connection.get(1);      // (from,to,weight) 1 corrisponde a to
                        if (!nodes_already_labeled.contains(current_user) && !users_to_add.contains(current_user)) {
                            users_to_add.add(current_user);
                        }
                        }
                    }
                user_to_add_in_this_iteration.add(users_to_add);
               /* for (Long not_labeled_user : users_to_add){
                    user_to_add_in_this_iteration.add(users_to_add);
                    nodes_already_labeled.add(not_labeled_user);
                }*/
                }
            int current_label=0;
            int current_num_of_labels = num_of_labels;
            ArrayList<Long> new_added = new ArrayList<>();
            //System.out.println("user_to_add_in_this_iteration : "+user_to_add_in_this_iteration);
            for (ArrayList<Long> label : user_to_add_in_this_iteration){//aggiunta nodi ai label
                ArrayList<Long> next_nodes_to_visit= new ArrayList<>();
                for (Long user : label){
                    boolean free = true; //

                    for(int i = 1; i <= current_num_of_labels; i++){        ////controllo se due label competono per lo stesso user
                        if (i == current_label) continue;

                        //qui bug da risolvere considera solo il primo label altri no!
                        if(new_added.contains(user)){
                            free= false;

                        }

                        if (user_to_add_in_this_iteration.get(i).contains(user) && !new_added.contains(user)) {  //se si creo nuovo label e lo aggiungo la
                            free= false;
                            num_of_labels++;
                            ArrayList<Long> nodes_of_new_label = new ArrayList<>();
                            nodes_of_new_label.add(user);
                            lpa_nodes.put(num_of_labels,nodes_of_new_label);

                            nodes_to_visit.put(num_of_labels,nodes_of_new_label);

                            new_added.add(user);
                        }


                    }
                    if (free){
                        lpa_nodes.get(current_label).add(user);
                        next_nodes_to_visit.add(user);
                    }
                    nodes_already_labeled.add(user);

                    //remove form label0
                    new_label0 = new ArrayList<>();
                    for (Long vertex : lpa_nodes.get(0)){
                    //    System.out.println("vertex: " + vertex + ", nodo: "+user);
                        if(user.equals(vertex)) continue;
                        new_label0.add(vertex);
                    }
                //    System.out.println("new_label0 : "+new_label0);
                    lpa_nodes.put(0,new_label0);
                }
                nodes_to_visit.put(current_label,next_nodes_to_visit);
                current_label++;
            }

            boolean check = false;      //controllare se ci sta almeno un nodo da visitare;
            for(Integer label_to_visit : nodes_to_visit.keySet()) {
                if (!nodes_to_visit.get(label_to_visit).isEmpty()) check = true;    //se ce mette check a true
            }
            if (!check) something_to_visit=false;   // se e' tutto vuoto while si ferma

/*
            System.out.println("nodes_to_visit : "+nodes_to_visit);
            System.out.println("lpa_nodes : "+lpa_nodes);
            System.out.println("");

*/
        }
        return lpa_nodes;
    }
}
