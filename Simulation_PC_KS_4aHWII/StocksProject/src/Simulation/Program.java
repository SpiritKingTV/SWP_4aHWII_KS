package Simulation;

import Methods.DataBase;
import Methods.OtherMethods;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Program {
    //DataBase Stuff
    public static String dbURL = "jdbc:mysql://localhost:3306/stocks?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static Connection connection;
    public static Statement myStmt;

    //  Values
    public static String selectedStock;
    public static double startMoney;
    public static LocalDate startDate;
    private static Scanner reader = new Scanner(System.in);
    public static ArrayList<String> allStocks = new ArrayList<String>();

    //start
    public static void main(String[] args) throws SQLException, IOException {

        connectToDB();
        input();
        createTable(selectedStock);
        sillyDbEntry(selectedStock, startMoney);
        buyAndHold(selectedStock,startMoney,startDate);
       // cycleSimulationNormal(selectedStock);




    }

    //Methods
    public static void input() throws IOException {
        OtherMethods.readStocksFromFile(allStocks);
        System.out.println("Available shares:" + allStocks);

        //Aus Textfile verfügbare Aktien auslesen
        System.out.print("Your choice: ");
        selectedStock = reader.next().toLowerCase();
        System.out.print("Seed capital: ");
        startMoney = reader.nextDouble();
        System.out.print("Start-date [yyyy-mm-dd]: ");
        startDate = LocalDate.parse(reader.next());
    }

    public static void createTable(String unternehmen) throws SQLException {
        try {
            myStmt = connection.createStatement();
            //true -> 1 -> buy
            //false -> 0 -> sell
            String command = "create Table if not Exists " + unternehmen + "_Sim" + "(DateOfDay Date primary key, action boolean, depot Double, Money double);";
            myStmt.executeUpdate(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void sillyDbEntry(String unternehmen, double startMoney) {
        LocalDate sillyDate = LocalDate.parse("1111-11-11");


        String sql = "insert into " + unternehmen + "_Sim(DateOfDay,action,depot,Money) values(?,?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, sillyDate.toString());
            pstmt.setBoolean(2, Boolean.parseBoolean("false"));
            pstmt.setDouble(3, 0);
            pstmt.setDouble(4, startMoney);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void connectToDB() throws SQLException {
        try {
            connection = DriverManager.getConnection(dbURL, "root", "ABC13Y@12Bz");
            myStmt = connection.createStatement();
            System.out.println("Database Connected");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void cycleSimulationNormal(String unternehmen) {

        double close, schnitt;
        String date;
        String sql = "Select * from " + unternehmen + " where DateofValue >= '" + startDate + "' order by DateOfValue asc";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Tagesdurchlauf
            while (rs.next()) {
                close = rs.getDouble("CloseValue");
                date = rs.getString("DateofValue");
                schnitt = rs.getDouble("Schnitt");
            if (boughtOrSoldBefore(unternehmen)){
                //true --> habe vorher gekauft -> will verkaufen
                if(close<schnitt){
                    double sBefore = stocksBefore(unternehmen);
                    double mBefore = moneyBefore(unternehmen);
                    double moneyAfterSell = mBefore + (close*sBefore);
                    buyOrSellInsert(unternehmen,date,0.0,moneyAfterSell);

                }
            }else{
                //false --> habe vorher verkauft -> will kaufen
                if(close>schnitt&& !date.equals(lastDay(unternehmen))){
                    //insert und rechnung mit kaufen
                    double mbefore = moneyBefore(unternehmen);
                    int roundedNumber = (int)(mbefore/close);
                    double stocksNow = roundedNumber;
                    double moneyleft = mbefore%close;
                    buyOrSellInsert(unternehmen,date,stocksNow,moneyleft);
                }
            }
          /*  if(date.equals(lastDay(unternehmen))&& date.equals(lastDay(unternehmen))){
                if (stocksBefore(unternehmen)!= 0) {
                    double sBefore = stocksBefore(unternehmen);
                    double mBefore = moneyBefore(unternehmen);
                    double moneyAfterSell = mBefore + (close*sBefore);
                    buyOrSellInsert(unternehmen,date,0.0,moneyAfterSell);
                }
            }
*/
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void buyAndHold(String unt, double sm, LocalDate sd) {
        double close=0;
        double close2=0;
        String date1="",date2="";

        String sql = "Select * from " + unt + " where DateOfValue >= '" + sd + "' order by DateOfValue asc limit 1";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Erster Tag -- Buy
            while (rs.next()) {
                close = rs.getDouble("CloseValue");
                date1 = rs.getString("DateofValue");
            }
            int roundedNumber = (int)(sm/close);
            double depot = roundedNumber;
            double moneyRest = sm%close;
            //Insert
            buyOrSellInsert(unt,date1,depot,moneyRest);

            String sql2 = "Select * from " + unt + " order by DateOfValue desc limit 1";
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql2);

            //Letzter Tag-- Sell
            while(rs2.next()){
                close2 = rs2.getDouble("CloseValue");
                date2 = rs2.getString("DateofValue");
            }

            //Insert
            buyOrSellInsert(unt,date2,0.0,depot*close2);

        } catch (SQLException e) {
e.printStackTrace();
        }


    }

    public static void buyOrSellInsert(String unt, String dateAtMoment,double stocks,double newMoney){
        String sql = "insert into " + unt + "_Sim(DateOfDay,action,depot,Money) values(?,?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, dateAtMoment.toString());
            pstmt.setBoolean(2, !boughtOrSoldBefore(unt));
            pstmt.setDouble(3, stocks);
            pstmt.setDouble(4, newMoney);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean boughtOrSoldBefore(String unt){
        boolean bOs = false;
        String sql = "Select * from " + unt +"_Sim "+" order by DateOfDay desc limit 1";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                bOs = rs.getBoolean("action");
            }
            return bOs;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }
    public static double stocksBefore(String unt){
        double stocks = 0.0;
        String sql = "Select * from " + unt +"_Sim "+" order by DateOfDay desc limit 1";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                stocks = rs.getDouble("depot");
            }
            return stocks;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0.0;

    }
    public static double moneyBefore(String unt){
        double money = 0.0;
        String sql = "Select * from " + unt +"_Sim "+" order by DateOfDay desc limit 1";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                money = rs.getDouble("Money");
            }
            return money;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0.0;
    }

    public static String lastDay(String unt){
        String day ="";
        String sql = "Select * from " + unt +" order by DateofValue desc limit 1";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                day = rs.getString("DateofValue");
            }
            return day;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }




public static int whichMethod(){
    System.out.println("Welche Simulation wollen Sie Starten:");
    System.out.println("Buy and Hold...... 1");
    System.out.println("200er Schnitt strategie.......2");
    System.out.println("200er Schnitt Strategie mit 3% Toleranz..... 3");
    System.out.print("Ihre Wahl: ");
    return reader.nextInt();

}
public static void methods(){

        int choise = whichMethod();
        switch (choise){
            case 1:
                buyAndHold(selectedStock,startMoney,startDate);

                break;

            case 2:
                cycleSimulationNormal(selectedStock);

                break;

            case 3:
                System.out.println("Kommt noch");

                break;

            default:
                System.out.println("Falsche eingabe");

                break;

    }

    }

}

