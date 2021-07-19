package exercises.ex2;

import NetworkAnalysis.FindTweets;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class Ex2Point1 {
    public static void main(String[] args) throws IOException, ParseException {
        FindTweets ft = new FindTweets();
        ft.find();  //questo per creare dataset, crea un dataset salvato in memoria eseguirlo piu volte causa dupplicazioni
        ft.getTweets(); //questo per leggere dataset se gia creato, in ft.find()
    }
}
