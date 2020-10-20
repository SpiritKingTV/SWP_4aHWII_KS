
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.sql.*;

public class Main extends Application {
    static NodeList dateOH, daysOH;
    static int mon=0,tue=0,wed=0,thu=0,fri =0;
    static int startyear, countyear, yearnow;
    static List<LocalDate> dynamicDates = new ArrayList<>();
    static List<LocalDate> holidayDates = new ArrayList<>();
    static String url;
    static Scanner reader = new Scanner(System.in);
    static String fromTo = "";
    static String Bundesland = "&nur_land=BY";
    static File file = new File("C:\\Users\\repet\\Desktop\\repositories\\SWP\\Ferienrechner mit DB\\src\\holidays.xml");

    public static void main(String[] args) throws IOException {
        importFile();
        anzahlJahre();
        String url = "https://feiertage-api.de/api/?jahr=" + startyear + Bundesland;
        yearnow = startyear;
        JSONObject json = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        fillList(holidayDates);

        for (int i = 0; i < countyear; i++) {

            getDynamicDate(json, "Ostermontag");
            getDynamicDate(json, "Christi Himmelfahrt");
            getDynamicDate(json, "Pfingstmontag");
            getDynamicDate(json, "Fronleichnam");
            yearnow++;
            URLFromYear(yearnow);
        }
        Main db = new Main();
        checkday(dynamicDates,holidayDates);
        output();
        vonBis();
        Application.launch(args);

        db.connectDB();
        db.createNewDatabase();
        db.createTableofDB();
        db.insert();
        db.selectDB();








    }

    public static void URLFromYear(int year) throws MalformedURLException {
        url = ("https://feiertage-api.de/api/?jahr=" + year + Bundesland);
    }

    public static void anzahlJahre() {
        System.out.print("Geben Sie das Startjahr ein: ");
        startyear = reader.nextInt();

        System.out.print("Geben Sie die Anzahl der Jahre ein: ");
        countyear = reader.nextInt();
    }

    private static void getDynamicDate(JSONObject json, String key) {
        JSONObject daten = (JSONObject) json.get(key);
        String date = daten.get("datum").toString();
        LocalDate ldate = LocalDate.parse(date);
        dynamicDates.add(ldate);


    }

    public static void checkday(List<LocalDate> dynamicDates, List<LocalDate> holidaysDates) {
        for (int i = 0; i < holidaysDates.size(); i++) {
            switch (holidaysDates.get(i).getDayOfWeek()) {
                case MONDAY:
                    mon++;
                    break;
                case TUESDAY:
                    tue++;
                    break;
                case WEDNESDAY:
                    wed++;

                    break;
                case THURSDAY:

                    thu++;
                    break;


                case FRIDAY:

                    fri++;
                    break;

            }
        }
        for (int i = 0; i < dynamicDates.size(); i++) {
            switch (dynamicDates.get(i).getDayOfWeek()) {
                case MONDAY:
                    mon++;
                    break;
                case TUESDAY:
                    tue++;
                    break;
                case WEDNESDAY:
                    wed++;

                    break;
                case THURSDAY:

                    thu++;
                    break;


                case FRIDAY:

                    fri++;
                    break;

            }
        }
    }

