package NetworkAnalysis;

/* 2.1) Considering the initial twitter dataset, identifying tweets of users that
mention other users, and/or retweets other user’s tweets.*/

import entites.Tweet;
import examples.ID_Card;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

public class FindTweets {

    public ArrayList<Tweet> find() throws IOException, ParseException {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("tweets1"));

        //reader settings
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(indexReader);
        System.out.println(indexReader.getContext());
        //Query q = new TermQuery(new Term(query,tag));

        //finding tweets with user_mentions
        Analyzer analyzer = new EnglishAnalyzer();
        QueryParser parser = new QueryParser("user_mentions",analyzer);
        Query q= parser.parse("*:*");
        TopDocs top = is.search(q,100000000); // perform a query and limit resultsnumber
        ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

        //writer settings
        Directory dir2 = new SimpleFSDirectory(FileSystems.getDefault().getPath("tweets2.1"));
        IndexWriterConfig cfg= new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir2, cfg);
        Document doc = new Document();
        Tweet tweet = new Tweet();
        for(ScoreDoc entry:hits){
            tweet = new Tweet(); // pulisco tutto;
            // load document in memory (only the stored filed are available)
            doc=is.doc(entry.doc); /* the same as ir.document(entry.doc); */

            tweet.Created_at = doc.get("created_at");
            tweet.Id = Integer.parseInt(doc.get("id"));
            tweet.Favorite_count= Integer.parseInt(doc.get("favorite_count"));
            tweet.Retweet_count = Integer.parseInt(doc.get("retweet_count"));
            tweet.Lang=doc.get("lang");
            tweet.Text=doc.get("text");
            tweet.User_mentions=doc.get("user_mentions");

            tweet.updateDoc();
            writer.addDocument(Tweet.doc);
            tweets.add(tweet);
        }
        writer.commit();
        writer.close();
        dir2.close();
        indexReader.close();
        return tweets;
        }

        public ArrayList<Tweet> getTweets() throws IOException, ParseException {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("tweets2.1"));

            //reader settings
            IndexReader indexReader = DirectoryReader.open(dir);
            IndexSearcher is = new IndexSearcher(indexReader);
            Analyzer analyzer = new EnglishAnalyzer();
            QueryParser parser = new QueryParser("user_mentions",analyzer);
            Query q= parser.parse("*:*");
            TopDocs top = is.search(q,100000000); // perform a query and limit resultsnumber
            ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

            Document doc = new Document();
            Tweet tweet = new Tweet();
            for(ScoreDoc entry:hits){
                tweet = new Tweet(); // pulisco tutto;
                // load document in memory (only the stored filed are available)
                doc=is.doc(entry.doc); /* the same as ir.document(entry.doc); */

                tweet.Created_at = doc.get("created_at");
                tweet.Id = Integer.parseInt(doc.get("id"));
                tweet.Favorite_count= Integer.parseInt(doc.get("favorite_count"));
                tweet.Retweet_count = Integer.parseInt(doc.get("retweet_count"));
                tweet.Lang=doc.get("lang");
                tweet.Text=doc.get("text");
                tweet.User_mentions=doc.get("user_mentions");

                tweets.add(tweet);
            }

            return tweets;
        }
    }








