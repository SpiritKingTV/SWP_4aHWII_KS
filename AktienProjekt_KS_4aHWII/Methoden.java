import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Methoden {

public static ArrayList <LocalDate> dates = new ArrayList<LocalDate>();
   public static  Scanner reader = new Scanner(System.in);


    public static void connectToAPI(String unternehmen) throws IOException, JSONException {

        String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+ unternehmen + "&apikey=T5PECTJY4ZNB1WPA"));
        JSONObject obj = new JSONObject(link);
        JSONObject daily = obj.getJSONObject("Time Series (Daily)");
        //System.out.print(daily);
        for(int i = 0;i<50;i++){
            System.out.print(daily.names().get(i)+ ": close= ");
            dates.add(LocalDate.parse((CharSequence) daily.names().get(i)));
            System.out.println(daily.getJSONObject(daily.names().getString(i)).get("4. close"));

            System.out.println();
        }
        dates.sort(null);


    }

    public static String unternehmenName(){
        System.out.print("Geben Sie das zu erfassende Unternehmen ein: ");
        return reader.next();
    }

    
}
