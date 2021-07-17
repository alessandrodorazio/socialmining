package exercises.ex1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.*;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Ex1Point2_2 {

    public static void timeSeriesForEachTermInTimeWindow() throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("dataset_tweets"));
        IndexReader indexReader = DirectoryReader.open(dir);
        Analyzer analyzer = new EnglishAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        IndexSearcher is = new IndexSearcher(indexReader);

        BufferedReader csvReader;
        HashMap<String, Integer[]> ts;
        int iteration = 0;
        String row;
        String term;
        Calendar cal = Calendar.getInstance();
        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse("04/01/2021");
        FileWriter csvWriter;
        long count = 0;

        for (int i = 1; i <= 8; i++) { //for each time window
            System.out.println("TIME WINDOW" + i);
            ts = new HashMap<String, Integer[]>();
            csvReader = new BufferedReader(new FileReader("temporal_analysis/tf_idf/tf_idf_time_window_" + i + ".csv"));
            while ((row = csvReader.readLine()) != null) { //for each term
                if (iteration == 0) {
                    iteration++;
                    continue;
                }
                System.out.println("TERMINE" + count++);

                String[] data = row.split(",");
                term = data[0];
                ts.put(term, numOccurrencesTermInDocument(term, indexReader, i, startDate));
                System.out.println("FATTO");
            }
            csvWriter = new FileWriter("temporal_analysis/time_series/ts_time_window_" + i + ".csv");


            for (Map.Entry<String, Integer[]> entry : ts.entrySet()) {
                csvWriter.append(entry.getKey());
                for(int timeseries: entry.getValue()) {
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(timeseries));
                }
                csvWriter.append("\n");
            }
            csvWriter.close();
            cal.setTime(startDate);
            cal.add(Calendar.DATE, 5);
            startDate = cal.getTime();
        }
        // prendi tutti i termini nella time window
        // per ogni documento di quel giorno
        // prendi num occorrenze
        // result Hashmap<String, Double[]>
    }

    public static Integer[] numOccurrencesTermInDocument(String termToSearch, IndexReader indexReader, int timeWindow, Date startDate) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
        int result = 0;
        BufferedReader csvReader;
        HashMap<String, Integer> ts = new HashMap<String, Integer>();
        int iteration = 0;
        String row;
        Document doc;
        Analyzer analyzer = new EnglishAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        IndexSearcher is = new IndexSearcher(indexReader);
        QueryParser parser = new QueryParser("text", analyzer);
        Query q = parser.parse(termToSearch);
        TopDocs top = is.search(q, 1000000); // perform a query and limit resultsnumber
        ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, 10);
        Date endDate = cal.getTime();

        System.out.println("START DATE" + startDate.toString());
        System.out.println("END DATE " + endDate.toString());
        SimpleDateFormat formatter = new SimpleDateFormat("MMdd");

        for(ScoreDoc hit: hits) {
            doc = indexReader.document(hit.doc);
            Date createdAt = new SimpleDateFormat("MMM dd yyyy HH:mm:ss").parse(doc.get("created_at").substring(4, 10) + " " + doc.get("created_at").substring(26, 30) + " " + doc.get("created_at").substring(11, 19));
            if(createdAt.compareTo(startDate) >= 0 && createdAt.compareTo(endDate) <= 0) {
                Terms terms = indexReader.getTermVector(hit.doc, "text");
                if (terms != null && terms.size() > 0) {
                    // access the terms for this field
                    TermsEnum termsEnum = terms.iterator();
                    BytesRef term = null;
                    // explore the terms for this field

                    while ((term = termsEnum.next()) != null) {
                        // enumerate through documents, in this case only one
                        PostingsEnum docsEnum = termsEnum.postings(null);
                        int docIdEnum;
                        while ((docIdEnum = docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                            // get the term frequency in the document
                            String utf8Term = term.utf8ToString().replaceAll("[^A-Za-z0-9]", "");
                            if (utf8Term.equals(termToSearch)) {
                                if (ts.containsKey(formatter.format(createdAt).toString())) {
                                    ts.put(formatter.format(createdAt).toString(), ts.get(formatter.format(createdAt).toString()) + docsEnum.freq());
                                } else {
                                    ts.put(formatter.format(createdAt).toString(), docsEnum.freq());
                                }
                            }

                        }
                    }
                }
            }

        }

        //document date, get day month
        //Date createdAt = new SimpleDateFormat("MMM dd yyyy HH:mm:ss").parse(doc.get("created_at").substring(4, 10) + " " + doc.get("created_at").substring(26, 30) + " " + doc.get("created_at").substring(11, 19));
        Date dateToSetZeroValues = startDate;
        Integer[] timeSeries = new Integer[10];
        for(int i=0; i<10; i++) {
            timeSeries[i] = ts.getOrDefault(formatter.format(startDate), 0);
            cal.setTime(dateToSetZeroValues);
            cal.add(Calendar.DATE, 1);
            startDate = cal.getTime();
        }

        return timeSeries;
    }
}
