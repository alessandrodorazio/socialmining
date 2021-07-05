package examples;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.lang.module.Configuration;
import java.util.Arrays;


public class SimpleRestTest {
    static String OAuthAccessToken = "1370008737025523721-l0NVA73O3GBDOIvQZhsEyoraKbKZe2";
    static String OAuthAccessTokenSecret = "5v2JFpqUHESGVVKWo5QivtHQrxmHGvzuhy1LAYb9jo6ah";
    static String OAuthConsumerKey = "A5NX8rZaqjuY1ytpW2kbAHYWd";
    static String OAuthConsumerSecret = "lSr32RlHG29Em2oNX3V8X9bP6ensuvvVeoej5v8UhC3cEOgPNx";

    public static void main(String[] args) throws TwitterException {
        ConfigurationBuilder cfg = new ConfigurationBuilder();
        cfg.setJSONStoreEnabled(true);
        {
            cfg.setOAuthAccessToken(OAuthAccessToken);
            cfg.setOAuthAccessTokenSecret(OAuthAccessTokenSecret);
            cfg.setOAuthConsumerKey(OAuthConsumerKey);
            cfg.setOAuthConsumerSecret(OAuthConsumerSecret);
        }

        Twitter twitter = new TwitterFactory(cfg.build()).getInstance();

        //limit.get("/follower/ids").getRemaining() /*per controllare il limite per una richiesta (int),
        // si puo usare per uscire dal while quando si supera il numero invece di stampare in continuazione senza senso */

        long[] ids=twitter.getFriendsIDs(18935802L, -1).getIDs();
        long[] ids1=twitter.getFollowersIDs(18935802L, -1).getIDs();

        System.out.println(18935802L+": "+ Arrays.toString(ids));
    }
    }

