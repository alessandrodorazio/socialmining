package preprocessing;

import twitter4j.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.GZIPOutputStream;

public class TweetDownloader {
    public File folder;
    public ArrayList<Status> tweets;

    public TweetDownloader(String dir) throws IOException, TwitterException {
        this.folder = new File(dir);
        this.tweets = this.downloadTweets();
        this.storeTweets();
    }

    public Status downloadTweet(String tweetId) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        try {
            Status status = twitter.showStatus(Long.parseLong(tweetId));
            if (status != null) {
                return status;
            } else {
                return null;
            }

        } catch (TwitterException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    public ArrayList<Status> downloadTweets() throws IOException, TwitterException {
        Random rand = new Random();
        tweets = new ArrayList<Status>();
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    if (rand.nextInt(10) + 1 == 1) {
                        Status tweet = this.downloadTweet(data);
                        if (tweet != null) {
                            tweets.add(tweet);
                            if (tweet.getLang().equals("en")) {
                                System.out.println("Il tweet " + data + " è in lingua inglese");
                            }
                        }
                    }
                }
            }
        }
        return tweets;
    }

    public void storeTweets() throws IOException {
        String filename = "tweets_" + System.currentTimeMillis();
        FileWriter fstream = new FileWriter(filename + ".txt");
        BufferedWriter out = new BufferedWriter(fstream);
        this.tweets.forEach((tweet) -> {
            try {
                out.write(tweet.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        out.close();
        compressGzipFile(filename + ".txt", filename + ".gzip");
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
