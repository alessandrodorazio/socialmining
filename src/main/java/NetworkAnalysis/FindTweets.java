package NetworkAnalysis;

/* 2.1) Considering the initial twitter dataset, identifying tweets of users that
mention other users, and/or retweets other user’s tweets.*/

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
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

public class FindTweets {
    public void mentions(){}
    public void retweets(){}
    public void mentions_and_retweets() throws IOException, ParseException {
        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("ids"));

        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(indexReader);

        //Query q = new TermQuery(new Term(query,tag));
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("bio",analyzer);
        Query q= parser.parse("@");

        TopDocs top = is.search(q, 100); // perform a query and limit resultsnumber
        ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

        Document doc=null;
        for(ScoreDoc entry:hits){
// load document in memory (only the stored filed are available)
            doc=is.doc(entry.doc); /* the same as ir.document(entry.doc); */
            System.out.println("tag: "+doc.get("tag"));
            System.out.println("surname: "+doc.get("surname"));
            System.out.println("name: "+doc.get("name"));
            System.out.println("birth_date: "+doc.get("birth_date"));
        }
        indexReader.close();
    }
}
