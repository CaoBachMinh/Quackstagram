package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager extends DataManager {
    private static final  String filePath = "data/notifications.txt"; 
    private static List<String> notificationMessages;
    int lastWrittenIndex =-1;
 
    class NotiDetails{
        private String userWhoLiked;
        private String imageId;
        private String timestamp;

        public NotiDetails(String userWhoLiked,String imageId,String timestamp,List<String> notificationMessages) {
            this.userWhoLiked = userWhoLiked;
            this.imageId = imageId;
            this.timestamp = timestamp;
        }
        
    }
    @Override
    void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {     
            String line;
            LoggedinUser loggedinUser = LoggedinUser.getInstance(); 
            String loggedinUsername = loggedinUser.getUsername();
            notificationMessages = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                 String[] parts = line.split(";");
                if (parts[0].trim().equals(loggedinUsername)) {
                     // Format the notification message
                     String userWhoLiked = parts[1].trim();
                     String imageId = parts[2].trim();
                     String timestamp = parts[3].trim();
                     String notificationMessage = userWhoLiked + " liked your picture - " + getElapsedTime(timestamp) + " ago";
                     notificationMessages.add(notificationMessage);
                }
            }
            lastWrittenIndex = notificationMessages.size() - 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void updateFile() {
        if(notificationMessages==null){
            return;
        }
        if (lastWrittenIndex >= notificationMessages.size() - 1) {
            return; 
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {     
            boolean isFirstWrite = true;
            for (int i = lastWrittenIndex + 1; i < notificationMessages.size(); i++) {
                if(isFirstWrite){
                    writer.write("\n");
                    isFirstWrite=false;
                }
                String message = notificationMessages.get(i);
                if (!message.isEmpty()) { // Skip empty messages
                    writer.write(message);
                }
            }
            lastWrittenIndex = notificationMessages.size() - 1;
        } catch (IOException e) {
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
