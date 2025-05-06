package src.SQLDatabase;

import java.sql.*;

public class DBConnection {
    private static String url = "jdbc:sqlite:data/quackstagram.db";

    public static Connection getDBConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(url);
    }
}
