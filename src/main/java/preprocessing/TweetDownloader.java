package preprocessing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import twitter4j.*;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

public class TweetDownloader {
    public File folder;
    public ArrayList<Status> tweets;

    public TweetDownloader(String dir) throws IOException, TwitterException, InterruptedException {
        this.folder = new File(dir);
        this.tweets = this.downloadTweets();
        //this.storeTweets();
    }

    public void downloadTweets(Long[] tweetIds, IndexWriter writer) throws TwitterException, InterruptedException {
        Twitter twitter = TwitterFactory.getSingleton();
        try {

            ResponseList<Status> test = twitter.lookup(tweetIds[0], tweetIds[1], tweetIds[2], tweetIds[3], tweetIds[4], tweetIds[5], tweetIds[6], tweetIds[7], tweetIds[8], tweetIds[9], tweetIds[10], tweetIds[11], tweetIds[12], tweetIds[13], tweetIds[14], tweetIds[15], tweetIds[16], tweetIds[17], tweetIds[18], tweetIds[19], tweetIds[20], tweetIds[21],
                    tweetIds[22],
                    tweetIds[23],
                    tweetIds[24],
                    tweetIds[25],
                    tweetIds[26],
                    tweetIds[27],
                    tweetIds[28],
                    tweetIds[29],
                    tweetIds[30],
                    tweetIds[31],
                    tweetIds[32],
                    tweetIds[33],
                    tweetIds[34],
                    tweetIds[35],
                    tweetIds[36],
                    tweetIds[37],
                    tweetIds[38], tweetIds[39],
                    tweetIds[40],
                    tweetIds[41],
                    tweetIds[42],
                    tweetIds[43],
                    tweetIds[44],
                    tweetIds[45],
                    tweetIds[46],
                    tweetIds[47],
                    tweetIds[48],
                    tweetIds[49],
                    tweetIds[50],
                    tweetIds[51],
                    tweetIds[52],
                    tweetIds[53],
                    tweetIds[54],
                    tweetIds[55],
                    tweetIds[56],
                    tweetIds[57],
                    tweetIds[58],
                    tweetIds[59],
                    tweetIds[60],
                    tweetIds[61],
                    tweetIds[62],
                    tweetIds[63],
                    tweetIds[64],
                    tweetIds[65],
                    tweetIds[66],
                    tweetIds[67],
                    tweetIds[68],
                    tweetIds[69],
                    tweetIds[70],
                    tweetIds[71],
                    tweetIds[72],
                    tweetIds[73],
                    tweetIds[74],
                    tweetIds[75],
                    tweetIds[76],
                    tweetIds[77],
                    tweetIds[78],
                    tweetIds[79],
                    tweetIds[80],
                    tweetIds[81],
                    tweetIds[82],
                    tweetIds[83],
                    tweetIds[84],
                    tweetIds[85],
                    tweetIds[86],
                    tweetIds[87],
                    tweetIds[88],
                    tweetIds[89],
                    tweetIds[90],
                    tweetIds[91],
                    tweetIds[92],
                    tweetIds[93],
                    tweetIds[94],
                    tweetIds[95],
                    tweetIds[96],
                    tweetIds[97],
                    tweetIds[98],
                    tweetIds[99]);
            test.forEach((tweet) ->{
                if (tweet != null) {
                    System.out.println(tweet.getId());
                    Document document = new Document();
                    document.add(new StringField("lang", tweet.getLang(), Field.Store.YES));
                    document.add(new StringField("text", tweet.getText(), Field.Store.YES));
                    document.add(new LongPoint("id", tweet.getId()));
                    document.add(new StringField("user", tweet.getUser().getName(), Field.Store.YES));
                    try {
                        writer.addDocument(document);
                        writer.commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });



        } catch (TwitterException e) {
            System.out.println("ERRORE!");
            TimeUnit.MINUTES.sleep(50);
        }

    }


    public ArrayList<Status> downloadTweets() throws IOException, TwitterException, InterruptedException {
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("tweets1"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, cfg);


        Twitter twitter = TwitterFactory.getSingleton();
        Random rand = new Random();
        tweets = new ArrayList<Status>();
        File[] listOfFiles = folder.listFiles();
        int i = 0;
        int j = 1;
        Long[] tweetsToDownload = new Long[100];
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    if (rand.nextInt(10) + 1 == 1) {
                        tweetsToDownload[i] = Long.valueOf(data);
                        i=i+1;
                        if(i == 100) {
                            System.out.println("Ho collezionato i tweet e ora li scarico");
                            this.downloadTweets(tweetsToDownload, writer);
                            i = 0;
                            j = j+1;
                        }
                        if(j == 900) {
                            System.out.println("Mi fermo per 500 secondi");
                            TimeUnit.SECONDS.sleep(500);
                            j = 0;
                        }
                    }
                }
            }
        }
        writer.close();

        return tweets;
    }

    public void storeTweet(Status tweet) throws IOException {
        String filename = "tweets";
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("tweets.txt", true)))) {
            out.println(tweet.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //compressGzipFile(filename + ".txt", filename + ".gzip");
    }

    public void storeTweets() throws IOException {
        /*this.tweets.forEach((tweet) -> {
            try {
                this.storeTweet(tweet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }); */
        //compressGzipFile(filename + ".txt", filename + ".gzip");
    }

    private static void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
