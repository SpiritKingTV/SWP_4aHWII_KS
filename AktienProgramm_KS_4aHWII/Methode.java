import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Methode  {
    static String urlDB = "jdbc:sqlite:C:\\Users\\repet\\IdeaProjects\\AktienProjekt\\Database.db";
    public static ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
    public static HashMap<LocalDate, String> HMzwischenspeicher = new HashMap<>();
    public static HashMap<LocalDate, String> HMsortiert = new HashMap<>();
    public static Map<String, Double> readDataFromDB = new TreeMap<String,Double>();
    public static Map<String, Double> avgAndData = new TreeMap<String,Double>();
    public static ArrayList<Double> closeWerte = new ArrayList<Double>();
    public static ArrayList<Double> gleitender_Mittelwert = new ArrayList<Double>();
    public static double average;
    // public static HashMap<LocalDate, String> HMsortiert;


    public static Scanner reader = new Scanner(System.in);


    public static void connectToAPI(String unternehmen) throws IOException, JSONException {

        String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + unternehmen + "&outputsize=full&apikey=T5PECTJY4ZNB1WPA"));
        JSONObject obj = new JSONObject(link);
        JSONObject daily = obj.getJSONObject("Time Series (Daily)");
        //System.out.print(daily);
        for (int i = 0; i < daily.names().length(); i++) {
          daily.names().get(i);
            dates.add(LocalDate.parse((CharSequence) daily.names().get(i)));

            HMzwischenspeicher.put(LocalDate.parse((CharSequence) daily.names().get(i)), daily.getJSONObject(daily.names().getString(i)).get("4. close").toString());

            daily.getJSONObject(daily.names().getString(i)).get("4. close");


        }
        dates.sort(null);


    }

        public static void sortAPIentries(){
          for(int i = 0;i<dates.size();i++){
              HMsortiert.put(dates.get(i),HMzwischenspeicher.get(dates.get(i)));

               closeWerte.add(Double.parseDouble(HMzwischenspeicher.get(dates.get(i))));
          }
        }

    public static String unternehmenName() {
        System.out.print("Geben Sie das zu erfassende Unternehmen ein: ");
        return reader.next();
    }

    public static void connectDB(String url) {
        Connection conn = null;
        try {


            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void createTableofDB(String url, String unternehmen) {


        String sql = "CREATE TABLE IF NOT EXISTS " + unternehmen + " (\n"
                + " DateOfValue text PRIMARY KEY UNIQUE,\n"
                + " CloseValue double,"
                +" Schnitt double)";
//double1

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connection(String url) {


        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String unternehmen, String datum, double closeValue, double gleitenderMittelwert) {
        String sql = "INSERT OR REPLACE INTO " + unternehmen + "(DateOfValue,CloseValue,Schnitt) VALUES(?,?,?)";

        try {

            Connection conn = this.connection(urlDB);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, datum);
            pstmt.setDouble(2, closeValue);
            pstmt.setDouble(3, gleitenderMittelwert);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewDatabase(String url) {


        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
public int laengeDB(){
    System.out.print("Wählen Sie den gleitenden Mittelwert aus: ");
    return reader.nextInt();
}

    public void selectDB(String unternehmen, int laenge) {
        String datum;
        double close;
        String sql = "SELECT * from " + unternehmen + " ORDER BY DateofValue DESC";

        try {
            Connection con = this.connection(urlDB);
            Statement stmt = connection(urlDB).createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {

                System.out.println(
                        rs.getString("DateofValue") + "\t" + "\t" +
                                rs.getDouble("CloseValue") + "\t" + "\t" +
                                rs.getDouble("Schnitt") + "\t" + "\t");
                        //closeWerte.add(rs.getDouble("CloseValue"));
                //100 kann auch ersetzt werden(Menge der Daten in javaFX)
            if(readDataFromDB.size()<=100) {
                datum = rs.getString("DateofValue");
                close = rs.getDouble("CloseValue");
                rs.getDouble("Schnitt");

                readDataFromDB.put(datum,close);
            }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public static void calcMittelwert(int laenge){
        int count = 0;
        double addierteZahlen = 0;
        double m;

        for(int i = 0; i <= closeWerte.size()-1;i++){
            count = count +1;
            if(count<=laenge){
                addierteZahlen = addierteZahlen+ closeWerte.get(i);
                gleitender_Mittelwert.add(addierteZahlen/count);
            }
            if(count > laenge){
                m = closeWerte.get(i-laenge);
                addierteZahlen = addierteZahlen -m;
                addierteZahlen = addierteZahlen +closeWerte.get(i);
                gleitender_Mittelwert.add(addierteZahlen/laenge);
            }

        }

    }

    public void getAvgWithData(String unternehmen, int laenge) {
        String datum;
        double avgValue;
        String sql = "SELECT * from " + unternehmen + " ORDER BY DateofValue DESC";

        try {
            Connection con = this.connection(urlDB);
            Statement stmt = connection(urlDB).createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {


                //closeWerte.add(rs.getDouble("CloseValue"));
                //100 kann auch ersetzt werden(Menge der Daten in javaFX)
                if(avgAndData.size()<=100) {
                    datum = rs.getString("DateofValue");
                    avgValue = rs.getDouble("Schnitt");


                    avgAndData.put(datum,avgValue);
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void outputsize(String unternehmen){
        System.out.print("Möchten sie die volle oder Kompakte version des Unternehmens:(v/c)");
        char antwort = reader.next().charAt(0);
        if(antwort == 'v'){

            urlDB ="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + unternehmen + "&outputsize=full&apikey=T5PECTJY4ZNB1WPA";
        }else{
            urlDB ="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + unternehmen + "&outputsize=compact&apikey=T5PECTJY4ZNB1WPA";
        }
    }


    public static String graphsize(String unternehmen){
        char answer;
        int amount;
        String sql;
        int amount1 = gleitender_Mittelwert.size();
        //bestimmt sql eingabe für länge de sgraphen
        System.out.println("");
        System.out.println("Wollen Sie Aktientage ausgeben oder eine bestimmte Anzahl der letzten Tage?");
        System.out.println("Alle... A");
        System.out.println("Bestimmte Anzahl... B");
        System.out.print("Ihre Wahl: ");
         answer = reader.next().toLowerCase().charAt(0);
        if(answer == 'b'){

            System.out.print("Wie viele Werte möchten Sie ausgeben lassen: ");
            amount = reader.nextInt();
           // return "SELECT * from " + unternehmen + " order by DateOfValue asc limit "+"sum((select count(DateofValue) from "+unternehmen+")"+ " - "+ amount +"),"+ amount;
           // return "SELECT * from " + unternehmen + " order by DateOfValue asc limit "+"select(sum((select count(DateofValue) from "+unternehmen+")"+ " - "+ amount +")from "+unternehmen+"),"+ amount;
            //Select * from TSLA order by DateofValue asc limit sum((select count(DateofValue) from TSLA)-amount),amount
            //Select * from TSLA order by DateofValue asc limit (select sum((select count(DateofValue) from TSLA)-amount)from TSLA),amount
            return "Select * from "+ unternehmen + " order by DateofValue asc limit ("+amount1+" - " +amount+ "), "+amount;

        }else{
            return "SELECT * from " + unternehmen + " order by DateofValue asc ";
        }

    }

}
