import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Methode {
    static String urlDB ="jdbc:sqlite:C:\\Users\\repet\\IdeaProjects\\AktienProjekt\\Database.db";
public static ArrayList <LocalDate> dates = new ArrayList<LocalDate>();
    public  static HashMap<LocalDate, String> HMzwischenspeicher = new HashMap<>();
   // public static HashMap<LocalDate, String> HMsortiert;


   public static  Scanner reader = new Scanner(System.in);


    public static void connectToAPI(String unternehmen) throws IOException, JSONException {

        String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+ unternehmen + "&apikey=T5PECTJY4ZNB1WPA"));
        JSONObject obj = new JSONObject(link);
        JSONObject daily = obj.getJSONObject("Time Series (Daily)");
        //System.out.print(daily);
        for(int i = 0;i<50;i++){
            System.out.print(daily.names().get(i)+ ": close= ");
            dates.add(LocalDate.parse((CharSequence) daily.names().get(i)));

            HMzwischenspeicher.put(LocalDate.parse((CharSequence) daily.names().get(i)),daily.getJSONObject(daily.names().getString(i)).get("4. close").toString());

            System.out.println(daily.getJSONObject(daily.names().getString(i)).get("4. close"));

            System.out.println();
        }
        dates.sort(null);


    }

/*    public static void sortAPIentries(){
      for(int i = 0;i<dates.size();i++){
          HMsortiert.put(dates.get(i),HMzwischenspeicher.get(dates.get(i)));
      }
    }
*/
    public static String unternehmenName(){
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

    public static void createTableofDB(String url, String unternehmen ) {


        String sql = "CREATE TABLE IF NOT EXISTS "+unternehmen +" (\n"
                + " DateOfValue text PRIMARY KEY,\n"
                + " CloseValue text)";


        try{
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

    public void insert(String unternehmen,String datum, String closeValue) {
        String sql = "INSERT INTO "+unternehmen + "(DateOfValue,CloseValue) VALUES(?,?)";

        try{

            Connection conn = this.connection(urlDB );
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, datum);
            pstmt.setString(2, closeValue );
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


    public  void selectDB(String unternehmen){
        String sql = "SELECT * from " +unternehmen;

        try{
            Connection con = this.connection(urlDB );
            Statement stmt = connection(urlDB ).createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){

                System.out.println(
                        rs.getString("DateOfValue") + "\t" + "\t" +
                                rs.getDouble("CloseValue") + "\t" + "\t");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }




}
