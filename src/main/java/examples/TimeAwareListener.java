package examples;

import net.seninp.jmotif.sax.SAXException;
import net.seninp.jmotif.sax.SAXProcessor;
import net.seninp.jmotif.sax.alphabet.NormalAlphabet;
import net.seninp.jmotif.sax.datastructure.SAXRecord;
import net.seninp.jmotif.sax.datastructure.SAXRecords;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TimeAwareListener implements StatusListener {
    private long last=-1;
    private long counter=0;

    private long DELTA = 10*1000; // 10 * 1000 milliseconds = 10 seconds
    @Override
    public void onStatus(Status status) {
        long current = status.getCreatedAt().getTime();

        if(last==-1){
            last=current;
        }
        if (current-last>= DELTA){
            System.out.println(counter);
            last = current;
            counter=1;
        }
        else {
            counter++;
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int i) {

    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {

    }
    public static void main(String[] args) throws SAXException {
        int alphabetSize = 2;
        double nThreshold=0.01;

        NormalAlphabet na= new NormalAlphabet();
        SAXProcessor sp = new SAXProcessor();

        double[] ts = {10,20,20,50,80,10,50,80,10,5};

        SAXRecords res = sp.ts2saxByChunking(ts,ts.length,na.getCuts(alphabetSize),nThreshold);

        String sax = res.getSAXString("");
        System.out.println(sax);
        System.out.println(sax.matches("a+b+a*b*a*"));
    }
}
