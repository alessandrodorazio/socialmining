package examples;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;


import java.io.IOException;
import java.nio.file.FileSystems;

public class Searcher {
    public void search(String query, String tag) throws IOException, ParseException {

        Directory dir = new SimpleFSDirectory(FileSystems.getDefault().getPath("idcards-index"));

        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(indexReader);

        //Query q = new TermQuery(new Term(query,tag));
        Analyzer analyzer = new ItalianAnalyzer();
        QueryParser parser = new QueryParser("bio",analyzer);
        Query q= parser.parse("DIO AND Sbudellato");

        TopDocs top = is.search(q, 100); // perform a query and limit resultsnumber
        ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDoc isa tuple)

        Document doc=null;
        for(ScoreDoc entry:hits){
// load document in memory (only the stored filed are available)
            doc=is.doc(entry.doc); /* the same as ir.document(entry.doc); */
            System.out.println("tag: "+doc.get("tag"));
            System.out.println("surname: "+doc.get("surname"));
            System.out.println("name: "+doc.get("name"));
            System.out.println("birth_date: "+doc.get("birth_date"));
        }
        indexReader.close();
    }
}
//TWEET STRUCTURE
/*
{
        "created_at": "Mon Jul 05 14:23:00 +0000 2021",
        "id": 1412054409815658500,
        "id_str": "1412054409815658500",
        "text": "RT @cutexxcnco_: cómo no les va a gustar reggaetón lento? si es alto tema \n\n#KCAMexico #CNCO #CNCOwners",
        "source": "<a href=\"http://twitter.com/download/android\" rel=\"nofollow\">Twitter for Android</a>",
        "truncated": false,
        "in_reply_to_status_id": null,
        "in_reply_to_status_id_str": null,
        "in_reply_to_user_id": null,
        "in_reply_to_user_id_str": null,
        "in_reply_to_screen_name": null,
        "user": {
        "id": 1267544506112630800,
        "id_str": "1267544506112630785",
        "name": "Dayana Orellana",
        "screen_name": "Daya_Orellan23",
        "location": null,
        "url": null,
        "description": null,
        "translator_type": "none",
        "protected": false,
        "verified": false,
        "followers_count": 5,
        "friends_count": 38,
        "listed_count": 0,
        "favourites_count": 906,
        "statuses_count": 2867,
        "created_at": "Mon Jun 01 19:52:26 +0000 2020",
        "utc_offset": null,
        "time_zone": null,
        "geo_enabled": false,
        "lang": null,
        "contributors_enabled": false,
        "is_translator": false,
        "profile_background_color": "F5F8FA",
        "profile_background_image_url": "",
        "profile_background_image_url_https": "",
        "profile_background_tile": false,
        "profile_link_color": "1DA1F2",
        "profile_sidebar_border_color": "C0DEED",
        "profile_sidebar_fill_color": "DDEEF6",
        "profile_text_color": "333333",
        "profile_use_background_image": true,
        "profile_image_url": "http://pbs.twimg.com/profile_images/1411548919792902149/YjHwnRk3_normal.jpg",
        "profile_image_url_https": "https://pbs.twimg.com/profile_images/1411548919792902149/YjHwnRk3_normal.jpg",
        "profile_banner_url": "https://pbs.twimg.com/profile_banners/1267544506112630785/1617644027",
        "default_profile": true,
        "default_profile_image": false,
        "following": null,
        "follow_request_sent": null,
        "notifications": null,
        "withheld_in_countries": []
        },
        "geo": null,
        "coordinates": null,
        "place": null,
        "contributors": null,
        "retweeted_status": {
        "created_at": "Mon Jul 05 13:45:31 +0000 2021",
        "id": 1412044976918249500,
        "id_str": "1412044976918249479",
        "text": "cómo no les va a gustar reggaetón lento? si es alto tema \n\n#KCAMexico #CNCO #CNCOwners",
        "source": "<a href=\"http://twitter.com/download/android\" rel=\"nofollow\">Twitter for Android</a>",
        "truncated": false,
        "in_reply_to_status_id": null,
        "in_reply_to_status_id_str": null,
        "in_reply_to_user_id": null,
        "in_reply_to_user_id_str": null,
        "in_reply_to_screen_name": null,
        "user": {
        "id": 1352323168862658600,
        "id_str": "1352323168862658560",
        "name": "Cncoxxshine",
        "screen_name": "cutexxcnco_",
        "location": null,
        "url": null,
        "description": "Esta cuenta es es para votaciones y dinámicas 🧡",
        "translator_type": "none",
        "protected": false,
        "verified": false,
        "followers_count": 99,
        "friends_count": 110,
        "listed_count": 0,
        "favourites_count": 1239,
        "statuses_count": 3896,
        "created_at": "Thu Jan 21 18:32:45 +0000 2021",
        "utc_offset": null,
        "time_zone": null,
        "geo_enabled": false,
        "lang": null,
        "contributors_enabled": false,
        "is_translator": false,
        "profile_background_color": "F5F8FA",
        "profile_background_image_url": "",
        "profile_background_image_url_https": "",
        "profile_background_tile": false,
        "profile_link_color": "1DA1F2",
        "profile_sidebar_border_color": "C0DEED",
        "profile_sidebar_fill_color": "DDEEF6",
        "profile_text_color": "333333",
        "profile_use_background_image": true,
        "profile_image_url": "http://pbs.twimg.com/profile_images/1377511473371549696/djudQrCR_normal.jpg",
        "profile_image_url_https": "https://pbs.twimg.com/profile_images/1377511473371549696/djudQrCR_normal.jpg",
        "profile_banner_url": "https://pbs.twimg.com/profile_banners/1352323168862658560/1617259303",
        "default_profile": true,
        "default_profile_image": false,
        "following": null,
        "follow_request_sent": null,
        "notifications": null,
        "withheld_in_countries": []
        },
        "geo": null,
        "coordinates": null,
        "place": null,
        "contributors": null,
        "is_quote_status": false,
        "quote_count": 0,
        "reply_count": 0,
        "retweet_count": 7,
        "favorite_count": 2,
        "entities": {
        "hashtags": [
        {
        "text": "KCAMexico",
        "indices": [
        59,
        69
        ]
        },
        {
        "text": "CNCO",
        "indices": [
        70,
        75
        ]
        },
        {
        "text": "CNCOwners",
        "indices": [
        76,
        86
        ]
        }
        ],
        "urls": [],
        "user_mentions": [],
        "symbols": []
        },
        "favorited": false,
        "retweeted": false,
        "filter_level": "low",
        "lang": "es"
        },
        "is_quote_status": false,
        "quote_count": 0,
        "reply_count": 0,
        "retweet_count": 0,
        "favorite_count": 0,
        "entities": {
        "hashtags": [
        {
        "text": "KCAMexico",
        "indices": [
        76,
        86
        ]
        },
        {
        "text": "CNCO",
        "indices": [
        87,
        92
        ]
        },
        {
        "text": "CNCOwners",
        "indices": [
        93,
        103
        ]
        }
        ],
        "urls": [],
        "user_mentions": [
        {
        "screen_name": "cutexxcnco_",
        "name": "Cncoxxshine",
        "id": 1352323168862658600,
        "id_str": "1352323168862658560",
        "indices": [
        3,
        15
        ]
        }
        ],
        "symbols": []
        },
        "favorited": false,
        "retweeted": false,
        "filter_level": "low",
        "lang": "es",
        "timestamp_ms": "1625494980659"
        }
*/