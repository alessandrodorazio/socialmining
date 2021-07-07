package entites;



import org.apache.lucene.document.Document;

import java.util.ArrayList;

public class Tweet {
   public  String Created_at, Text, Lang;
   public  int Id,Retweet_count,Favorite_count;
   public String User_mentions;
   public static Document doc = new Document();

   /* static {
        doc.add(created_at);
        doc.add(text);
        doc.add(lang);
        doc.add(id);
        doc.add(favorite_count);
        doc.add(user_mentions);
    }
*/
    public void updateDoc(){}

/*
    @Override
    public String toString() {
        return "Tweet{" +
                "created_at='" + created_at + '\'' +
                ", text='" + text + '\'' +
                ", id_str='" + id_str + '\'' +
                ", id=" + id +
                ", user=" + user +
                ", mentions=" + mentions +
                ", retweet=" + retweet +
                '}';
    }
*/
}
