package exercises.ex1;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Ex1Point4 {
    public static void topClustersOfEachTimeWindow() throws IOException {
        int n = 5;
        BufferedReader csvReader;
        FileWriter csvWriter;
        String row, rowTs;
        String[] data;
        Integer[] actualTs;
        int iteration = 0;
        for (int i = 1; i <= 8; i++) {
            iteration = 0;
            csvReader = new BufferedReader(new FileReader("temporal_analysis/clusters/terms/cluster_of_terms_time_window_" + i + ".csv"));
            csvWriter = new FileWriter("temporal_analysis/time_series_top_clusters/time_window_" + i + ".csv");
            while((row = csvReader.readLine()) != null) {
                if(iteration == 0) {
                    iteration++;
                    continue;
                }
                data = row.split(",");
                if(!data[1].equals("Term")) { //bug on first time window
                    if(Integer.parseInt(data[0]) <= n) {
                        actualTs = getTimeSeriesOfTerm(data[1], i);
                        csvWriter.append(data[1]);
                        for(int j=0; j<10; j++) {
                            csvWriter.append(",");
                            csvWriter.append(String.valueOf(actualTs[j]));
                        }
                        csvWriter.append("\n");
                    }
                }


            }
            csvReader.close();
            csvWriter.close();
        }
    }

    public static Integer[] getTimeSeriesOfTerm(String term, int timeWindow) throws IOException {
        Integer[] result = new Integer[10];
        BufferedReader csvReader;
        String row;
        String[] data;
        boolean found = false;
        csvReader = new BufferedReader(new FileReader("temporal_analysis/time_series/ts_time_window_" + timeWindow + ".csv"));
        while((row = csvReader.readLine()) != null) {
            if(found) {
                continue;
            }
            data = row.split(",");
            if(data[0].equals(term)) {
                for(int i=1; i<=10; i++) {
                    result[i-1] = Integer.parseInt(data[i]);
                }
                found = true;
            }

        }
        csvReader.close();
        return result;
    }

    // order the file by the time series (only first day is compared)
    public static void orderByOccurences() throws IOException {
        FileWriter csvWriter;
        for(int i =1; i<=8;i++) {
            Path filePath = new File("temporal_analysis/time_series_top_clusters/time_window_" + i + ".csv").toPath();
            Charset charset = Charset.defaultCharset();
            List<String> stringList = Files.readAllLines(filePath, charset);
            String[] stringArray = stringList.toArray(new String[]{});


            Arrays.sort(stringArray, new Comparator<String>(){
                public int compare(String first, String second) {
                    return Integer.valueOf(second.split(",")[1]).compareTo(Integer.valueOf(first.split(",")[1]));
                }
            });
            csvWriter = new FileWriter("temporal_analysis/time_series_top_clusters/with_ordered_terms/time_window_" + i + ".csv");

            for (String newstr: stringArray){
                csvWriter.append(newstr);
                csvWriter.append("\n");
            }
            csvWriter.close();
        }

    }
}
