package src.SQLDatabase;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static void insertDataToLikes(String username, String postId, String notificationId) throws SQLException {
        String query = "INSERT INTO likes (username, post_id, notification_id) VALUES " +
                "(?,?,?);";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, postId);
        pstmt.setString(3, notificationId);
        pstmt.executeUpdate();
    }

    public static void insertDataToFollows(String username, String following) throws SQLException {
        String query = "INSERT INTO follows (username, user_followed) VALUES (?,?);";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, following);
        pstmt.executeUpdate();
    }

    public static void insertDataToNotification(String sender, String receiver, String type, String postId) throws SQLException {
        String query = "INSERT INTO notifications (sender_username,receive_username,type,timestamp,post_id) VALUES" +
                "(?, ?, ?, ?, ?);";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, sender);
        pstmt.setString(2, receiver);
        pstmt.setString(3, "like");
        pstmt.setString(4, timestamp);
        pstmt.setString(5, postId);
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

    public static void likeIncrement(String imageId,int likeCount, String loggedUser, String receivedUser) throws SQLException {
        String query = "Update posts " +
                "Set likeCount=?"+
                " where post_id=?";
        try (PreparedStatement updatePost = conn.prepareStatement(query)) {
            updatePost.setInt(1, likeCount);
            updatePost.setString(2, imageId);
            updatePost.executeUpdate();
        }

        insertDataToNotification(loggedUser,receivedUser,"like",imageId);

        query = "Select notification_id from notifications " +
        "WHERE sender_username=? and receive_username=? ;";
        try (PreparedStatement getNotificationData = conn.prepareStatement(query)) {
            getNotificationData.setString(1, loggedUser);
            getNotificationData.setString(2, receivedUser);
            ResultSet rs = getNotificationData.executeQuery();
            while (rs.next()) {
                String notificationId = rs.getString("notification_id");
                insertDataToLikes(loggedUser,imageId,notificationId);
            }
        }
    }

    public static Statement getStatement() throws SQLException {
        return conn.createStatement();
    }
}
