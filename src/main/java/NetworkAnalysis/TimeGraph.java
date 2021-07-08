package NetworkAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeGraph {
    long weight=1;
    @Override
    public String toString() {
        return "TimeGraph{" +
                "adj=" + adj.values() + adj.keySet()+
                '}';
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
        ArrayList<ArrayList<Long>> edges1 = new ArrayList<>();
        ArrayList<ArrayList<Long>> edges2 = new ArrayList<>();
        this.adj.putIfAbsent(from, edges1);
        this.adj.putIfAbsent(to, edges2);
       /* if (this.adj.get(to)==null){

            this.adj.put(to , edges);
        }*/
        if(!this.adj.get(from).isEmpty()){  //non ce bisogno di fare anche per to perche è un grafo indiretto
            edges1 =  getEdges(from);
            for(ArrayList<Long> edge : edges1){
                if(from == edge.get(0) && to == edge.get(1)){ // se trovo due archi uguali aggiungo al arco peso +1
                    edge.set(2,edge.get(2) + 1);
                    modified = true;
                    break;
                }

            }
            if (modified) {
                edges2 = getEdges(to);
                for (ArrayList<Long> edge : edges2) {
                    if (to == edge.get(0) && from == edge.get(1)) { // se trovo due archi uguali aggiungo al arco peso +1

                        edge.set(2, edge.get(2) + 1);
                        modified = true;
                        break;
                    }
                }
            }
        }

        if(!modified) {
            ArrayList<ArrayList<Long>> fromEdges = getEdges(from);
            ArrayList<ArrayList<Long>> toEdges = getEdges(to);
            ArrayList<Long> new_edge1 = new ArrayList<Long>();
            ArrayList<Long> new_edge2 = new ArrayList<Long>();
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

    public  ArrayList<ArrayList<Long>> getEdges(long a){
        ArrayList<ArrayList<Long>> edges = new ArrayList<>();
        edges=adj.get(a);
        return edges;
    }


}




