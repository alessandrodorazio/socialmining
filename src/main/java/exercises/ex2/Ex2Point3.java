package exercises.ex2;

import NetworkAnalysis.MentionsGraph;
import NetworkAnalysis.Rank;
import NetworkAnalysis.TermGraph;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class Ex2Point3 {
    public static void main(String[] args) throws IOException, ParseException {
        //2.2a
        TermGraph tg = new TermGraph();
        tg.populate(); // per riempire il grafo, (ci mette tantissimo, potrebbe richedere un paio di ore)
        TermGraph tg1 = new TermGraph();
        tg1.addAllEdges(tg.largestConnectedComponents());

        //2.2b
        MentionsGraph mg = new MentionsGraph();
        //mg.populate("find"); //find se non esite dataset lo stesso creato nel punto 2.1
        mg.populate("");// ci mette relativamente poco per completare
        MentionsGraph mg1= new MentionsGraph();
        mg1.addAllEdges(mg.largestConnectedComponents());

        Rank rank = new Rank();

        System.out.println(rank.HITS(tg1.adj));
        System.out.println(rank.LPA(tg1.adj));

        System.out.println(rank.HITS(mg.adj));
        System.out.println(rank.KPP_NEG(mg.adj));
    }

}
