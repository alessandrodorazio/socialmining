package exercises.ex1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import twitter4j.JSONArray;
import twitter4j.JSONObject;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Ex1Point1 {

    public static void compressGZip() throws IOException {
        InputStream in = new FileInputStream("finaldataset0807.txt");
        OutputStream out = new GZIPOutputStream(
                new BufferedOutputStream(new FileOutputStream("finaldataset.gz")));

        byte[] bytes = new byte[32*1024];
        int len;
        while((len = in.read(bytes)) > 0)
            out.write(bytes, 0, len);

        in.close();
        out.close();
    }

    public static void compress() throws IOException {
        compressGZip();
    }

    private static Document createDocument(JSONObject obj) {

        Document doc = new Document();
        doc.add(new StringField("id", (String) obj.getString("id_str"), Field.Store.YES));

        doc.add(new StringField("created_at", (String) obj.getString("created_at"), Field.Store.YES));
        doc.add(new TextField("text", obj.getString("text"), Field.Store.YES));
        doc.add(new LongPoint("retweet_count", obj.getLong("retweet_count")));
        doc.add(new LongPoint("favorite_count", obj.getLong("favorite_count")));
        doc.add(new StringField("lang", obj.getString("lang"), Field.Store.YES));
        JSONObject entities = obj.getJSONObject("entities");
        JSONArray userMentions = entities.getJSONArray("user_mentions");
        for(int i=0; i<userMentions.length(); i++) {
            JSONObject mention = userMentions.getJSONObject(i);
            doc.add(new StringField("mentions", mention.getString("id_str"), Field.Store.YES));

        }
        return doc;
    }


    public static void storeIntoLucene() throws IOException {
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("dataset_tweets"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig());

        BufferedReader in = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("finaldataset.gz"))));
        String content;
        JSONObject obj;
        int count = 0;


        while ((content = in.readLine()) != null) {
            System.out.println(count);
            count++;

            Document doc = createDocument(new JSONObject(content));
            try {
                writer.addDocument(doc);
                if(count % 50000 == 0) {
                    writer.commit();
                }
                //writer.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.close();


    }

    public void datasetIntoLucene() throws IOException {
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("dataset_tweets"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, cfg);

    }

    public void checkTweetsInEng() {

    }

    public boolean checkTweetLang() {
        return false;
    }

    public void datasetIntoGZip() {

    }

}
