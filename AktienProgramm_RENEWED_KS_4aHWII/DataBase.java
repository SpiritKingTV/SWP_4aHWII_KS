package Methods;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBase {
    public static String dbURL = "jdbc:mysql://localhost:3306/stocks?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";


    public static Connection connection;
    public static Statement myStmt;
    public static ArrayList<Double> readCloseFromDB = new ArrayList<Double>();
    public static HashMap<String, Double> readDateAndCloseDB = new HashMap<>();
    public static ArrayList<String> readDateFromDB = new ArrayList<String>();




     public static void connectToDB()throws SQLException {
try{
    connection = DriverManager.getConnection(dbURL,"root","ABC13Y@12Bz");
    myStmt = connection.createStatement();
    System.out.println("Database Connected");

}catch(SQLException e){
    e.printStackTrace();
}

    }

    public static void  createTable(String unternehmenname) throws SQLException{
         try{
             myStmt = connection.createStatement();
             String Command = "CREATE TABLE IF NOT EXISTS " + unternehmenname + " (\n"
                     + " DateOfValue varchar(100) PRIMARY KEY UNIQUE,\n"
                     + " CloseValue double,"
                     +" Schnitt double)";
             //sdvbaiavdsfasvdbi FEHLER????
             myStmt.executeUpdate(Command);

         }catch(SQLException e){
             e.printStackTrace();
         }

    }

    public static void insert(String unternehmen){


        try{
            String sql = "REPLACE INTO "+unternehmen + "(DateOfValue,CloseValue) VALUES(?,?)";
            //Statement myStmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            for(int i=0;i<API.dates.size();i++){
                pstmt.setString(1,API.dates.get(i).toString());
                pstmt.setDouble(2, Double.valueOf(API.HMDatesAndValues.get(API.dates.get(i))));
                pstmt.executeUpdate();

            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void selectDB(String unternehmen){
         String datum;
         double close;
         String SQL = "Select * from "+unternehmen+" order by DateofValue ASC";
         try{
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SQL);
             while(rs.next()){
                 datum = rs.getString("DateofValue");
                 close = rs.getDouble("CloseValue");
                 readCloseFromDB.add(close);
                 readDateFromDB.add(datum);
                 readDateAndCloseDB.put(datum,close);

             }
         }catch(SQLException e){
             e.printStackTrace();
         }
    }
    public static void updateAddSchnitt(String unternehmen){

try{
    String SQL = "Update "+unternehmen + " Set Schnitt = ? where DateOfValue = ?";
    PreparedStatement pstmt = connection.prepareStatement(SQL);
    for(int i=0; i<OtherMethods.Schnitt200er.size();i++){
        pstmt.setDouble(1, OtherMethods.Schnitt200er.get(i));
        pstmt.setString(2, DataBase.readDateFromDB.get(i));
        pstmt.executeUpdate();
    }


}catch(SQLException e){
    e.printStackTrace();
}


    }










}
