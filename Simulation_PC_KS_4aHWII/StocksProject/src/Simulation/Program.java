package Simulation;

import Methods.DataBase;
import Methods.OtherMethods;
import com.mysql.cj.protocol.Resultset;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
enum StockStrats{
    Hold,Cycle,Cycle3,
}
public class Program {
    //DataBase Stuff
    public static String dbURL = "jdbc:mysql://localhost:3306/stocks?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static Connection connection;
    public static Statement myStmt;

    //  Values
    public static String selectedStock;
    public static LocalDate StartDateMinusOne;
    public static double startMoney;
    public static LocalDate startDate;
    private static Scanner reader = new Scanner(System.in);
    public static ArrayList<String> allStocks = new ArrayList<String>();

    //start
    public static void main(String[] args) throws SQLException, IOException {

        connectToDB();
        char choise = choiseMethod();
        System.out.println(choise);
        if(choise =='a'){

            InputMultipleStocks();
            TableSetupAll(allStocks);
            executeMultipleStocksAllStrategies(allStocks);
            outputAllStocks(allStocks);
        }else if(choise=='e'){

            input();
            TableSetup();
            buyAndHold(selectedStock, startMoney, startDate,StockStrats.Hold);
            cycleSimulationNormal(selectedStock,StockStrats.Cycle);
            cycleSimulationWithPercent(selectedStock,StockStrats.Cycle3);
            outPut();
        }





    }

    //Methods
    public static void input() throws IOException {
        boolean negative = true;

        OtherMethods.readStocksFromFile(allStocks);
        System.out.println("Available shares:" + allStocks);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        //Aus Textfile verfügbare Aktien auslesen
        System.out.print("Your choice: ");
        selectedStock = reader.next().toLowerCase();
        do{
            System.out.print("Seed capital[$]: ");
            startMoney = reader.nextDouble();
            if(startMoney > 0 ){
                negative = false;
            }
        }while(negative == true);

        System.out.print("Start-date [dd-MM-yyyy]: ");
        String date = reader.next();
        startDate = LocalDate.parse(date,formatter);
        StartDateMinusOne = startDate.minusDays(1);
    }

