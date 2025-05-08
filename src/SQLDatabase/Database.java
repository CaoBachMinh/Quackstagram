package src.SQLDatabase;
import java.sql.*;

public class Database {
    private static Connection conn;

    public static void openConnection() {
        try {
            conn = DBConnection.getDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getUserTable() throws SQLException {
        Statement stmt = getStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        return rs;
    }

    public static ResultSet getPostsTable() throws SQLException {
        Statement stmt = getStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM posts");
        return rs;
    }

    public static ResultSet getNotificationsTable() throws SQLException {
        Statement stmt = getStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM notifications");
        return rs;
    }

    public static ResultSet getFollowersTable(String username) throws SQLException {
        Statement stmt = getStatement();
        String query = "Select * from follows where username = '" + username + "'";
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public static void insertDataToUser(String username, String password, String bio) throws SQLException {
        String query = "INSERT INTO users " +
                "(username, bio, followers_count, following_count, password, image, post_count) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, bio);
        pstmt.setInt(3, 0);
        pstmt.setInt(4, 0);
        pstmt.setString(5, password);
        pstmt.setLong(6, 0);
        pstmt.setInt(7, 0);
        pstmt.executeUpdate();
    }

    public static void insertDataToPost(String imageId, String username, String bio, String imagePath, String timestamp) throws SQLException {
        String query = "INSERT INTO posts" +
                "(image,caption,likeCount,timestamp,username,post_id)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, imagePath);
        pstmt.setString(2, bio);
        pstmt.setInt(3, 0);
        pstmt.setString(4, timestamp);
        pstmt.setString(5, username);
        pstmt.setString(6, imageId);
        pstmt.executeUpdate();
    }

    public static ResultSet getDatasetFromQuery(String query) throws SQLException {
        Statement stmt = getStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public static Statement getStatement() throws SQLException {
        return conn.createStatement();
    }
}
