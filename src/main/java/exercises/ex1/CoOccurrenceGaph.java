package exercises.ex1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class CoOccurrenceGaph {

        String weight="1";
        @Override
        public String toString() {
            return "CoOccurrenceGaph{" +
                    "from nodes: "+adj.keySet()+ " to [nodes, weights]: " + adj.values() + '}';
        }

        public HashMap<String, ArrayList<ArrayList<String>>> adj = new HashMap<>();

        //primo arraylist sono archi il secondo sono le variabili dentro cioe from, to, weight
        public void addVertex(String value) {
            if(adj.get(value) == null) {
                ArrayList<ArrayList<String>> edges = new ArrayList<>();
                this.adj.put(value, edges);
            }
            else System.out.println("Vertex is already in the graph");
        }

        public void addEdge(String from, String to) {
            boolean modified=false;
            ArrayList<ArrayList<String>> edges1 = new ArrayList<>();
            ArrayList<ArrayList<String>> edges2 = new ArrayList<>();
            this.adj.putIfAbsent(from, edges1);
            this.adj.putIfAbsent(to, edges2);
       /* if (this.adj.get(to)==null){

            this.adj.put(to , edges);
        }*/
            if(!this.adj.get(from).isEmpty()){  //non ce bisogno di fare anche per to perche è un grafo indiretto
                edges1 =  getEdges(from);
                for(ArrayList<String> edge : edges1){
                    if( to == edge.get(1)){ // se trovo due archi uguali aggiungo al arco peso +1
                        edge.set(2,String.valueOf(Integer.parseInt(edge.get(2)) + 1));
                        modified = true;
                        break;
                    }

                }
                if (modified) {
                    edges2 = getEdges(to);
                    for (ArrayList<String> edge : edges2) {
                        if (from.equals(edge.get(1))) { // se trovo due archi uguali aggiungo al arco peso +1

                            edge.set(2,String.valueOf(Integer.parseInt(edge.get(2)) + 1));
                            modified = true;
                            break;
                        }
                    }
                }
            }

            if(!modified) {
                ArrayList<ArrayList<String>> fromEdges = getEdges(from);
                ArrayList<ArrayList<String>> toEdges = getEdges(to);

                ArrayList<String> new_edge1 = new ArrayList<String>();
                ArrayList<String> new_edge2 = new ArrayList<String>();

                new_edge1.add(from);
                new_edge1.add(to);
                new_edge1.add(weight);
                fromEdges.add(new_edge1);

                new_edge2.add(to);
                new_edge2.add(from);
                new_edge2.add(weight);
                toEdges.add(new_edge2);
            }
        }
        public void addAllEdges(ArrayList<ArrayList<String>> arcs){
            for (ArrayList<String> arc: arcs){
                if (this.adj.get(arc.get(0)) == null) {
                    ArrayList<ArrayList<String>> edges = new ArrayList<>();
                    this.adj.put(arc.get(0), edges);
                }
                if (this.adj.get(arc.get(1)) == null) {
                    ArrayList<ArrayList<String>> edges = new ArrayList<>();
                    this.adj.put(arc.get(1), edges);
                }
                ArrayList<ArrayList<String>> fromEdges = getEdges(arc.get(0));
                fromEdges.add(arc);
            }
        }


        public  ArrayList<ArrayList<String>> getEdges(String a){
            ArrayList<ArrayList<String>> edges = new ArrayList<>();
            edges=adj.get(a);
            return edges;
        }

        ArrayList<ArrayList<String>> largestConnectedComponents() {
            ArrayList<ArrayList<String>> largestCC = new ArrayList<>();
            ArrayList<ArrayList<String>> current_component = new ArrayList<>();
            //ArrayList<ArrayList<ArrayList<Long>>> components = new ArrayList<>();
            ArrayList<String> keys = new ArrayList<>();
            keys.addAll(adj.keySet());      //creo una lista con le chiavi cioe id utenti
            int current_max_size=0;
            for (String k : keys) {
                current_component=find_cc(k);
                //components.add(current_component);
                if (current_max_size < current_component.size()){
                    current_max_size = current_component.size();
                    largestCC=current_component;
                }
            }
            return largestCC;
        }

    CoOccurrenceGaph k_core(int k) {
        CoOccurrenceGaph graph = this;
        ArrayList<String> nodes;
        boolean empty=false;
        while (!empty){
            CoOccurrenceGaph curr_graph = new CoOccurrenceGaph();
            nodes = new ArrayList<>();  //archi con grado minore di k
            for (String key : graph.adj.keySet()) {
                if (graph.adj.get(key).size() < k) {
                    nodes.add(key);
                }
            }
            for (String key : graph.adj.keySet()) {
                if (!nodes.contains(key)) {     // se nodo non ha grado < k?
                    for (ArrayList<String> arcs : adj.get(key)) {
                        if (!nodes.contains(arcs.get(1))) { // se nodo to non ha grado < k
                            curr_graph.addEdge(arcs.get(0), arcs.get(1));    //aggiungo arco
                        }
                    }
                }
            }
            graph = curr_graph;
            if (nodes.isEmpty()) empty=true;    //se non ci sono nodi con grado minore di k
        }

    return graph;
    }

        ArrayList<ArrayList<String>> find_cc(String k) {
            Queue<ArrayList<String>> queue = new LinkedList<>();

            ArrayList<ArrayList<String>> component = new ArrayList<>();  //una singola componente
            ArrayList<String> visited = new ArrayList<>();
            ArrayList<ArrayList<String>> root = adj.get(k);

            if(root.isEmpty()) return new ArrayList<>();        //if root is empty return empty
            visited.add(k);
            queue.addAll(root);

            while(!queue.isEmpty()) {
                ArrayList<ArrayList<String>> current_root = adj.get(k);
                for (ArrayList<String> node : current_root) {
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
