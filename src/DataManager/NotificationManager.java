package src.DataManager;

import src.Components.User.LoggedinUser;
import src.SQLDatabase.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager extends DataManager {
    private static List<String> notificationMessages;
    int lastWrittenIndex =-1;

    @Override
    public void readDatabase() {
        try {
            ResultSet dataset = Database.getNotificationsTable();
            LoggedinUser loggedinUser = LoggedinUser.getInstance();
            String loggedinUsername = loggedinUser.getUsername();
            notificationMessages = new ArrayList<>();

            while (dataset.next()) {
                String senderUser = dataset.getString("sender_username");
                if (senderUser.equals(loggedinUsername)) {
                     // Format the notification message
                     String userWhoLiked = dataset.getString("receive_username");
                     String imageId = dataset.getString("post_id");
                     String timestamp = dataset.getString("timestamp");
                     String notificationMessage = userWhoLiked + " liked your picture - " + getElapsedTime(timestamp) + " ago";
                     notificationMessages.add(notificationMessage);
                }
            }
            lastWrittenIndex = notificationMessages.size() - 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getElapsedTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        StringBuilder timeElapsed = new StringBuilder();
        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }
        if (minutesBetween > 0) {
            if (daysBetween > 0) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        }
        return timeElapsed.toString();
    }

    public static void updateNotificationToCache(String notification){
        notificationMessages.add(notification);
    }
    protected static List<String> getNotificationMessagesInternal(){
        return notificationMessages;
    }
}
