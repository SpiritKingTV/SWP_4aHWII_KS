package Main;

import Methods.API;
import Methods.DataBase;
import Methods.OtherMethods;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import javax.xml.crypto.Data;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program extends Application {

    public static ArrayList<String> stocksToRead = new ArrayList<String>();
    public static String unterName;
    public static void main(String[] args) throws IOException, SQLException {

   launch(args);


    }
    @Override
public  void start(Stage stage) throws Exception{
        OtherMethods.readStocksFromFile(stocksToRead);
        DataBase.connectToDB();
        for(int i=0;i<stocksToRead.size();i++){
            unterName = stocksToRead.get(i);
            API.connectToAPI(unterName);



            DataBase.createTable(unterName);
            DataBase.insert(unterName);

            DataBase.selectDB(unterName);
            OtherMethods.calc200Schnitt();
            DataBase.updateAddSchnitt(unterName);
           // System.out.println(DataBase.readDateFromDB);
            OtherMethods.clearAllLists();

        stage.setTitle("Stock Monitoring");

        final CategoryAxis date = new CategoryAxis();
        final NumberAxis close = new NumberAxis();
        date.setLabel("Number of Month");
        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(date, close);

        lineChart.setTitle("Stock Monitoring of " + unterName);
        //defining a series

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
            String sql = "select * from " +  unterName + " order by DateofValue desc limit 1";
        double avg=0;
        double cl=0;
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                avg = Double.parseDouble(rs.getString("Schnitt"));
                cl = Double.parseDouble(rs.getString("CloseValue"));

            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        String css;
        Scene scene = new Scene(lineChart,1500,900);


        if(avg<cl){
            scene.getStylesheets().add("green.css");
        }else if(avg>cl){
            scene.getStylesheets().add("red.css");
        }else{
            System.out.println("SERVAS KAISER");

        }
        close.setAutoRanging(false);
        double topBound = Collections.max(closeWerte1);
        double botBound = Collections.min(closeWerte1);
        close.setLowerBound(botBound-20);
        close.setUpperBound(topBound+20);

        lineChart.setAnimated(false);
        lineChart.getData().addAll(series1,series2);

        lineChart.setCreateSymbols(false);




        //foto vcon Stocks
        stage.setScene(scene);
        WritableImage writableImage = scene.snapshot(null);
        File file = new File("E:\\Plugins dev\\StocksProject\\src\\Images\\"+ unterName+"_"+LocalDate.now()+".png");
        ImageIO.write(SwingFXUtils.fromFXImage(writableImage,null),"PNG",file);
            System.out.println(unterName+" completed");
            stage.close();

        }


        System.exit(0);



    }
}
