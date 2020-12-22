import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public class Main extends Application {

    public  static String unternehmen = Methode.unternehmenName();
    public static void main(String[] args) throws IOException, JSONException {
        String datum = "";
        double closeValue = 0.0;
        String cw;
        Methode m = new Methode();

        m.connectToAPI(unternehmen);
        m.connectDB(m.urlDB);
        m.createNewDatabase(m.urlDB);
        m.createTableofDB(m.urlDB, unternehmen);
        for (int i = 0; i < Methode.dates.size(); i++) {
            datum = m.dates.get(i).toString();
            cw = m.HMzwischenspeicher.get(m.dates.get(i));
            closeValue = Double.valueOf(cw);
            m.insert(unternehmen, datum, closeValue);
        }
        int lange = m.laengeDB();
        m.selectDB(unternehmen, lange);
        //avg geht absolut nit
        m.averageDB(unternehmen,lange);
        launch(args);







    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final CategoryAxis date = new CategoryAxis();
        final NumberAxis close = new NumberAxis();
        date.setLabel("Number of Month");
        //creating the chart
        final LineChart<String,Number> lineChart =
                new LineChart<String,Number>(date,close);

        lineChart.setTitle("Stock Monitoring of "+ unternehmen);
        //defining a series
        XYChart.Series graph = new XYChart.Series();
        XYChart.Series mittelwert = new XYChart.Series();
        graph.setName("Aktien von " + unternehmen);
        mittelwert.setName("xxx'er Schnitt von " + unternehmen);
        //populating the series with data

        for(Map.Entry e : Methode.readDataFromDB.entrySet()){
            graph.getData().add(new XYChart.Data(e.getKey(), e.getValue()));
        }

        for(Map.Entry e : Methode.readDataFromDB.entrySet()){
            mittelwert.getData().add(new XYChart.Data(e.getKey(), Methode.average));
        }
       lineChart.setCreateSymbols(false);

        Scene scene  = new Scene(lineChart,1500,900);
        lineChart.getData().add(graph);
        lineChart.getData().add(mittelwert);

        stage.setScene(scene);
        stage.show();
    }
}

