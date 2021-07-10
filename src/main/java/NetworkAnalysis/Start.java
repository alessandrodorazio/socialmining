package NetworkAnalysis;

import entites.Tweet;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Start {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Starting...");
        System.out.println("Getting tweets...");
        //2.1
        FindTweets ft = new FindTweets();
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //metodo per trovare, salvare e resituire tweet (creare dataset, eseguire una volta sola quando dataset è vuoto)
        //ArrayList<Tweet> tweets= ft.find();

        //metodo per recuperare e restiutire tweet (dataset gia creato, eseguire piu volte dopo ft.find)
        ArrayList<Tweet> tweets= ft.getTweets();
////////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("Done, nice");
        System.out.println("Let make some graph :D");

        System.out.println("Mentions Graph first...");

        //2.2b
        MentionsGraph  graph1 = new MentionsGraph();
        for (Tweet t : tweets){
            long from = Long.parseLong(t.User_ID);
            if(graph1.adj.keySet().size()>=10000){      //limit first 10k users
                for (String mention : t.Mentions) {
                    long to = Long.parseLong(mention);
                    if (graph1.adj.keySet().contains(from) && graph1.adj.keySet().contains(to))
                        graph1.addEdge(from, to);
                }
            }
            else {
                for (String mention : t.Mentions) {
                    long to = Long.parseLong(mention);
                    graph1.addEdge(from, to);
            }
            }

        }
        /*
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        graph1.addEdge(1, 3);
        graph1.addEdge(3, 1);
        graph1.addEdge(2, 1);
        graph1.addEdge(1, 3);
        graph1.addEdge(1, 2);
        graph1.addEdge(1,4);
        graph1.addEdge(6,5);
        graph1.addEdge(7,6);
        TimeGraph  graph2 = new TimeGraph();
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 3);
        graph2.addEdge(1, 3);
        graph2.addEdge(3, 1);
        graph2.addEdge(2, 1);
        graph2.addEdge(1, 3);
        graph2.addEdge(1, 2);
        graph2.addEdge(4,5);
*/
        System.out.println("Done!");
        //System.out.println( graph1);
        //System.out.println( graph2);
        //ArrayList<Long> keys = (ArrayList<Long>) graph1.adj.keySet();
       // System.out.println(graph1.largestConnectedComponents());
        MentionsGraph graph3 = new MentionsGraph();
        System.out.println("Now largest Connected Component...");
        graph3.addAllEdges(graph1.largestConnectedComponents());

        System.out.println( graph3);

        }

}
