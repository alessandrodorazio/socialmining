package examples.scrapinng;

public class Event {
    String Url, Name, EDate,Price,Place, Info,Body,Title;

    @Override
    public String toString() {
        return "Event{" +
                "Url='" + Url + '\'' +
                ", Name='" + Name + '\'' +
                ", EDate='" + EDate + '\'' +
                ", Price='" + Price + '\'' +
                ", Place='" + Place + '\'' +
                ", Info='" + Info + '\'' +
                ", Body='" + Body + '\'' +
                ", Title='" + Title + '\'' +
                '}';
    }
    public void printToOut(){
        System.out.println(this.toString());
    }
}
