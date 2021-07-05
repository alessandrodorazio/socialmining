package examples.scrapinng;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Scraper {

    public static final String USER_AGENT = " ";
    private String baseurl;

    public static void main(String[] args) throws IOException {
        Scraper crawler = new Scraper();
        crawler.baseurl = "https://www.romatoday.it";

        String landing = crawler.baseurl + "/eventi/";

        Document doc = null;
        ArrayList<Event> events= new ArrayList<Event>();

        if((doc = crawler.fetch(landing))!= null){
            crawler.extractBaseInfo(doc,events);
        }
        System.out.println(events);
        for(Event e : events){
            e.printToOut();
        }
    }
    public Document fetch(String url) throws IOException{
       try{
           //DOM
           Document webpage = Jsoup.connect(url).userAgent(USER_AGENT).get();
           System.out.println("Successfully downloaded "+ url);
           return webpage;
       }
       catch (HttpStatusException nt){
           System.out.println(nt.getStatusCode());
       }
       catch (SocketTimeoutException st) {
           System.out.println("timeout");
       }

        return null;
    }
    public void extractBaseInfo(Document doc, ArrayList<Event> events){
        System.out.println("Scanning web page....");

        Elements heds = doc.select("article:o-link-text u-block");
        Elements dates = doc.select("u-label-07 u-ml-medium u-inline-block");
        Elements places = doc.select("u-label-07 u-ml-medium u-inline-block");

        for(int i= 0; i< heds.size(); i++){
            Event event = new Event();
            event.Url= baseurl + heds.get(i).attr("href");

            event.Name=heds.get(i).text();
            event.EDate=dates.get(i).text();
            event.Place=places.get(i).text();

            events.add(event);
        }
    }
}
