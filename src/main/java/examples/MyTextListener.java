package examples;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class MyTextListener implements StatusListener {


    @Override
    public void onStatus(Status status) {
        //if(status.getText().toLowerCase().contains("astrazneca")){
            System.out.println(status.getUser().getName()+ ":=====:"+status.getText());
        //}

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
}
