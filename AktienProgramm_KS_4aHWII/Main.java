import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends Application {
    public static int lange;
    public static String unternehmen = Methode.unternehmenName();
    public static String sql_graph;

    public static void main(String[] args) throws IOException, JSONException {
        String datum = "";
        double closeValue = 0.0;
        String cw;
        double avg;
        Methode m = new Methode();

        m.connectToAPI(unternehmen);
        m.connectDB(m.urlDB);
        m.sortAPIentries();
        m.createNewDatabase(m.urlDB);
        m.createTableofDB(m.urlDB, unternehmen);

        lange = m.laengeDB();
        System.out.println(lange);
        m.calcMittelwert(lange);
        for (int i = 0; i < Methode.dates.size(); i++) {
            datum = m.dates.get(i).toString();
            cw = m.HMzwischenspeicher.get(m.dates.get(i));
            closeValue = Double.valueOf(cw);
            avg = m.gleitender_Mittelwert.get(i);
            m.insert(unternehmen, datum, closeValue, avg);

        }

        m.selectDB(unternehmen, lange);
        m.getAvgWithData(unternehmen, lange);
        //avg geht absolut nit
       sql_graph = m.graphsize(unternehmen);

        launch(args);


    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Stock Monitoring");
        //defining the axes
        final CategoryAxis date = new CategoryAxis();
        final NumberAxis close = new NumberAxis();
        date.setLabel("Number of Month");
        //creating the chart
        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(date, close);

        lineChart.setTitle("Stock Monitoring of " + unternehmen);
        //defining a series
        XYChart.Series graph = new XYChart.Series();
        XYChart.Series mittelwert = new XYChart.Series();

        //populating the series with data
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.setName("Aktien von " + unternehmen);
        series2.setName(lange + "er Schnitt von " + unternehmen);
        Connection conn = DriverManager.getConnection(Methode.urlDB);
        List<Double> closeWerte1 = new ArrayList<Double>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_graph);

            while (rs.next()) {
                series1.getData().add(new XYChart.Data(rs.getString("DateOfValue"), Float.parseFloat(rs.getString("CloseValue"))));
                series2.getData().add(new XYChart.Data(rs.getString("DateOfValue"), Float.parseFloat(rs.getString("Schnitt"))));
                closeWerte1.add(Double.parseDouble(rs.getString("CloseValue")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
String sql = "select * from " +  unternehmen + " order by DateofValue desc limit 1";
double avg =0;
double cl =0;
try{
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
 while(rs.next()){
     avg = Double.parseDouble(rs.getString("Schnitt"));
     cl = Double.parseDouble(rs.getString("CloseValue"));
 }

}catch(SQLException e){
    System.out.println(e.getMessage());
}
String css;
        Scene scene = new Scene(lineChart, 1500, 900);

if(avg < cl){
    scene.getStylesheets().add("green.css");
}else if(avg>cl){
    scene.getStylesheets().add("red.css");
}else{

}
        close.setAutoRanging(false);
        double topBound = Collections.max(closeWerte1);
        double botBound = Collections.min(closeWerte1);
        close.setLowerBound(botBound-20);
        close.setUpperBound(topBound+20);



    ;


        lineChart.getData().addAll(series1, series2);



            lineChart.setCreateSymbols(false);




            stage.setScene(scene);
            stage.show();
        }
    }



