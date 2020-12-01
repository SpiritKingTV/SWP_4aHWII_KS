import org.json.JSONException;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException, JSONException {
String datum = "";
String closeValue = "";
        Methode m = new Methode();
String unternehmen = m.unternehmenName();

m.connectToAPI(unternehmen);
        m.connectDB(m.urlDB);
        m.createNewDatabase(m.urlDB );
        m.createTableofDB(m.urlDB, unternehmen);
 for(int i = 0; i< Methode.dates.size(); i++){
     datum = m.dates.get(i).toString();
     closeValue = m.HMzwischenspeicher.get(m.dates.get(i));
     m.insert(unternehmen,datum,closeValue);
 }
        m.selectDB(unternehmen);



    }
}
