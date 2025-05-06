package src.SQLDatabase;

import java.sql.*;

public class SQLTest {
    public static void main(String[] args) {
        Connection con = null;
        try {
            con = DBConnection.getDBConnection();
            System.out.println("Connected to database");
            Statement stmt = con.createStatement();
            String sql = "create table if not exists users (" +
                    "username varchar(30) not null, " +
                    "bio varchar(128) not null, " +
                    "followed_count int default 0, " +
                    "follower_count int default 0, " +
                    "passwords varchar(128) not null, " +
                    "image varbinary(128) not null, " +
                    "post_count int default 0)";
            stmt.executeUpdate(sql);
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
