package Main;

import Methods.API;
import Methods.DataBase;
import Methods.OtherMethods;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program extends Application {

    public static ArrayList<String> stocksToRead = new ArrayList<String>();
    public static String unterName;
    public static void main(String[] args) throws IOException, SQLException {
     unterName = OtherMethods.getUnternhemenname();
        API.connectToAPI(unterName);
        DataBase.connectToDB();
        DataBase.createTable(unterName);
        DataBase.insert(unterName);

        DataBase.selectDB(unterName);
        OtherMethods.calc200Schnitt();
        DataBase.updateAddSchnitt(unterName);
        System.out.println(DataBase.readDateFromDB);
        OtherMethods.readStocksFromFile(stocksToRead);
        System.out.println(stocksToRead);



launch(args);
    }
    @Override
public  void start(Stage stage) throws Exception{
        stage.setTitle("Stock Monitoring");

        final CategoryAxis date = new CategoryAxis();
        final NumberAxis close = new NumberAxis();
        date.setLabel("Number of Month");
        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(date, close);

        lineChart.setTitle("Stock Monitoring of " + unterName);
        //defining a series
        XYChart.Series graph = new XYChart.Series();
        XYChart.Series mittelwert = new XYChart.Series();
        //populating the series with data
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.setName("Aktien von " + unterName);
        series2.setName("200er Schnitt von " + unterName);
        Connection conn = DriverManager.getConnection(DataBase.dbURL,"root","ABC13Y@12Bz");
        List<Double> closeWerte1 = new ArrayList<Double>();
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from " +unterName+ " order by DateofValue ASC");

            while(rs.next()){
                series1.getData().add(new XYChart.Data(rs.getString("DateOfValue"), Float.parseFloat(rs.getString("CloseValue"))));
                series2.getData().add(new XYChart.Data(rs.getString("DateOfValue"), Float.parseFloat(rs.getString("Schnitt"))));
                closeWerte1.add(Double.parseDouble(rs.getString("CloseValue")));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        //sdfadsfasedf
        double avg=0;
        double cl=0;
        //dsfgsedrfgsdfgsdkomischer SHitr 93-105

        String css;
        Scene scene = new Scene(lineChart,1500,900);


        if(avg<cl){
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


        lineChart.getData().addAll(series1,series2);

        lineChart.setCreateSymbols(false);

        stage.setScene(scene);
        stage.show();


    }
}
