package entites;

import java.util.ArrayList;

public class Tweet {
    private String created_at, text, id_str;
    private int id;
    private User user;
    private ArrayList<User> mentions;
    private Tweet retweet = null;

    public Tweet(String created_at, String text, String id_str, int id, User user, ArrayList<User> mentions, Tweet retweet) {
        this.created_at = created_at;
        this.text = text;
        this.id_str = id_str;
        this.id = id;
        this.user = user;
        this.mentions = mentions;
        this.retweet = retweet;
    }

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<User> getMentions() {
        return mentions;
    }

    public void setMentions(ArrayList<User> mentions) {
        this.mentions = mentions;
    }

    public Tweet getRetweet() {
        return retweet;
    }

    public void setRetweet(Tweet retweet) {
        this.retweet = retweet;
    }
}
