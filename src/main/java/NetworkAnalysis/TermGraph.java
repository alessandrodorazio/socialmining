package NetworkAnalysis;

import entites.Tweet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.*;

public class TermGraph {
    long weight=1;
    @Override
    public String toString() {
        return "TermGraph{" +
                "from nodes: "+adj.keySet()+ " to [nodes, weights]: " + adj.values() + '}';
    }

    public HashMap<Long, ArrayList<ArrayList<Long>>> adj = new HashMap<>();

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
                if( to == edge.get(1)){ // se trovo due archi uguali aggiungo al arco peso +1
                    edge.set(2,edge.get(2) + 1);
                    modified = true;
                    break;
                }

            }
            if (modified) {
                edges2 = getEdges(to);
                for (ArrayList<Long> edge : edges2) {
                    if (from == edge.get(1)) { // se trovo due archi uguali aggiungo al arco peso +1

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


    public  ArrayList<ArrayList<Long>> getEdges(long a){
        ArrayList<ArrayList<Long>> edges = new ArrayList<>();
        edges=adj.get(a);
        return edges;
    }

    public ArrayList<ArrayList<Long>> largestConnectedComponents() {
        ArrayList<ArrayList<Long>> largestCC = new ArrayList<>();
        ArrayList<ArrayList<Long>> current_component = new ArrayList<>();
        //ArrayList<ArrayList<ArrayList<Long>>> components = new ArrayList<>();
        ArrayList<Long> keys = new ArrayList<>();
        keys.addAll(adj.keySet());      //creo una lista con le chiavi cioe id utenti
        int current_max_size=0;
        for (long k : keys) {
            current_component=find_cc(k);
            //components.add(current_component);
            if (current_max_size < current_component.size()){
                current_max_size = current_component.size();
                largestCC=current_component;
            }
        }
        return largestCC;
    }

    public ArrayList<ArrayList<Long>> find_cc(long k) {
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
 public void populate() throws IOException, ParseException {
        Directory dir = getWindow(1);   //solo per il primo per fare prima altrimenti un for
        BufferedReader csvReader;

        HashMap<Integer, ArrayList<String>> terms_in_cluster = new HashMap<>();
        String row;
        csvReader = new BufferedReader(new FileReader("temporal_analysis/clusters/terms/cluster_of_terms_time_window_" + "1" + ".csv"));
        while ((row = csvReader.readLine()) != null) { //for each term
            if (row.split(",")[0].equals("ClusterId") || row.split(",")[0].equals("Cluster ID"))
                continue;       //la prima riga la salta

            int cluster = Integer.parseInt(row.split(",")[0]);
            String term = row.split(",")[1];

            if (!terms_in_cluster.containsKey(cluster)) terms_in_cluster.put(cluster, new ArrayList<>());
            terms_in_cluster.get(cluster).add(term);
     }
        System.out.println("ho trovato clusers dei termini");
     ArrayList<Tweet> tweets;
        for (Integer cluster : terms_in_cluster.keySet()){

            tweets = new ArrayList<>(get_Tweets(dir, terms_in_cluster.get(cluster)));
            ArrayList<Long> user_ids = new ArrayList<>();
            for (Tweet tweet : tweets){
                Long current_user_id= Long.parseLong(tweet.User_ID);
                if(user_ids.contains(current_user_id)) continue;
                user_ids.add(current_user_id);
            }
            TermGraph tg = new TermGraph();
            //faccio clique utente x utente
            //lento da ottimizzare o verficare se funziona
            int i =0;
            for (Long user_id : user_ids){
                for (Long user_id1 : user_ids) {
                    if (user_id1.equals(user_id)) continue;

                    boolean check = false;
                    if(tg.adj.get(user_id1)!=null){
                        for(ArrayList<Long> arc: tg.adj.get(user_id1)){
                            if (arc.get(1).equals(user_id)){
                                check=true;
                                break;
                            }
                        }
                    }

                    if(check) continue;
                    tg.addEdge(user_id,user_id1);
                }i++;
                }
            for(Long key : tg.adj.keySet()){
                for (ArrayList<Long> arc : tg.adj.get(key)){
                    this.addEdge(arc.get(0),arc.get(1));
                }
            }
        }



        //1)devo prendere i tweet di un arco temporale, per ogni arco temporale;
        //answer : prendo solo il primo per velocizzare

        //2)per ogni cluster devo cercare i tweet che contengono o t1 || t2 || t3 || .. || t_dimensionecluster //
        //answer: questo lo faccio con una hashmap (cluster,termini) per associare ad ogni cluster i termini e poi cerco su dataset trovato prima usando i termini nella hashmap

        //3)fare una clique tra gli utenti che compaiono nella ricerca,due utenti hanno un arco se se entrambi hanno fatto un post che contiene uno dei termini del cluster;
        //answer: creo grafo provisorio e poi aggiungo tutti i suoi archi a quello principale

        //problemi: ci mette troppo a creare archi nel punto 3.
 }
    public ArrayList<Tweet> get_Tweets(Directory cluster, ArrayList<String> terms) throws IOException, ParseException {
        //reader settings
        IndexReader indexReader = DirectoryReader.open(cluster);
        IndexSearcher is = new IndexSearcher(indexReader);
        Analyzer analyzer = new EnglishAnalyzer();
        QueryParser parser = new QueryParser("text", analyzer);
        StringBuilder query = new StringBuilder();
        int i=0;
        for(String term : terms){
            if(i==0){
                query.append(term);
            }else {
                query.append(" OR ").append(term);
            }
            i++;
        }

        ArrayList<Tweet> tweets= new ArrayList<>();
        Query q = parser.parse(query.toString());
        TopDocs top = is.search(q, 500); // perform a query and limit resultsnumber

        ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

        Document doc;
        Tweet tweet ;
        for (ScoreDoc entry : hits) {
            doc = is.doc(entry.doc);
            tweet = new Tweet(); // pulisco tutto;
            tweet.User_ID = doc.get("user_id");
            tweets.add(tweet);
        }
        return tweets;
    }

    public Directory getWindow(int part) throws IOException {

        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("dataset_tweets"));
        BufferedReader csvReader;
        String row;

        //reader settings
        IndexReader indexReader = DirectoryReader.open(dir);
        Analyzer analyzer = new StandardAnalyzer();

        Directory dir1= new ByteBuffersDirectory();
        IndexWriterConfig cfg= new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir1, cfg);

        Document doc;
        csvReader = new BufferedReader(new FileReader("temporal_analysis/time_windows_size_10/time_window_" + part + ".csv"));
        while ((row = csvReader.readLine()) != null) { //for each term
            if (row.split(",")[0].equals("Document ID") || row.split(",")[0].equals("DocumentID"))
                continue;       //la prima riga la salta

            doc = indexReader.document(Integer.parseInt(row.split(",")[0]));
            writer.addDocument(doc);
        }

        writer.commit();
        writer.close();
        return dir1;
    }
}




