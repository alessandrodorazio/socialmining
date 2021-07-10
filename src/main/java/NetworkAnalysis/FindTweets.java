package NetworkAnalysis;

/* 2.1) Considering the initial twitter dataset, identifying tweets of users that
mention other users, and/or retweets other user’s tweets.*/

import entites.Tweet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
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

    //questo metodo serve per trovare i tweet in cui ci sono delle menzioni e memorizzarli una volta trovati(si usa solo una volta)
    public ArrayList<Tweet> find() throws IOException, ParseException {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("dataset_tweets"));

        //reader settings
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(indexReader);
        //Query q = new TermQuery(new Term(query,tag));

        //finding tweets with user_mentions
        Analyzer analyzer = new EnglishAnalyzer();
        QueryParser parser = new QueryParser("mentions", analyzer);
        Query q = parser.parse("*:*");
        TopDocs top = is.search(q,30000000); // perform a query and limit resultsnumber
        ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

        //writer settings
        Directory dir2 = new SimpleFSDirectory(FileSystems.getDefault().getPath("tweets2.1"));
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir2, cfg);
        Document doc = new Document();
        Tweet tweet = new Tweet();
        for (ScoreDoc entry : hits) {
            doc = is.doc(entry.doc); /* the same as ir.document(entry.doc); */
            String[] mentions = doc.getValues("mentions");
            if (!(mentions.length == 0)) {
                tweet = new Tweet();

                tweet.Created_at = doc.get("created_at");
                tweet.Id = doc.get("id");
                tweet.Lang = doc.get("lang");
                tweet.Text = doc.get("text");
                tweet.User_ID = doc.get("user_id");
                tweet.Mentions = mentions;

                tweet.updateDoc();
                writer.addDocument(Tweet.doc);
                tweets.add(tweet);
            }

        }
        writer.close();
        dir2.close();
        indexReader.close();
        return tweets;
    }
        //questo metodo serve per prendere i tweet trovati con metodo precedente
        public ArrayList<Tweet> getTweets() throws IOException, ParseException {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("tweets2.1"));

            //reader settings
            IndexReader indexReader = DirectoryReader.open(dir);
            IndexSearcher is = new IndexSearcher(indexReader);
            Analyzer analyzer = new EnglishAnalyzer();
            QueryParser parser = new QueryParser("mentions",analyzer);
            Query q= parser.parse("*:*");
            TopDocs top = is.search(q,100000000); // perform a query and limit resultsnumber
            ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

            Document doc = new Document();
            Tweet tweet = new Tweet();
            for (ScoreDoc entry : hits) {
                doc = is.doc(entry.doc); /* the same as ir.document(entry.doc); */
                String[] mentions = doc.getValues("mentions");
                tweet = new Tweet(); // pulisco tutto;

                tweet.Created_at = doc.get("created_at");
                tweet.Id = doc.get("id");
                tweet.Lang = doc.get("lang");
                tweet.Text = doc.get("text");
                tweet.User_ID = doc.get("user_id");
                tweet.Mentions = mentions;

                tweets.add(tweet);
            }
            return tweets;
        }
    }





/*


Document<stored,indexed,tokenized,omitNorms,indexOptions=DOCS<id:1351500061432287232>
        stored,indexed,tokenized,omitNorms,indexOptions=DOCS<created_at:Tue Jan 19 12:01:38 +0000 2021>
        stored,indexed,tokenized<text:RT @mac_puck: @ChrisGiles_ @FinancialTimes The vaccination programme is organised by the NHS and is working superbly. The BBC call it "the…>" +
        " stored,indexed,tokenized,omitNorms,indexOptions=DOCS<lang:en>" +
        " stored,indexed,tokenized,omitNorms,indexOptions=DOCS<user_id:1898369586>" +
        " stored,indexed,tokenized,omitNorms,indexOptions=DOCS<mentions:885425256797798401>" +
        " stored,indexed,tokenized,omitNorms,indexOptions=DOCS<mentions:305073570>" +
        " stored,indexed,tokenized,omitNorms,indexOptions=DOCS<mentions:4898091>>
*/