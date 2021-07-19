package exercises.ex1;

import exercises.ex1.DataSet;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Ex1Point2_4 {
    public static final Double PRECISION = 0.0;

    /* K-Means++ implementation, initializes K centroids from data */
    public static LinkedList<HashMap<String, Double>> kmeanspp(DataSet data, int K) {
        LinkedList<HashMap<String,Double>> centroids = new LinkedList<>();

        centroids.add(data.randomFromDataSet());

        for(int i=1; i<K; i++){
            centroids.add(data.calculateWeighedCentroid());
        }

        return centroids;
    }

    /* K-Means itself, it takes a dataset and a number K and adds class numbers
     * to records in the dataset */
    public static void kmeansToClusters() throws IOException {
        int K;
        int l = 0;
        for(int i=1;i<=8; i++) {
            K = (int) (Math.ceil(countFileLines("temporal_analysis/sax/terms_filtered_time_window_" + i + ".csv") / 20) - 1);
            System.out.println(K);
            DataSet data = new DataSet("temporal_analysis/sax/terms_filtered_time_window_" + i + ".csv");

            // Select K initial centroids
            LinkedList<HashMap<String,Double>> centroids = kmeanspp(data, K);

            // Initialize Sum of Squared Errors to max, we'll lower it at each iteration
            Double SSE = Double.MAX_VALUE;

            while (true) {
                System.out.println("ENTRATOOO");

                // Assign observations to centroids
                var records = data.getRecords();

                // For each record
                for(var record : records){
                    System.out.println("NEL FOR");
                    Double minDist = Double.MAX_VALUE;
                    // Find the centroid at a minimum distance from it and add the record to its cluster
                    for(int j = 0; j < centroids.size(); j++){
                        Double dist = DataSet.euclideanDistance(centroids.get(j), record.getRecord());
                        if(dist < minDist){
                            minDist = dist;
                            record.setClusterNo(j);
                        }
                    }
                }

                // Recompute centroids according to new cluster assignments
                centroids = data.recomputeCentroids(K);

                // Exit condition, SSE changed less than PRECISION parameter
                Double newSSE = data.calculateTotalSSE(centroids);
                if(SSE-newSSE <= PRECISION){
                    break;
                }
                SSE = newSSE;
                l=l+1;
            }
            data.createCsvOutput("temporal_analysis/clusters/distances/distances_time_window_" + i + ".csv");
        }
        clusterAssociationWithTerms();

    }

    public static void clusterAssociationWithTerms() throws IOException {
        BufferedReader csvReader, csvReader2;
        String[] data,data2;
        String row, row2;
        FileWriter csvWriter;
        int iteration = 0;
        for (int i = 1; i <= 8; i++) {
            csvReader = new BufferedReader(new FileReader("temporal_analysis/clusters/distances/distances_time_window_" + i + ".csv"));
            csvReader2 = new BufferedReader(new FileReader("temporal_analysis/sax/terms_filtered_time_window_" + i + ".csv"));
            csvWriter = new FileWriter("temporal_analysis/clusters/terms/cluster_of_terms_time_window_" + i + ".csv");
            while ((row = csvReader.readLine()) != null) { //for each term
                if(iteration == 0) {
                    iteration++;
                    csvWriter.append("Cluster ID, Term\n");
                    continue;
                }
                row2 = csvReader2.readLine();
                data = row.split(",");
                data2=row2.split(",");
                csvWriter.append(data[2]);
                csvWriter.append(",");
                csvWriter.append(data2[0]);
                csvWriter.append("\n");

                /*for (SAXRecord saxRecord : str) {
                    System.out.println(saxRecord.toString());
                }*/
            }
            csvWriter.close();
            // print the output
            System.out.println("NUOVA WINDOW");
        }
    }

    public static int countFileLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean endsWithoutNewLine = false;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if(endsWithoutNewLine) {
                ++count;
            }
            return count;
        } finally {
            is.close();
        }
    }
}