    public static void createTable(String unternehmen, StockStrats strat) throws SQLException {
        try {
            myStmt = connection.createStatement();
            //true -> 1 -> buy
            //false -> 0 -> sell
            String command = "create Table if not Exists " + unternehmen + "_Sim_" + strat+" (DateOfDay Date primary key, action boolean, depot Double, Money double);";
            myStmt.executeUpdate(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void sillyDbEntry(String unternehmen, double startMoney,StockStrats strat) {
   //     LocalDate sillyDate = LocalDate.parse("1111-11-11");


        String sql = "insert into " + unternehmen + "_Sim_"+strat+" (DateOfDay,action,depot,Money) values(?,?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, StartDateMinusOne.toString());
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

    public static void cycleSimulationNormal(String unternehmen, StockStrats strat) {

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
                if (boughtOrSoldBefore(unternehmen,strat)) {
                    //true --> habe vorher gekauft -> will verkaufen
                    if (close < schnitt) {
                        double sBefore = stocksBefore(unternehmen,strat);
                        double mBefore = moneyBefore(unternehmen,strat);
                        double moneyAfterSell = mBefore + (close * sBefore);
                        buyOrSellInsert(unternehmen, date, 0.0, moneyAfterSell,strat);

                    }
                } else {
                    //false --> habe vorher verkauft -> will kaufen
                    if (close > schnitt && !date.equals(lastDay(unternehmen))) {
                        //insert und rechnung mit kaufen
                        double mbefore = moneyBefore(unternehmen,strat);
                        int roundedNumber = (int) (mbefore / close);
                        double stocksNow = roundedNumber;
                        double moneyleft = mbefore % close;
                        buyOrSellInsert(unternehmen, date, stocksNow, moneyleft,strat);
                    }
                }
                if(date.equals(lastDay(unternehmen))){
                    if(boughtOrSoldBefore(unternehmen,strat)){
                        double sBefore = stocksBefore(unternehmen,strat);
                        double mBefore = moneyBefore(unternehmen,strat);
                        double moneyAfterSell = mBefore + (close * sBefore);
                        buyOrSellInsert(unternehmen, date, 0.0, moneyAfterSell,strat);
                    }

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void buyAndHold(String unt, double sm, LocalDate sd,StockStrats strat) {
        double close = 0;
        double close2 = 0;
        String date1 = "", date2 = "";

        String sql = "Select * from " + unt + " where DateOfValue >= '" + sd + "' order by DateOfValue asc limit 1";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Erster Tag -- Buy
            while (rs.next()) {
                close = rs.getDouble("CloseValue");
                date1 = rs.getString("DateofValue");
            }
            int roundedNumber = (int) (sm / close);
            double depot = roundedNumber;
            double moneyRest = sm % close;
            //Insert
            buyOrSellInsert(unt, date1, depot, moneyRest,strat);

            String sql2 = "Select * from " + unt + " order by DateOfValue desc limit 1";
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql2);

            //Letzter Tag-- Sell
            while (rs2.next()) {
                close2 = rs2.getDouble("CloseValue");
                date2 = rs2.getString("DateofValue");
            }

            //Insert
            buyOrSellInsert(unt, date2, 0.0, depot * close2,strat);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void buyOrSellInsert(String unt, String dateAtMoment, double stocks, double newMoney,StockStrats strat) {
        String sql = "insert into " + unt + "_Sim_"+strat+" (DateOfDay,action,depot,Money) values(?,?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, dateAtMoment.toString());
            pstmt.setBoolean(2, !boughtOrSoldBefore(unt,strat));
            pstmt.setDouble(3, stocks);
            pstmt.setDouble(4, newMoney);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean boughtOrSoldBefore(String unt,StockStrats strat) {
        boolean bOs = false;
        String sql = "Select * from " + unt + "_Sim_" +strat+ " order by DateOfDay desc limit 1";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bOs = rs.getBoolean("action");
            }
            return bOs;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    public static double stocksBefore(String unt,StockStrats strat) {
        double stocks = 0.0;
        String sql = "Select * from " + unt + "_Sim_" +strat+ " order by DateOfDay desc limit 1";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                stocks = rs.getDouble("depot");
            }
            return stocks;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0.0;

    }

    public static double moneyBefore(String unt,StockStrats strat) {
        double money = 0.0;
        String sql = "Select * from " + unt + "_Sim_" +strat+ " order by DateOfDay desc limit 1";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                money = rs.getDouble("Money");
            }
            return money;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0.0;
    }

    public static String lastDay(String unt) {
        String day = "";
        String sql = "Select * from " + unt + " order by DateofValue desc limit 1";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                day = rs.getString("DateofValue");
            }
            return day;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int whichMethod() {
        System.out.println("Welche Simulation wollen Sie Starten:");
        System.out.println("Buy and Hold...... 1");
        System.out.println("200er Schnitt strategie.......2");
        System.out.println("200er Schnitt Strategie mit 3% Toleranz..... 3");
        System.out.print("Ihre Wahl: ");
        return reader.nextInt();

    }

    public static void cycleSimulationWithPercent(String unternehmen,StockStrats strat) {

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
                if (boughtOrSoldBefore(unternehmen,strat)) {
                    //true --> habe vorher gekauft -> will verkaufen
                    if (close < schnitt -(schnitt*0.03)) {
                        double sBefore = stocksBefore(unternehmen,strat);
                        double mBefore = moneyBefore(unternehmen,strat);
                        double moneyAfterSell = mBefore + (close * sBefore);
                        buyOrSellInsert(unternehmen, date, 0.0, moneyAfterSell,strat);

                    }
                } else {
                    //false --> habe vorher verkauft -> will kaufen
                    if (close > schnitt+(schnitt*0.03) && !date.equals(lastDay(unternehmen))) {
                        //insert und rechnung mit kaufen
                        double mbefore = moneyBefore(unternehmen,strat);
                        int roundedNumber = (int) (mbefore / close);
                        double stocksNow = roundedNumber;
                        double moneyleft = mbefore % close;
                        buyOrSellInsert(unternehmen, date, stocksNow, moneyleft,strat);
                    }
                }
                if(date.equals(lastDay(unternehmen))){
                    if(boughtOrSoldBefore(unternehmen,strat)){
                        double sBefore = stocksBefore(unternehmen,strat);
                        double mBefore = moneyBefore(unternehmen,strat);
                        double moneyAfterSell = mBefore + (close * sBefore);
                        buyOrSellInsert(unternehmen, date, 0.0, moneyAfterSell,strat);
                    }

                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void calcProfit(String unt,StockStrats strat){
        String sql = "select * from "+unt+"_sim_"+strat+" order by DateOfDay asc limit 1";
        double moneybefore=0,moneyafter=0;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                moneybefore = rs.getDouble("Money");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        String sql2 = "select * from "+unt+"_sim_"+strat+" order by DateOfDay desc limit 1";
        try{
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql2);
            while(rs2.next()){
                moneyafter = rs2.getDouble("Money");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println("Nach der Simulation von "+usedStratName(strat)+" hast du "+(moneyafter/moneybefore*100)+ " % vom Startkapital");
        System.out.println("Vorher: "+moneybefore + " |  Danach: "+moneyafter);
        System.out.println("");

    }
    public static String usedStratName(StockStrats s){
        switch (s){
            case Hold:
                return "Buy and Hold";
            case Cycle:
                return "200er Strategie";
            case Cycle3:
                return "200er Strategie mit 3% Toleranz";
        }
        return "Error";
    }

    public static void TableSetup() throws SQLException {
        createTable(selectedStock,StockStrats.Hold);
        createTable(selectedStock,StockStrats.Cycle);
        createTable(selectedStock,StockStrats.Cycle3);
        sillyDbEntry(selectedStock, startMoney,StockStrats.Hold);
        sillyDbEntry(selectedStock, startMoney,StockStrats.Cycle);
        sillyDbEntry(selectedStock, startMoney,StockStrats.Cycle3);
    }

    public static void outPut(){
        calcProfit(selectedStock,StockStrats.Hold);
        calcProfit(selectedStock,StockStrats.Cycle);
        calcProfit(selectedStock,StockStrats.Cycle3);
    }

    public static void InputMultipleStocks() throws IOException {
        boolean negative = false;
        OtherMethods.readStocksFromFile(allStocks);
        System.out.println("Selected Shares:" + allStocks);
        do{


        System.out.print("Available Money[$]: ");
        startMoney = reader.nextDouble();
        if(startMoney>0){
            negative = true;
        }
        }while(negative == false);
        startMoney =startMoney/allStocks.size();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.print("Start Date[dd/MM/yyyy]:");
        String date = reader.next();
        startDate =  LocalDate.parse(date,formatter);
        StartDateMinusOne = startDate.minusDays(1);
    }

    public  static void calcProfitFromAll(ArrayList<String> Stocks, StockStrats strat){
        double money=0, wholeMoneyTogether=0;
        for(String i : Stocks){


        String sql = "select * from "+i+"_sim_"+strat+" order by DateOfDay desc limit 1";

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                money = rs.getDouble("Money");
            }

        }catch(SQLException e){
            e.printStackTrace();

        }
        wholeMoneyTogether = wholeMoneyTogether + money;

        }
          NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        int AmountOfStocks = Stocks.size();
        System.out.println("Nach der Simulation von "+usedStratName(strat)+" mit aufgeteiltes Geld auf den Aktien hast du "+n.format((wholeMoneyTogether/(startMoney*AmountOfStocks))*100)+ " % vom Startkapital");
        System.out.println("Vorher: "+startMoney*Stocks.size()+"  | Nachher: "+n.format(wholeMoneyTogether));
        System.out.println("");

    }

    public static void executeMultipleStocksAllStrategies(ArrayList<String> Stocks){
        for (String i : Stocks)
        {
            buyAndHold(i, startMoney, startDate,StockStrats.Hold);
            System.out.println(i);
            cycleSimulationNormal(i,StockStrats.Cycle);
            cycleSimulationWithPercent(i,StockStrats.Cycle3);
        }
//    buyAndHold(Stocks.get(i), startMoney, startDate,StockStrats.Hold);
  //  cycleSimulationNormal(Stocks.get(i),StockStrats.Cycle);
    //cycleSimulationWithPercent(Stocks.get(i),StockStrats.Cycle3);
}



    public static void TableSetupAll(ArrayList<String> Stocks) throws SQLException {
for(String i: Stocks){
    createTable(i,StockStrats.Hold);
    createTable(i,StockStrats.Cycle);
    createTable(i,StockStrats.Cycle3);
    sillyDbEntry(i, startMoney,StockStrats.Hold);
    sillyDbEntry(i, startMoney,StockStrats.Cycle);
    sillyDbEntry(i, startMoney,StockStrats.Cycle3);

}
    }

    public static void outputAllStocks(ArrayList<String> Stocks){

        calcProfitFromAll(Stocks,StockStrats.Hold);
        calcProfitFromAll(Stocks,StockStrats.Cycle);
        calcProfitFromAll(Stocks,StockStrats.Cycle3);

    }

    public static char choiseMethod(){
        boolean corr=false;
        System.out.println("Alle Aktien aufgeteiltmit alle Strategien oder eine Aktie mit allen Strategien: ");
        System.out.println("Alle......a");
        System.out.println("Eine......e");
        char choise ='y';
        do {
            System.out.print("Entscheidung:");
            choise = reader.next().toLowerCase().charAt(0);
            choise= choise;
            if(choise == 'a'){
               return 'a';
            }else if(choise=='e'){
               return 'e';
            }
        }while(corr == false);

return 'y';
    }
}