    public static void importFile() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            dateOH = doc.getElementsByTagName("Date");
            daysOH = doc.getElementsByTagName("Days");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillList(List<LocalDate> holidaysDates){
        for (int i = 0; i < dateOH.getLength(); i++) {
            Node node = dateOH.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                holidaysDates.add(LocalDate.of(startyear, Integer.parseInt(eElement.getElementsByTagName("Month").item(0).getTextContent()), Integer.parseInt(eElement.getElementsByTagName("Day").item(0).getTextContent())));

            }
        }
        for (int y = 1; y < countyear; y++) {
            for (int i = 0; i < dateOH.getLength(); i++) {
                holidaysDates.add(holidaysDates.get(i).plusYears(y));
            }
        }


    }

    public static void output(){

        System.out.println("Montage: " + mon);
        System.out.println("Dienstage: "+ tue);
        System.out.println("Mittwoche: " + wed);
        System.out.println("Donnerstage: " + thu);
        System.out.println("Freitage: "+ fri);
    }
    /*
        public static void start(Stage primaryStage) throws Exception{
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Wochentag");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Feiertage");


            //BarChart erstellen
            BarChart<String, Number> barChart = new BarChart<String,Number>(xAxis,yAxis);

            XYChart.Series<String,Number> strang = new XYChart.Series<String,Number>();
            //strang.setName("Montag");
            strang.getData().add(new XYChart.Data<String,Number>("Montag",mon));
            strang.getData().add(new XYChart.Data<String,Number>("Dienstag",tue));
            strang.getData().add(new XYChart.Data<String,Number>("Mittwoch",wed));
            strang.getData().add(new XYChart.Data<String,Number>("Donnerstag",thu));
            strang.getData().add(new XYChart.Data<String,Number>("Freitag",fri));

            barChart.getData().add(strang);


            barChart.setTitle("Feiertagberechner von "+ startyear+" über " + countyear + " Jahre");

            VBox vbox = new VBox(barChart);

            primaryStage.setTitle("Grafik über Anzahl der Feiertage");
            Scene scene = new Scene(vbox, 400, 200);

            primaryStage.setScene(scene);
            primaryStage.setHeight(300);
            primaryStage.setWidth(400);

            primaryStage.show();

        }
    */
    @Override
    public void start(Stage primaryStage) throws Exception {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Wochentag");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Feiertage");


        //BarChart erstellen
        BarChart<String, Number> barChart = new BarChart<String,Number>(xAxis,yAxis);

        XYChart.Series<String,Number> strang = new XYChart.Series<String,Number>();
        //strang.setName("Montag");
        strang.getData().add(new XYChart.Data<String,Number>("Montag",mon));
        strang.getData().add(new XYChart.Data<String,Number>("Dienstag",tue));
        strang.getData().add(new XYChart.Data<String,Number>("Mittwoch",wed));
        strang.getData().add(new XYChart.Data<String,Number>("Donnerstag",thu));
        strang.getData().add(new XYChart.Data<String,Number>("Freitag",fri));

        barChart.getData().add(strang);


        barChart.setTitle("Feiertagberechner von "+ fromTo);

        VBox vbox = new VBox(barChart);

        primaryStage.setTitle("Grafik über Anzahl der Feiertage");
        Scene scene = new Scene(vbox, 700, 300);

        primaryStage.setScene(scene);


        primaryStage.show();
    }

    public static void connectDB() {
        Connection conn = null;
        try {

            String url = "jdbc:sqlite:C:\\Users\\repet\\Desktop\\repositories\\SWP\\Ferienrechner mit DB\\Database.db";

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

  /*  public static void createDB(String fileName) {

        String url = "jdbc:sqlite:C:/sqlite/" + fileName;

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
*/

    public static void createTableofDB() {
        String url = "jdbc:sqlite:C:\\Users\\repet\\Desktop\\repositories\\SWP\\Ferienrechner mit DB\\Database.db";

        String sql = "CREATE TABLE IF NOT EXISTS Feiertage (\n"
                + " fromTo text not null,\n"
                + " montag integer,\n"
                + " dienstag integer,\n"
                + " mittwoch integer,\n"
                + " donnerstag integer,\n"
                + " freitag integer)";


        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connection() {

        String url = "jdbc:sqlite:C:\\Users\\repet\\Desktop\\repositories\\SWP\\Ferienrechner mit DB\\Database.db";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public void insert() {
        String sql = "INSERT INTO Feiertage(fromTo,montag, dienstag, mittwoch, donnerstag, freitag) VALUES(?,?,?,?,?,?)";

        try{

            Connection conn = this.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fromTo);
            pstmt.setInt(2, mon );
            pstmt.setInt(3, tue );
            pstmt.setInt(4, wed );
            pstmt.setInt(5,thu );
            pstmt.setInt(6, fri);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewDatabase() {

        String url = "jdbc:sqlite:C:\\Users\\repet\\Desktop\\repositories\\SWP\\Ferienrechner mit DB\\Database.db";

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

    public static void vonBis(){
        //startyear = startjahr
        //Countyear = anzahl JAhre
        //Wenn startyear 2020 und COuntyear 1 ist, ist es nur 2020
        //Bei 2 wäre es 2020 - 2021 usw.

        if(countyear == 1){
            fromTo = "01.01."+ startyear + " - 31.12." +startyear;
        }else if(countyear >1){
            fromTo =  "01.01."+ startyear + " - 31.12." +(startyear+(countyear-1));
        }

    }

    public void selectDB(){
        String sql = "SELECT * from Feiertage";

        try{
            Connection con = this.connection();
            Statement stmt = connection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){

                System.out.println(
                                rs.getString("fromTo") + "\t" + "\t" +
                                rs.getInt("montag") + "\t" + "\t" +
                                rs.getInt("dienstag") + "\t" + "\t" +
                                rs.getInt("mittwoch") + "\t" + "\t" +
                                rs.getInt("Donnerstag") + "\t" + "\t" +
                                rs.getInt("Freitag") + "\t" + "\t");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
