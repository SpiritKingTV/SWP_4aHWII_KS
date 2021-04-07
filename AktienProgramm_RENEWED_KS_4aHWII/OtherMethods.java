package Methods;

import Main.Program;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class OtherMethods {
    public static ArrayList<Double> Schnitt200er = new ArrayList<Double>();
    public static HashMap<Double, Double> CloseAndSchnitt = new HashMap<>();
    public static String getUnternhemenname(){
        Scanner reader = new Scanner(System.in);
        System.out.print("Geben Sie das Kürzel der Aktiengesellschaft ein: ");
        return reader.next();
    }

    public static void calc200Schnitt(){
        int count =0;
        double addierteZahlen =0;
        double m;
        for(int i=0;i<=DataBase.readCloseFromDB.size()-1;i++){
            count = count+1;
            if(count<= 200){
               addierteZahlen = addierteZahlen+ DataBase.readCloseFromDB.get(i);
               Schnitt200er.add(addierteZahlen/count);


            }
            if(count>200){
                m = DataBase.readCloseFromDB.get(i-200);
                addierteZahlen = addierteZahlen-m;
                addierteZahlen = addierteZahlen+DataBase.readCloseFromDB.get(i);
                Schnitt200er.add(addierteZahlen/200);

            }
        }

    }


    public static void readStocksFromFile (ArrayList<String> stocks) throws IOException {
        String stocksName;
        try{
            BufferedReader bfr = new BufferedReader(new FileReader("E:/Plugins dev/StocksProject/src/Methods/Aktien.txt"));

            while((stocksName = bfr.readLine())!= null){
                stocks.add(stocksName);

            }
            bfr.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }


    public static void clearAllLists(){
        DataBase.readCloseFromDB.clear();
        DataBase.readDateAndCloseDB.clear();
        DataBase.readDateFromDB.clear();
        API.dates.clear();
        API.HMDatesAndValues.clear();
        OtherMethods.Schnitt200er.clear();
        OtherMethods.CloseAndSchnitt.clear();


    }
    public static String readAPIKeyFromFile () throws IOException {
        String a;
        String apikey = "";
        try{
            BufferedReader bfr = new BufferedReader(new FileReader("E:/Plugins dev/StocksProject/src/Methods/APIKey.txt"));

            while((a = bfr.readLine())!= null){
                apikey = a;


            }
            bfr.close();
            return apikey;
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }

    }
}
