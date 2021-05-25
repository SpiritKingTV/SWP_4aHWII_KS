package Simulation;

import Methods.DataBase;
import Methods.OtherMethods;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
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
        sillyDbEntry(selectedStock,startMoney,startDate);


    }
//Methods
    public static void input() throws IOException {
        OtherMethods.readStocksFromFile(allStocks);
        System.out.println("Available shares:"+ allStocks);

        //Aus Textfile verfügbare Aktien auslesen
        System.out.print("Your choice: ");
         selectedStock = reader.next().toLowerCase();
        System.out.print("Seed capital: ");
         startMoney = reader.nextDouble();
        System.out.print("Start-date [yyyy-mm-dd]: ");
         startDate = LocalDate.parse(reader.next());
    }

    public static void createTable(String unternehmen)throws SQLException{
        try{
            myStmt = connection.createStatement();
            //true -> 1 -> buy
            //false -> 0 -> sell
            String command = "create Table if not Exists " + unternehmen +"_Sim"+ "(DateOfDay Date primary key, action boolean, depot Double, Money double);";
            myStmt.executeUpdate(command);
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void sillyDbEntry(String unternehmen, double startMoney, LocalDate startDate){
        LocalDate sillyDate = LocalDate.parse("1111-11-11");


        String sql = "insert into "+ unternehmen + "_Sim(DateOfDay,action,depot,Money) values(?,?,?,?)";
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,sillyDate.toString());
            pstmt.setBoolean(2,Boolean.parseBoolean("false"));
            pstmt.setDouble(3,0);
            pstmt.setDouble(4,startMoney);
            pstmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void connectToDB()throws SQLException {
        try{
            connection = DriverManager.getConnection(dbURL,"root","ABC13Y@12Bz");
            myStmt = connection.createStatement();
            System.out.println("Database Connected");

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void cycleSimulation(String unternehmen){

        double close, schnitt;
        String sql = "Select * from "+  unternehmen+" where DateOfDay >= '"+startDate+"' order by DateOfValue asc";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Tagesdurchlauf
            while(rs.next()){

            }


        }catch(SQLException e){
            e.printStackTrace();
        }

    }


}
