package NetworkAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MentionsGraph {
    long weight=1;
    @Override
    public String toString() {
        return "MentionsGraph{" +
                "from nodes: "+adj.keySet()+ " to [nodes, weights]: " + adj.values() + '}';
    }

    public Map<Long, ArrayList<ArrayList<Long>>> adj = new HashMap<>();

    //primo arraylist sono archi il secondo sono le variabili dentro cioe from, to, weight
    public void addVertex(long value) {
        if(adj.get(value) == null) {
            ArrayList<ArrayList<Long>> edges = new ArrayList<>();
            this.adj.put(value, edges);
        }
        else System.out.println("Vertex is already in the graph");
    }

    public void addEdge(long from, long to) {
        boolean modified=false;
        if (this.adj.get(from)==null){
            ArrayList<ArrayList<Long>> edges = new ArrayList<>();
            this.adj.put(from , edges);
        }
        if(!this.adj.get(from).isEmpty()){
            ArrayList<ArrayList<Long>> edges =  getEdges(from);
            for(ArrayList<Long> edge : edges){
                if(to == edge.get(0)){ // se trovo due archi uguali aggiungo al arco peso +1
                    long new_weight= edge.get(1) +1;
                    edge.set(1,new_weight);
                    modified = true;
                    break;
                }
            }
        }
        if(!modified) {
            ArrayList<ArrayList<Long>> fromEdges = getEdges(from);
            ArrayList<Long> new_edge = new ArrayList<Long>();
            //new_edge.add(from);
            new_edge.add(to);
            new_edge.add(weight);
            fromEdges.add(new_edge);
        }
    }

        public  ArrayList<ArrayList<Long>> getEdges(long from){
            ArrayList<ArrayList<Long>> fromEdges = new ArrayList<>();
            fromEdges=adj.get(from);
            return fromEdges;
        }


}