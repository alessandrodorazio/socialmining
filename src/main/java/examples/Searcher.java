package examples;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;


import java.io.IOException;
import java.nio.file.FileSystems;

public class Searcher {
    public void search(String query, String tag) throws IOException, ParseException {

        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("idcards-index"));

        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(indexReader);

        //Query q = new TermQuery(new Term(query,tag));
        Analyzer analyzer = new ItalianAnalyzer();
        QueryParser parser = new QueryParser("bio",analyzer);
        Query q= parser.parse("DIO AND Sbudellato");

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
