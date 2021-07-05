package entites;

public class User {
    private String screen_name, name, id_str;
    private int id;

    public User(String screen_name, String name, String id_str, int id) {
        this.screen_name = screen_name;
        this.name = name;
        this.id_str = id_str;
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "screen_name='" + screen_name + '\'' +
                ", name='" + name + '\'' +
                ", id_str='" + id_str + '\'' +
                ", id=" + id +
                '}';
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
