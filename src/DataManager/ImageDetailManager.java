package src.DataManager;

import src.Components.UIComponents.ImageDetails;
import src.Components.User.User;
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
import java.util.*;

public class ImageDetailManager extends DataManager {
    private static final String IMAGE_FOLDER = "img/uploaded/";
    private static Map<String, ImageDetails> imagesMap = new HashMap<>();
    private static Map<String,List<ImageDetails>> userToImageMap = new HashMap<>();
    private static List<ImageDetails> imageDetailsList = new ArrayList<>(); //or arraylist?
    private static Map<String, List<ImageDetails>> keywordImageDetails = new HashMap<>();
    private static Map<String, List<ImageDetails>> hashtagImageDetails = new HashMap<>();


    @Override
    public void readDatabase() {
        try {
            ResultSet dataset = Database.getPostsTable();
            while (dataset.next()) {
                String imageID = dataset.getString("post_id");
                String username = dataset.getString("username");
                String description = dataset.getString("caption");
                String timestamp = dataset.getString("date");
                int likes = dataset.getInt("likeCount");
                String imagePath = dataset.getString("image"); // Assuming PNG format
                User user = getUserDetails(username);
                ImageDetails imageDetails = new ImageDetails (imageID, user, imagePath, description, timestamp, likes);
                processImageDetail(imageDetails,username,imageID);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processImageDetail(ImageDetails imageDetails,String username,String imageID){
        processHashtagBio(imageDetails);
        userToImageMap.computeIfAbsent(username, k -> new LinkedList<>()).add(imageDetails);
        imageDetailsList.add(imageDetails);
        imagesMap.put(imageID, imageDetails);
    }

    private static void processHashtagBio (ImageDetails imageDetails){
        String bio = imageDetails.getDescription();
        String[] words = bio.split("\\s+");
        for (String word : words) {
            String cleanedWord = word.replaceAll("[\\p{Punct}&&[^#]]", "");
            if (cleanedWord.isEmpty()) continue;
            if (cleanedWord.startsWith("#")) {
                imageDetails.addHashtag(cleanedWord);
                hashtagImageDetails.computeIfAbsent(cleanedWord, k -> new ArrayList<>()).add(imageDetails);
            } else {
                cleanedWord = cleanedWord.toLowerCase();

                keywordImageDetails.computeIfAbsent(cleanedWord, k -> new ArrayList<>()).add(imageDetails);
            }
        }
    }

    public static void addImageDetails(String imageId, String username, String bio){
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            User user = getUserDetails(username);
            String imagePath = IMAGE_FOLDER + imageId + ".png";
            Database.insertDataToPost(imageId,username,bio,imagePath,timestamp);
            ImageDetails newImageDetails = new ImageDetails(imageId, user, imagePath, bio, timestamp, 0);
            imagesMap.put(imageId,newImageDetails);
            imageDetailsList.add(newImageDetails);

            List<ImageDetails> userImageList = userToImageMap.computeIfAbsent(username, k -> new ArrayList<>());
            userImageList.add(newImageDetails);

            processHashtagBio(newImageDetails);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static Map<String,List<ImageDetails>> getUserToImage() {
        return userToImageMap;
    }
    protected static Map<String,ImageDetails> getImagesMap() {
        return imagesMap;
    }

    protected static List<ImageDetails> getImagesDetailsList() {
        return  imageDetailsList;
    }

    protected static Map<String, List<ImageDetails>> getKeywordImageDetails(){
        return keywordImageDetails;
    }
    protected static Map<String, List<ImageDetails>> getHashtagImageDetails(){
        return hashtagImageDetails;
    }
}