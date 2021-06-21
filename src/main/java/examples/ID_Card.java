package examples;
import org.apache.lucene.document.*;

public class ID_Card {
    String Tag, Name, Surname, State, City, Bio, BDate;
    float Height;

    static Document doc = new Document();
    static StringField tag = new StringField("tag", " ", Field.Store.YES);
    static TextField name = new TextField("name", " ", Field.Store.YES);
    static TextField surname = new TextField("surname", " ", Field.Store.YES);
    static StringField birth_date = new StringField("birth_date", " ", Field.Store.YES);
    static TextField state = new TextField("state", " ", Field.Store.YES);
    static TextField city = new TextField("city", " ", Field.Store.YES);
    static DoublePoint height = new DoublePoint("height", 0.0);
    static TextField bio = new TextField("bio", " ", Field.Store.YES);

    static {
        doc.add(tag);
        doc.add(name);
        doc.add(surname);
        doc.add(birth_date);
        doc.add(state);
        doc.add(city);
        doc.add(height);
        doc.add(bio);

    }

    public void updateDoc() {
        ID_Card.tag.setStringValue(Tag);
        ID_Card.name.setStringValue(Name);
        ID_Card.surname.setStringValue(Surname);
        ID_Card.birth_date.setStringValue(BDate);
        ID_Card.state.setStringValue(State);
        ID_Card.city.setStringValue(City);
        ID_Card.height.setDoubleValue(Height);
        ID_Card.bio.setStringValue(Bio);

    }
}
