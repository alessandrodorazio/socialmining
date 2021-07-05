package examples;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.lang.module.Configuration;


public class TweeterStreamTest {
    static String OAuthAccessToken = "1370008737025523721-l0NVA73O3GBDOIvQZhsEyoraKbKZe2";
    static String OAuthAccessTokenSecret = "5v2JFpqUHESGVVKWo5QivtHQrxmHGvzuhy1LAYb9jo6ah";
    static String OAuthConsumerKey = "A5NX8rZaqjuY1ytpW2kbAHYWd";
    static String OAuthConsumerSecret = "lSr32RlHG29Em2oNX3V8X9bP6ensuvvVeoej5v8UhC3cEOgPNx";

    public static void main(String[] args){

        StatusListener textOriented = new MyTextListener(); //normal representation
        StatusListener storeOriented = new StoreListener(); //json representation
        TimeAwareListener timeAware = new TimeAwareListener();

        ConfigurationBuilder cfg = new ConfigurationBuilder();
        cfg.setJSONStoreEnabled(true);
        {
            cfg.setOAuthAccessToken(OAuthAccessToken);
            cfg.setOAuthAccessTokenSecret(OAuthAccessTokenSecret);
            cfg.setOAuthConsumerKey(OAuthConsumerKey);
            cfg.setOAuthConsumerSecret(OAuthConsumerSecret);
        }
        {

        }

        TwitterStream twitterStream = new TwitterStreamFactory(cfg.build()).getInstance();

        //twitterStream.addListener(timeAware);
        //twitterStream.addListener(textOriented);
        twitterStream.addListener(storeOriented);

         twitterStream.sample();
        /*
        FilterQuery fq = new FilterQuery();
        String[] queryText = {"Astrazeneca","Phizer","vaccine"};
        fq.track(queryText);
        twitterStream.filter(fq);
        */
    }
}
