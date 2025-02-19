import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ImageDetailManager extends DataManager {
    private static final  String filePath = "img/image_details.txt"; 
    private static Map<String,ImageDetails> imagesMap = new HashMap<>();
    private static Map<String,Integer> userImageCounts = new HashMap<>();

    class ImageDetails {
        private String imageID;
        private User user;
        private String imagePath;
        private String description;
        private String timestampString;
        private int likes;

        ImageDetails(String imageID,User user,String imagePath,String description, String timestampString,int likes) {
            this.imageID= imageID;
            this.user = user;
            this.description= description;
            this.imagePath= imagePath;
            this.timestampString = timestampString;
            this.likes = likes;
        }
        String getUserName(){
            return user.getUsername();
        }
        String getImagePath(){
            return imagePath;
        }
        String getDescription(){
            return description;
        }
        User getUser(){
            return user;
        }

        String getTimestamp() {
            return timestampString;
        }

        void incrementLikes(){
            likes++;
        }
        int getLikes() {
            return likes;
        }
        @Override
        public String toString() {
            return "ImageID: " + imageID + 
                   ", Username: " + user.getUsername() + 
                   ", Bio: " + description + 
                   ", Timestamp: " + timestampString + 
                   ", Likes: " + likes;
        }
    }

    @Override
    public void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            String imageID = parts[0].split(": ")[1].trim();
            String username = parts[1].split(": ")[1].trim();
            String description = parts[2].split(": ")[1].trim();
            String timestamp = parts[3].split(": ")[1].trim();
            int likes = Integer.parseInt(parts[4].split(": ")[1].trim());
            String imagePath = "img/uploaded/" + parts[0].split(": ")[1] + ".png"; // Assuming PNG format
            User user = getUserDetails(username);
            ImageDetails imageDetails = new ImageDetails(imageID,user,imagePath, description,timestamp, likes);
            imagesMap.put(imageID,imageDetails);
            userImageCounts.put(username, userImageCounts.getOrDefault(username, 0) + 1);
        }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { 
            for (Map.Entry<String, ImageDetails> entry : imagesMap.entrySet()) {
                writer.write(entry.getValue().toString()); 
                writer.newLine(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static Map<String,Integer> getUserImageCounts() {
        return userImageCounts;
    }
    protected static Map<String,ImageDetails> getImagesMap() {
        return imagesMap;
    }

}
