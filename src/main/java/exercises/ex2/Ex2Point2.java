package exercises.ex2;

import NetworkAnalysis.MentionsGraph;
import NetworkAnalysis.TermGraph;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class Ex2Point2 {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("start");
        //2.2a
        TermGraph tg = new TermGraph();
        tg.populate(); // per riempire il grafo, (ci mette tantissimo, potrebbe richedere un paio di ore)
        System.out.println(tg);
        //2.2b
        MentionsGraph mg = new MentionsGraph();
        mg.populate("find"); //find se non esite dataset lo stesso creato nel punto 2.1
        //mg.populate("");// ci mette relativamente poco per completare
        System.out.println(mg);
        System.out.println("done!");
    }
}
