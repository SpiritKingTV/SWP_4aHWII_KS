package Methods;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class API {

public static HashMap<LocalDate, String> HMDatesAndValues = new HashMap<>();
    public static ArrayList<LocalDate> dates = new ArrayList<LocalDate>();


    public static void connectToAPI(String unternehmen) throws IOException, JSONException{
        String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + unternehmen + "&outputsize=full&apikey=T5PECTJY4ZNB1WPA"));
        JSONObject obj = new JSONObject(link);
        JSONObject daily = obj.getJSONObject("Time Series (Daily)");

        for(int  i =0; i<daily.names().length();i++){
            daily.names().get(i);
            dates.add(LocalDate.parse((CharSequence)daily.names().get(i)));
            HMDatesAndValues.put(LocalDate.parse((CharSequence)daily.names().get(i)),daily.getJSONObject(daily.names().getString(i)).get("5. adjusted close").toString());
        }
        dates.sort(null);
    }


}
