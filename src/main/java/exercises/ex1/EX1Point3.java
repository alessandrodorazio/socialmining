package exercises.ex1;

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

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;

public class EX1Point3 {

    public ArrayList<HashMap<Integer, ArrayList<String>>> cluster_terms_for_TimeWindow() throws IOException {
        BufferedReader csvReader;
        ArrayList<HashMap<Integer, ArrayList<String>>> rows = new ArrayList<>(); // rows for of cluster each time window
        String row;

        for (int i = 1; i <= 8; i++) {

            HashMap<Integer, ArrayList<String>> terms_in_cluster = new HashMap<>();
            rows.add(terms_in_cluster);

            csvReader = new BufferedReader(new FileReader("temporal_analysis/clusters/terms/cluster_of_terms_time_window_" + i + ".csv"));
            while ((row = csvReader.readLine()) != null) { //for each term
                if (row.split(",")[0].equals("ClusterId") || row.split(",")[0].equals("Cluster ID"))
                    continue;       //la prima riga la salta

                int cluster = Integer.parseInt(row.split(",")[0]);
                String term = row.split(",")[1];

                if (!terms_in_cluster.containsKey(cluster)) terms_in_cluster.put(cluster, new ArrayList<>());
                terms_in_cluster.get(cluster).add(term);
            }
        }

        return rows;
    }

    public ArrayList<CoOccurrenceGaph> cooccurrence_graphs(ArrayList<HashMap<Integer, ArrayList<String>>> cluster_terms_for_TimeWindow) throws IOException, ParseException {
        ArrayList<CoOccurrenceGaph> cooccurrence_graphs = new ArrayList<>();
        //lista di grafi, String identifica ID nodo e poi abbiamo una lista di archi con un peso dove arco è una tripla from,to, weight
        int current_graph = 0;
        int current_time_window = 1;
        for (HashMap<Integer, ArrayList<String>> time_window : cluster_terms_for_TimeWindow) {

            System.out.println("Current graph:" + current_graph);
            cooccurrence_graphs.add(new CoOccurrenceGaph());
            Directory current_cluster=getcluster(current_time_window);

            for (Integer cluster : time_window.keySet()) {
                System.out.println(cluster);
                System.out.println(time_window.keySet());
                int j=0;
                for (String term : time_window.get(cluster)) {
                    System.out.println((time_window.get(cluster)).size()-j);
                    for (String term2 : time_window.get(cluster)) {

                        if (term.equals(term2)) continue;


                        boolean already_seen = false;
                        if(cooccurrence_graphs.get(current_graph).adj.get(term2)!=null){
                        for (ArrayList<String> terms_already_seen : cooccurrence_graphs.get(current_graph).adj.get(term2)){
                            if (terms_already_seen.contains(term)){
                                already_seen=true;
                                break;
                            }
                        }}
                        if (already_seen) continue;


                        int occur = get_num_of_occur(current_cluster, term, term2);
                        if (occur==0) continue;


                        for (int i = 1; i<= occur; i++) {
                            cooccurrence_graphs.get(current_graph).addEdge(term, term2);
                        }
                    }
                    j++;
                }
            }
            System.out.println(cooccurrence_graphs.get(current_graph));
            System.out.println("ok done, next!");

            current_graph++;
        }
        return cooccurrence_graphs;
    }


    public Directory getcluster(int part) throws IOException {

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

        System.out.println("fatto");
        writer.commit();
        writer.close();
        return dir1;
    }

    public int get_num_of_occur(Directory cluster, String term, String term2) throws IOException, ParseException {
        //reader settings
        IndexReader indexReader = DirectoryReader.open(cluster);
        IndexSearcher is = new IndexSearcher(indexReader);
        Analyzer analyzer = new EnglishAnalyzer();
        QueryParser parser = new QueryParser("text", analyzer);
        String query = term + " AND " + term2;
        Query q = parser.parse(query);
        TopDocs top = is.search(q, 10000); // perform a query and limit resultsnumber
        ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)
        return hits.length;
    }
    public static void main(String[] args) throws IOException, ParseException {
        EX1Point3 prova = new EX1Point3();
        System.out.println(prova.cooccurrence_graphs(prova.cluster_terms_for_TimeWindow()));
    }

}
//devo prendere i tweet di un arco temporale, per ogni arco temporale;
//per ogni cluster
//devo cercare i tweet che contengono o t1 || t2 || t3 || .. || t_dimensionecluster
// fare una clique tra gli utenti che compaiono nella ricerca.
//due utenti hanno un arco se se entrambi hanno fatto un post che contiene uno dei termini del cluster;