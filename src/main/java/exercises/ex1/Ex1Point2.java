package exercises.ex1;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.function.valuesource.IDFValueSource;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefIterator;
import org.apache.lucene.util.Version;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.text.SimpleDateFormat;
import java.util.*;

public class Ex1Point2 {
    public static void getTermFrequencyInCollection() throws IOException, ParseException, java.text.ParseException {
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("dataset_tweets"));
        IndexReader indexReader = DirectoryReader.open(dir);
        Analyzer analyzer = new EnglishAnalyzer();
        IndexSearcher is = new IndexSearcher(indexReader);

        documentsInTimeWindowsToCsv(is, analyzer);
        documentsInTimeWindowsToCsv2(is, analyzer);
        getAllTermsForEachTimeWindow(is, indexReader);
        getTfForEachTerm();
        getIdfForEachTerm();
        calculateTfIdfForEachTerm();
    }

    public static void documentsInTimeWindowsToCsv(IndexSearcher is, Analyzer analyzer) throws ParseException, IOException, java.text.ParseException {
        Date startTimeWindow = new SimpleDateFormat("dd/MM/yyyy").parse("04/01/2021");

        Calendar cal = Calendar.getInstance();
        cal.setTime(startTimeWindow);
        cal.add(Calendar.DATE, 10);
        Date endTimeWindow = cal.getTime();

        FileWriter csvWriter;
        Date finalDate = new SimpleDateFormat("dd/MM/yyyy").parse("12/02/2021");

        for (int i = 1; startTimeWindow.compareTo(finalDate) <= 0; i++) {
            csvWriter = new FileWriter("temporal_analysis/time_windows_size_10/time_window_" + i + ".csv");
            csvWriter.append("Document ID");
            csvWriter.append(",");
            csvWriter.append("Created at");
            csvWriter.append("\n");

            System.out.println("ITERAZIONE " + i + ": " + startTimeWindow.toString());
            System.out.println("ITERAZIONE " + i + ": " + endTimeWindow.toString());
            QueryParser parser = new QueryParser("text", analyzer);
            parser.setAllowLeadingWildcard(true);

            Query q = parser.parse("*:*");
            TopDocs top = is.search(q, 3000000); // perform a query and limit resultsnumber
            ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

            Document doc = null;
            for (ScoreDoc entry : hits) {
                // load document in memory (only the stored filed are available)
                doc = is.doc(entry.doc); /* the same as ir.document(entry.doc); */
                Date createdAt = new SimpleDateFormat("MMM dd yyyy HH:mm:ss").parse(doc.get("created_at").substring(4, 10) + " " + doc.get("created_at").substring(26, 30) + " " + doc.get("created_at").substring(11, 19));
                if (createdAt.compareTo(startTimeWindow) >= 0 && createdAt.compareTo(endTimeWindow) <= 0) {
                    System.out.println(createdAt.toString());
                    csvWriter.append(String.valueOf(entry.doc)).append(", ").append(createdAt.toString()); //DOCUMENTID, CREATED AT DATE OF THE DOCUMENT
                    csvWriter.append("\n");
                }

                //System.out.println(createdAt.toString());
                //System.out.println("created_at: "+ doc.get("created_at"));
                //System.out.println("text: "+doc.get("text"));
            }

            csvWriter.flush();
            csvWriter.close();

            cal.setTime(startTimeWindow);
            cal.add(Calendar.DATE, 5);
            startTimeWindow = cal.getTime();
            cal.add(Calendar.DATE, 10);
            endTimeWindow = cal.getTime();
        }
    }

    public static void documentsInTimeWindowsToCsv2(IndexSearcher is, Analyzer analyzer) throws ParseException, IOException, java.text.ParseException {
        Date startTimeWindow = new SimpleDateFormat("dd/MM/yyyy").parse("04/01/2021");

        Calendar cal = Calendar.getInstance();
        cal.setTime(startTimeWindow);
        cal.add(Calendar.DATE, 5);
        Date endTimeWindow = cal.getTime();

        FileWriter csvWriter;
        Date finalDate = new SimpleDateFormat("dd/MM/yyyy").parse("12/02/2021");

        for (int i = 1; startTimeWindow.compareTo(finalDate) <= 0; i++) {
            csvWriter = new FileWriter("temporal_analysis/time_windows_size_5/time_window_" + i + ".csv");
            csvWriter.append("Document ID");
            csvWriter.append(",");
            csvWriter.append("Created at");
            csvWriter.append("\n");

            System.out.println("ITERAZIONE " + i + ": " + startTimeWindow.toString());
            System.out.println("ITERAZIONE " + i + ": " + endTimeWindow.toString());
            QueryParser parser = new QueryParser("text", analyzer);
            parser.setAllowLeadingWildcard(true);

            Query q = parser.parse("*:*");
            TopDocs top = is.search(q, 3000000); // perform a query and limit resultsnumber
            ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

            Document doc = null;
            for (ScoreDoc entry : hits) {
                // load document in memory (only the stored filed are available)
                doc = is.doc(entry.doc); /* the same as ir.document(entry.doc); */
                Date createdAt = new SimpleDateFormat("MMM dd yyyy HH:mm:ss").parse(doc.get("created_at").substring(4, 10) + " " + doc.get("created_at").substring(26, 30) + " " + doc.get("created_at").substring(11, 19));
                if (createdAt.compareTo(startTimeWindow) >= 0 && createdAt.compareTo(endTimeWindow) <= 0) {
                    System.out.println(createdAt.toString());
                    csvWriter.append(String.valueOf(entry.doc)).append(", ").append(createdAt.toString()); //DOCUMENTID, CREATED AT DATE OF THE DOCUMENT
                    csvWriter.append("\n");
                }

                //System.out.println(createdAt.toString());
                //System.out.println("created_at: "+ doc.get("created_at"));
                //System.out.println("text: "+doc.get("text"));
            }

            csvWriter.flush();
            csvWriter.close();

            cal.setTime(startTimeWindow);
            cal.add(Calendar.HOUR, 60);
            startTimeWindow = cal.getTime();
            cal.add(Calendar.DATE, 5);
            endTimeWindow = cal.getTime();
        }
    }


    public static void getAllTermsForEachTimeWindow(IndexSearcher is, IndexReader indexReader) throws IOException {
        HashMap<String, Integer> occurrencesInEntireDataset = new HashMap<String, Integer>();
        HashMap<String, Integer> documentsWithTermInEntireDataset = new HashMap<String, Integer>();

        FileWriter csvWriter;
        BufferedReader csvReader;
        String row;
        Document doc;
        HashMap<String, Integer> occurencesInTimeWindow;
        HashMap<String, Integer> documentsWithTermInTimeWindow = new HashMap<String, Integer>();

        for (int i = 1; i <= 8; i++) {
            csvReader = new BufferedReader(new FileReader("temporal_analysis/time_windows_size_10/time_window_" + i + ".csv"));
            occurencesInTimeWindow = new HashMap<String, Integer>();
            documentsWithTermInTimeWindow = new HashMap<String, Integer>();
            int iteration = 0;
            while ((row = csvReader.readLine()) != null) {
                if (iteration == 0) {
                    iteration++;
                    continue;
                }
                String[] data = row.split(",");

                Terms terms = indexReader.getTermVector(Integer.parseInt(data[0]), "text");
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
                            if (utf8Term.length() > 2) {
                                if (occurrencesInEntireDataset.containsKey(utf8Term)) {
                                    occurrencesInEntireDataset.put(utf8Term, occurrencesInEntireDataset.get(utf8Term) + docsEnum.freq());
                                    documentsWithTermInEntireDataset.put(utf8Term, documentsWithTermInEntireDataset.get(utf8Term) + 1);
                                } else {
                                    occurrencesInEntireDataset.put(utf8Term, docsEnum.freq());
                                    documentsWithTermInEntireDataset.put(utf8Term, 1);
                                }

                                if (occurencesInTimeWindow.containsKey(utf8Term)) {
                                    occurencesInTimeWindow.put(utf8Term, occurencesInTimeWindow.get(utf8Term) + docsEnum.freq());
                                    documentsWithTermInTimeWindow.put(utf8Term, documentsWithTermInTimeWindow.get(utf8Term) + 1);
                                } else {
                                    occurencesInTimeWindow.put(utf8Term, docsEnum.freq());
                                    documentsWithTermInTimeWindow.put(utf8Term, 1);
                                }
                            }

                        }
                    }
                }
            }
            csvReader.close();
            csvWriter = new FileWriter("temporal_analysis/terms_for_each_window_size_10/terms_for_time_window_" + i + ".csv");

            csvWriter.append("Term");
            csvWriter.append(",");
            csvWriter.append("Occurences");
            csvWriter.append(",");
            csvWriter.append("NumDocs");
            csvWriter.append("\n");

            for (Map.Entry<String, Integer> entry : occurencesInTimeWindow.entrySet()) {
                csvWriter.append(entry.getKey());
                csvWriter.append(",");
                csvWriter.append(entry.getValue().toString());
                csvWriter.append(",");
                csvWriter.append(documentsWithTermInTimeWindow.get(entry.getKey()).toString());
                csvWriter.append("\n");
            }
            csvWriter.close();
        }


        csvWriter = new FileWriter("temporal_analysis/all_terms_and_occurences.csv");

        csvWriter.append("Term");
        csvWriter.append(",");
        csvWriter.append("Occurences");
        csvWriter.append(",");
        csvWriter.append("NumDocs");
        csvWriter.append("\n");

        for (Map.Entry<String, Integer> entry : occurrencesInEntireDataset.entrySet()) {
            csvWriter.append(entry.getKey());
            csvWriter.append(",");
            csvWriter.append(entry.getValue().toString());
            csvWriter.append(",");
            csvWriter.append(documentsWithTermInEntireDataset.get(entry.getKey()).toString());
            csvWriter.append("\n");
        }
        csvWriter.close();
    }

    public static int getTotalWords() throws IOException {
        int totalWords = 0;
        BufferedReader csvReader;
        String row;
        int iteration = 0;
        for (int i = 1; i <= 8; i++) {
            csvReader = new BufferedReader(new FileReader("temporal_analysis/time_windows_size_10/time_window_" + i + ".csv"));
            while ((row = csvReader.readLine()) != null) {
                if (iteration == 0) {
                    iteration++;
                    continue;
                }
                String[] data = row.split(",");
                totalWords += Integer.parseInt(data[1]);
            }
            csvReader.close();
            iteration = 0;
        }
        return totalWords;
    }

    public static void getTfForEachTerm() throws IOException {
        HashMap<String, Integer> occurrences;

        int totalWordsTimeWindow = 0;

        BufferedReader csvReader;
        String row;
        int iteration = 0;
        occurrences = new HashMap<String, Integer>();
        for (int i = 1; i <= 8; i++) {
            csvReader = new BufferedReader(new FileReader("temporal_analysis/terms_for_each_window_size_10/terms_for_time_window_" + i + ".csv"));
            while ((row = csvReader.readLine()) != null) {
                if (iteration == 0) {
                    iteration++;
                    continue;
                }
                String[] data = row.split(",");
                totalWordsTimeWindow = totalWordsTimeWindow + Integer.parseInt(data[1]);
                if (occurrences.containsKey(data[0])) {
                    occurrences.put(data[0], occurrences.get(data[0]) + Integer.parseInt(data[1]));
                } else {
                    occurrences.put(data[0], Integer.parseInt(data[1])); //data[1] contains the number of occurences in the time window of the term data[0]
                }

            }
            csvReader.close();

            //HashMap<String, Integer> tf = new HashMap<String, Integer>();
            FileWriter csvWriter = new FileWriter("temporal_analysis/tf_idf/tf/tf_time_window_" + i + ".csv");
            csvWriter.append("Term");
            csvWriter.append(",");
            csvWriter.append("TF");
            csvWriter.append("\n");

            for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
                System.out.println(entry.getValue());
                System.out.println(totalWordsTimeWindow);
                //tf.put(entry.getKey(), entry.getValue()/totalWordsTimeWindow);
                csvWriter.append(entry.getKey());
                csvWriter.append(",");
                csvWriter.append(String.valueOf((double) entry.getValue() / totalWordsTimeWindow));
                csvWriter.append("\n");

            }

            csvWriter.close();
            iteration = 0;

            occurrences = new HashMap<String, Integer>();
        }


        //TF = quante volte c'è il termine nella time window / il numero di parole nella time window
    }

    public static void getIdfForEachTerm() throws IOException {

        HashMap<String, Integer> occurrences;
        int numDocs = 0;

        BufferedReader csvReader;
        String row;
        int iteration = 0;
        occurrences = new HashMap<String, Integer>();
        csvReader = new BufferedReader(new FileReader("temporal_analysis/all_terms_and_occurences.csv"));
        while ((row = csvReader.readLine()) != null) {
            numDocs++;
            if (iteration == 0) {
                iteration++;
                continue;
            }
            String[] data = row.split(",");
            occurrences.put(data[0], Integer.parseInt(data[2]));


        }
        csvReader.close();

        //HashMap<String, Integer> tf = new HashMap<String, Integer>();

        iteration = 0;


        FileWriter csvWriter = new FileWriter("temporal_analysis/tf_idf/idf/total_idf.csv");
        csvWriter.append("Term");
        csvWriter.append(",");
        csvWriter.append("IDF");
        csvWriter.append("\n");
        for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            //tf.put(entry.getKey(), entry.getValue()/totalWordsTimeWindow);
            csvWriter.append(entry.getKey());
            csvWriter.append(",");
            csvWriter.append(String.valueOf(Math.log((double) numDocs / entry.getValue())));
            csvWriter.append("\n");

        }

        csvWriter.close();
        /*HashMap<String, Integer> idf = new HashMap<String, Integer>();

        for(Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            idf.put(entry.getKey(), entry.getValue()/totalWords);
        } */
        //IDF = log numero documenti / numero documenti che contengono il termine
    }

    public static void calculateTfIdfForEachTerm() throws IOException {
        HashMap<String, Float> idfs;
        idfs = new HashMap<String, Float>();

        BufferedReader csvReader;
        String row;
        int iteration = 0;
        csvReader = new BufferedReader(new FileReader("temporal_analysis/tf_idf/idf/total_idf.csv"));
        while ((row = csvReader.readLine()) != null) {
            if (iteration == 0) {
                iteration++;
                continue;
            }
            String[] data = row.split(",");
            idfs.put(data[0], Float.parseFloat(data[1]));
        }


        HashMap<String, Float> tf;
        Float tfIdfTemp;
        iteration = 0;
        HashMap<String, Float> tfIdf = new HashMap<String, Float>();
        HashMap<String, Float> tfIdfToSave;
        for (int i = 1; i <= 8; i++) {
            tfIdfToSave = new HashMap<String, Float>();
            csvReader = new BufferedReader(new FileReader("temporal_analysis/tf_idf/tf/tf_time_window_" + i + ".csv"));

            while ((row = csvReader.readLine()) != null) {
                if (iteration == 0) {
                    iteration++;
                    continue;
                }
                String[] data = row.split(",");
                tfIdf.put(data[0], Float.parseFloat(data[1]) * idfs.get(data[0]));

            }
            csvReader.close();



            HashMap<String, Float> sorted = tfIdf
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));



            FileWriter csvWriter = new FileWriter("temporal_analysis/tf_idf/tf_idf_time_window_" + i + ".csv");
            csvWriter.append("Term");
            csvWriter.append(",");
            csvWriter.append("TF-IDF");
            csvWriter.append("\n");
            int k = 0;
            for (Map.Entry<String, Float> entry : sorted.entrySet()) {
                if(k<5000) {
                    csvWriter.append(entry.getKey());
                    csvWriter.append(",");
                    csvWriter.append(entry.getValue().toString());
                    csvWriter.append("\n");
                }
                System.out.println(entry.getValue());
                k++;
            }

            csvWriter.close();

            //HashMap<String, Integer> tf = new HashMap<String, Integer>();

            iteration = 0;


        }
    }


}
