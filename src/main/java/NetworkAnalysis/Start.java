package NetworkAnalysis;

import entites.Tweet;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Start {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Starting...");
/*
        //2.1
        FindTweets ft = new FindTweets();
        //ArrayList<Tweet> tweets= ft.find();       //isolare i tweet con le menzioni
        ArrayList<Tweet> tweets= ft.getTweets();
/*
        //2.2
        Network network = new Network();
        //2.2a
        HashMap<Integer, List<Integer>> mentionsGraph = network.getMentionsGraph(tweets); */
        //2.2b
        MentionsGraph  graph1 = new MentionsGraph();
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        graph1.addEdge(1, 3);
        graph1.addEdge(3, 1);
        graph1.addEdge(2, 1);
        graph1.addEdge(1, 3);
        graph1.addEdge(1, 2);

        TimeGraph  graph2 = new TimeGraph();
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 3);
        graph2.addEdge(1, 3);
        graph2.addEdge(3, 1);
        graph2.addEdge(2, 1);
        graph2.addEdge(1, 3);
        graph2.addEdge(1, 2);
        System.out.println( graph1);
        System.out.println( graph2);

        /*for(Tweet tweet : tweets) {
          long author = tweet.author;
          for(long user: tweet.User_mentions){
              graph.addEdge(author, user, 1);
          }
          ;*/
        }

}
