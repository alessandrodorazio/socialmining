package examples;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.analysis.*;
import preprocessing.TweetDownloader;

import java.nio.file.FileSystems;


public class Start {
    public static void main(String[] str) throws Throwable {
        TweetDownloader t = new TweetDownloader("ids");
        System.out.println(t.tweets);
/*   ID_Card[] cards = new ID_Card[2];
    cards[0] = new ID_Card();
    cards[0].Tag="CDxede431";
    cards[0].Name="marcello";
    cards[0].Surname="rossa";
    cards[0].BDate="09/05/97";
    cards[0].City="pisa";
    cards[0].State="pisa";
    cards[0].Bio="dio cane sbudellato ";
    cards[0].Height= (float) 1.7;
    cards[1] = new ID_Card();
    cards[1].Tag="C43de4311";
    cards[1].Name="pietro";
    cards[1].Surname="rossi";
    cards[1].BDate="09/05/971";
    cards[1].City="pisa1";
    cards[1].State="pisa1";
    cards[1].Bio="dio cane maledetto ";
    cards[1].Height= (float) 1.71;

    Indexer idx = new Indexer();
    idx.indexing(cards);
*/

    }
}
