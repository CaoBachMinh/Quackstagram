package src.SQLDatabase;

import java.sql.*;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/Quackstagram";
    private static String user = "";
    private static String password = "";

    public static Connection getDBConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url,user,password);
    }
}
