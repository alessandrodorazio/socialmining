package NetworkAnalysis;

import entites.Tweet;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Start {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Starting...");
        FindTweets ft = new FindTweets();

        ArrayList<Tweet> tweets= ft.find();

    }
}
