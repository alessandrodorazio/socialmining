package NetworkAnalysis;

/* 2.1) Considering the initial twitter dataset, identifying tweets of users that
mention other users, and/or retweets other user’s tweets.*/

import entites.Tweet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
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
    public void mentions(){}
    public void retweets(){}
    public ArrayList<Tweet> mentions_and_retweets() throws IOException, ParseException {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("tweets1"));

        IndexReader indexReader = DirectoryReader.open(dir);

        IndexSearcher is = new IndexSearcher(indexReader);
        System.out.println(indexReader.getContext());
        //Query q = new TermQuery(new Term(query,tag));
        Analyzer analyzer = new EnglishAnalyzer();
        QueryParser parser = new QueryParser("mentions",analyzer);
        Query q= parser.parse("*:*");

        TopDocs top = is.search(q, 100); // perform a query and limit resultsnumber
        ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

        Document doc=null;
        for(ScoreDoc entry:hits){
// load document in memory (only the stored filed are available)
            doc=is.doc(entry.doc); /* the same as ir.document(entry.doc); */
            System.out.println("lang: "+doc.get("lang"));
            System.out.println("text: "+doc.get("text"));
            System.out.println("id: "+doc.get("id"));
            System.out.println("user: "+doc.get("user"));
        }
        indexReader.close();
        return tweets;
    }
}
