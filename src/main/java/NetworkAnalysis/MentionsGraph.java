package NetworkAnalysis;

import java.util.*;

public class MentionsGraph {
    long weight = 1;

    @Override
    public String toString() {
        return "MentionsGraph{" +
                "from nodes: " + adj.keySet() + " to [nodes, weights]: " + adj.values() + '}';
    }

    public HashMap<Long, ArrayList<ArrayList<Long>>> adj = new HashMap<>();

    //primo arraylist sono archi il secondo sono le variabili dentro cioe from, to, weight
    public void addVertex(long value) {
        if (adj.get(value) == null) {
            ArrayList<ArrayList<Long>> edges = new ArrayList<>();
            this.adj.put(value, edges);
        } else System.out.println("Vertex is already in the graph");
    }
    public void addAllEdges(ArrayList<ArrayList<Long>> arcs){
        for (ArrayList<Long> arc: arcs){
            if (this.adj.get(arc.get(0)) == null) {
                ArrayList<ArrayList<Long>> edges = new ArrayList<>();
                this.adj.put(arc.get(0), edges);
            }
            if (this.adj.get(arc.get(1)) == null) {
                ArrayList<ArrayList<Long>> edges = new ArrayList<>();
                this.adj.put(arc.get(1), edges);
            }
            ArrayList<ArrayList<Long>> fromEdges = getEdges(arc.get(0));
            fromEdges.add(arc);
        }
    }
    public void addEdge(long from, long to) {
        boolean modified = false;
        if (this.adj.get(from) == null) {
            ArrayList<ArrayList<Long>> edges = new ArrayList<>();
            this.adj.put(from, edges);
        }
        if (this.adj.get(to) == null) {
            ArrayList<ArrayList<Long>> edges = new ArrayList<>();
            this.adj.put(to, edges);
        }
        if(!this.adj.get(from).isEmpty()){
            ArrayList<ArrayList<Long>> edges =  getEdges(from);
            for(ArrayList<Long> edge : edges){
                if(from == edge.get(0) && to == edge.get(1)){ // se trovo due archi uguali aggiungo al arco peso +1
                    long new_weight= edge.get(2) +1;
                    edge.set(2,new_weight);
                    modified = true;
                    break;
                }
            }
        }
        if(!modified) {
            ArrayList<ArrayList<Long>> fromEdges = getEdges(from);
            ArrayList<Long> new_edge = new ArrayList<Long>();
            new_edge.add(from);
            new_edge.add(to);
            new_edge.add(weight);
            fromEdges.add(new_edge);
        }
    }

    public ArrayList<ArrayList<Long>> getEdges(long from) {
        ArrayList<ArrayList<Long>> fromEdges = new ArrayList<>();
        fromEdges = adj.get(from);
        return fromEdges;
    }


    ArrayList<ArrayList<Long>> largestConnectedComponents() {
        ArrayList<ArrayList<Long>> largestCC = new ArrayList<>();
        ArrayList<ArrayList<Long>> current_component = new ArrayList<>();
        //ArrayList<ArrayList<ArrayList<Long>>> components = new ArrayList<>();
        ArrayList<Long> keys = new ArrayList<>();
        keys.addAll(adj.keySet());      //creo una lista con le chiavi cioe id utenti
        int current_max_size=0;
        for (long k : keys) {
            current_component=find_cc(k);
            //System.out.println("size of components starting by root " + k + " = " + current_component.size() + " with following component : " + current_component);

            //components.add(current_component);
            if (current_max_size < current_component.size()){
                current_max_size = current_component.size();
                largestCC=current_component;
                System.out.println(largestCC);
            }
        }
        return largestCC;
    }

    ArrayList<ArrayList<Long>> find_cc(long k) {    //is modified bfs with [ if (!component.contains(node)) component.add(node); ] in other words tree from bfs with other arcs
        Queue<ArrayList<Long>> queue = new LinkedList<>();

        ArrayList<ArrayList<Long>> component = new ArrayList<>();  //una singola componente
        ArrayList<Long> visited = new ArrayList<>();
        ArrayList<ArrayList<Long>> root = adj.get(k);

        if(root.isEmpty()) return new ArrayList<>();        //if root is empty return empty
        visited.add(k);
        queue.addAll(root);

        while(!queue.isEmpty()) {
            ArrayList<ArrayList<Long>> current_root = adj.get(k);
            for (ArrayList<Long> node : current_root) {
                if(!visited.contains(node.get(1))) {
                    queue.add(node);
                    visited.add(node.get(1));
                }
                if (!component.contains(node)) component.add(node);
            }

            queue.poll();

            if (!queue.isEmpty()){
                k=queue.peek().get(1);

            }

        }
        return component;
    }
}