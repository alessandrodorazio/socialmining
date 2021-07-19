package exercises.ex1;

import java.io.*;
import java.util.*;

public class DataSet {

    static class Record{
        HashMap<String, Double> record;
        Integer clusterNo;

        public Record(HashMap<String, Double> record){
            this.record = record;
        }

        public void setClusterNo(Integer clusterNo) {
            this.clusterNo = clusterNo;
        }

        public HashMap<String, Double> getRecord() {
            return record;
        }
    }

    private final LinkedList<String> attrNames = new LinkedList<>();
    private final LinkedList<Record> records = new LinkedList<>();
    private final LinkedList<Integer> indicesOfCentroids = new LinkedList<>();
    private final HashMap<String, Double> minimums = new HashMap<>();
    private final HashMap<String, Double> maximums = new HashMap<>();
    private static final Random random = new Random();

    public DataSet(String csvFileName) throws IOException {

        String row;
        try(BufferedReader csvReader = new BufferedReader(new FileReader(csvFileName))) {
            if((row = csvReader.readLine()) != null){
                String[] data = row.split(",");
                Collections.addAll(attrNames, data);
            }

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");

                HashMap<String, Double> record = new HashMap<>();

                if(attrNames.size() == data.length) {
                    for (int i = 0; i < attrNames.size(); i++) {
                        String name = attrNames.get(i);
                        double val = EditDistanceRecursive.calculate(name,data[i]);
                        record.put(name, val);
                        updateMin(name, val);
                        updateMax(name, val);
                    }
                } else{
                    throw new IOException("Incorrectly formatted file.");
                }

                records.add(new Record(record));
            }

        }
    }

    public void createCsvOutput(String outputFileName){
        System.out.println("CSV OUTPUT");

        try(BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFileName))) {
            for(int i=0; i<attrNames.size(); i++){
                csvWriter.write(attrNames.get(i));
                csvWriter.write(",");
            }

            csvWriter.write("ClusterId");
            csvWriter.write("\n");

            for(var record : records){
                for(int i=0; i<attrNames.size(); i++){
                    csvWriter.write(String.valueOf(record.getRecord().get(attrNames.get(i))));
                    csvWriter.write(",");
                }
                csvWriter.write(String.valueOf(record.clusterNo));
                csvWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMin(String name, Double val){
        if(minimums.containsKey(name)){
            if(val < minimums.get(name)){
                minimums.put(name, val);
            }
        } else{
            minimums.put(name, val);
        }
    }

    private void updateMax(String name, Double val){
        if(maximums.containsKey(name)){
            if(val > maximums.get(name)){
                maximums.put(name, val);
            }
        } else{
            maximums.put(name, val);
        }
    }

    public Double meanOfAttr(String attrName, LinkedList<Integer> indices){
        Double sum = 0.0;
        for(int i : indices){
            if(i<records.size()){
                sum += records.get(i).getRecord().get(attrName);
            }
        }
        return sum / indices.size();
    }

    public HashMap<String, Double> calculateCentroid(int clusterNo){
        HashMap<String, Double> centroid = new HashMap<>();

        LinkedList<Integer> recsInCluster = new LinkedList<>();
        for(int i=0; i<records.size(); i++){
            var record = records.get(i);
            if(record.clusterNo == clusterNo){
                recsInCluster.add(i);
            }
        }

        for(String name : attrNames){
            centroid.put(name, meanOfAttr(name, recsInCluster));
        }
        return centroid;
    }

    public LinkedList<HashMap<String,Double>> recomputeCentroids(int K){
        LinkedList<HashMap<String,Double>> centroids = new LinkedList<>();
        for(int i=0; i<K; i++){
            centroids.add(calculateCentroid(i));
        }
        return centroids;
    }

    public void removeAttr(String attrName){
        if(attrNames.contains(attrName)){
            attrNames.remove(attrName);

            for(var record : records){
                record.getRecord().remove(attrName);
            }

            minimums.remove(attrName);

            maximums.remove(attrName);
        }

    }

    public HashMap<String, Double> randomDataPoint(){
        HashMap<String, Double> res = new HashMap<>();

        for(String name : attrNames){
            Double min = minimums.get(name);
            Double max = maximums.get(name);
            res.put(name, min + (max-min) * random.nextDouble());
        }

        return res;
    }

    public HashMap<String, Double> randomFromDataSet(){
        int index = random.nextInt(records.size());
        return records.get(index).getRecord();
    }

    public static Double euclideanDistance(HashMap<String, Double> a, HashMap<String, Double> b){
        if(!a.keySet().equals(b.keySet())){
            return Double.POSITIVE_INFINITY;
        }

        double sum = 0.0;

        for(String attrName : a.keySet()){
            sum += Math.pow(a.get(attrName) - b.get(attrName), 2);
        }

        return Math.sqrt(sum);
    }

    public Double calculateClusterSSE(HashMap<String, Double> centroid, int clusterNo){
        double SSE = 0.0;
        for(int i=0; i<records.size(); i++){
            if(records.get(i).clusterNo == clusterNo){
                SSE += Math.pow(euclideanDistance(centroid, records.get(i).getRecord()), 2);
            }
        }
        return SSE;
    }

    public Double calculateTotalSSE(LinkedList<HashMap<String,Double>> centroids){
        Double SSE = 0.0;
        for(int i=0; i<centroids.size(); i++) {
            SSE += calculateClusterSSE(centroids.get(i), i);
        }
        return SSE;
    }

    public HashMap<String,Double> calculateWeighedCentroid(){
        double sum = 0.0;

        for(int i=0; i<records.size(); i++){
            if(!indicesOfCentroids.contains(i)){
                double minDist = Double.MAX_VALUE;
                for(int ind : indicesOfCentroids){
                    double dist = euclideanDistance(records.get(i).getRecord(), records.get(ind).getRecord());
                    if(dist<minDist)
                        minDist = dist;
                }
                if(indicesOfCentroids.isEmpty())
                    sum = 0.0;
                sum += minDist;
            }
        }

        double threshold = sum * random.nextDouble();

        for(int i=0; i<records.size(); i++){
            if(!indicesOfCentroids.contains(i)){
                double minDist = Double.MAX_VALUE;
                for(int ind : indicesOfCentroids){
                    double dist = euclideanDistance(records.get(i).getRecord(), records.get(ind).getRecord());
                    if(dist<minDist)
                        minDist = dist;
                }
                sum += minDist;

                if(sum > threshold){
                    indicesOfCentroids.add(i);
                    return records.get(i).getRecord();
                }
            }
        }

        return new HashMap<>();
    }

    public LinkedList<String> getAttrNames() {
        return attrNames;
    }

    public LinkedList<Record> getRecords() {
        return records;
    }

    public Double getMin(String attrName){
        return minimums.get(attrName);
    }

    public Double getMax(String attrName){
        return maximums.get(attrName);
    }
}

/*Levenshtein distance */
 class EditDistanceRecursive {

    static int calculate(String x, String y) {
        System.out.println(x + "CIAO" + y);
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1]
                                    + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }
}

//istanziare var di correlazione HashMap<<String,String>, Integer>
//prendi tutti i termini della time window
//confronta i termini presi con tutti i termini della time window in ogni documento
//se due termini sono nello stesso documento, allora fai +1