package entites;

import java.util.ArrayList;

public class Tweet {
    private String created_at, text, id_str;
    private int id;
    private User user;
    private ArrayList<User> mentions;
}
