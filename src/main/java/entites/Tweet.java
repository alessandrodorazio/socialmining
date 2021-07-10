package entites;

import org.apache.lucene.document.*;
import twitter4j.JSONArray;
import twitter4j.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Tweet {
   public  String Created_at, Text, Lang,Id,User_ID;
   //public  Long Retweet_count,Favorite_count;
   public String[] Mentions;
   public static Document doc = new Document();

   static StringField id=new StringField("id", " ", Field.Store.YES);
   static StringField created_at = new StringField("created_at", " ", Field.Store.YES);
   static StringField user_id = new StringField("user_id", " ", Field.Store.YES);
   static TextField  text = new TextField("text", " ", Field.Store.YES);
   //static LongPoint  retweet_count  = new LongPoint("retweet_count", 0);
   //static LongPoint favorite_count = new LongPoint("favorite_count", 0);
   static StringField lang = new StringField("lang", " ", Field.Store.YES);
   static StringField mentions = new StringField("mentions", " ", Field.Store.YES);

   static {
      doc.add(id);
      doc.add(created_at);
      doc.add(text);
      //doc.add(retweet_count);
      //doc.add(favorite_count);
      doc.add(lang);
      doc.add(mentions);
      doc.add(user_id);
      }



    public void updateDoc(){
       Tweet.id.setStringValue(Id);
       Tweet.created_at.setStringValue(Created_at);
       Tweet.text.setStringValue(Text);
       Tweet.user_id.setStringValue(User_ID);
       //Tweet.retweet_count.setLongValue(Retweet_count);
       //Tweet.favorite_count.setLongValue(Favorite_count);
       Tweet.lang.setStringValue(Lang);
       for(String mention : Mentions) Tweet.mentions.setStringValue(mention);
    }

   @Override
   public String toString() {
      return "Tweet{" +
              "Created_at='" + Created_at + '\'' +
              ", Text='" + Text + '\'' +
              ", Lang='" + Lang + '\'' +
              ", Id='" + Id + '\'' +
              ", User_ID='" + User_ID + '\'' +
              ", Mentions=" + Arrays.toString(Mentions) +
              '}';
   }
}
