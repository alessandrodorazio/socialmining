package examples;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.analysis.*;


import java.io.IOException;
import java.nio.file.FileSystems;

public class Indexer {
    public void indexing(ID_Card[] cards) throws Throwable{


    Directory dir= new SimpleFSDirectory(FileSystems.getDefault().getPath("idcards-index"));

    Analyzer analyzer = new ItalianAnalyzer();
    IndexWriterConfig cfg= new IndexWriterConfig(analyzer);
    IndexWriter writer = new IndexWriter(dir, cfg);

    Document doc = new Document();

        for(ID_Card id : cards) {
            System.out.println("Card : " + id.Tag);
            id.updateDoc();
        writer.addDocument(ID_Card.doc);
    }
writer.commit();
writer.close();
dir.close();
}
}