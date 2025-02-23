package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ImageDetailManager extends DataManager {
    private static final  String filePath = "img/image_details.txt"; 
    private static final String IMAGE_FOLDER = "img/uploaded/";
    private static Map<String,ImageDetails> imagesMap = new HashMap<>();
    private static Map<String,List<ImageDetails>> userToImageMap = new HashMap<>();
    private static List<ImageDetails> imageDetailsList = new ArrayList<>(); //or arraylist?
    private static Map<String, List<ImageDetails>> keywordImageDetails = new HashMap<>();
    private static Map<String, List<ImageDetails>> hashtagImageDetails = new HashMap<>();


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
            String imagePath = IMAGE_FOLDER + parts[0].split(": ")[1] + ".png"; // Assuming PNG format
            User user = getUserDetails(username);
            ImageDetails imageDetails = new ImageDetails (imageID, user, imagePath, description, timestamp, likes);

            processImageDetail(imageDetails,username,imageID);
            
            
        }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { 
            for (ImageDetails imageDetails : imageDetailsList) {
                writer.write(imageDetails.toString()); 
                writer.newLine(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void processImageDetail(ImageDetails imageDetails,String username,String imageID){
        processHashtagBio(imageDetails);
        userToImageMap.computeIfAbsent(username, k -> new LinkedList<>()).add(imageDetails);
        imageDetailsList.add(imageDetails);
        imagesMap.put(imageID, imageDetails);
    }

    private void processHashtagBio (ImageDetails imageDetails){
        String bio = imageDetails.getDescription();
        String[] words = bio.split("\\s+");
        String article = "a, an, the, what, where, who, when";
        for (String word : words) {
            String cleanedWord = word.replaceAll("[\\p{Punct}&&[^#]]", "");
            if (cleanedWord.isEmpty()) continue;
            if (cleanedWord.startsWith("#")) {
                imageDetails.addHashtag(cleanedWord);
                hashtagImageDetails.computeIfAbsent(cleanedWord, k -> new ArrayList<>()).add(imageDetails);
            } else {
                cleanedWord = cleanedWord.toLowerCase();
                if (article.contains(cleanedWord)) {continue;}

                keywordImageDetails.computeIfAbsent(cleanedWord, k -> new ArrayList<>()).add(imageDetails);
            }
        }
    }

    public static void addImageDetails(String imageId, String username, String bio){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        User user = getUserDetails(username);
        String imagePath = IMAGE_FOLDER + imageId + ".png";
        int likes = 0;
        ImageDetails newImageDetails = new ImageDetails(imageId, user, imagePath, bio, timestamp, likes);
        imagesMap.put(imageId,newImageDetails);
        imageDetailsList.add(newImageDetails);

        List<ImageDetails> userImageList = userToImageMap.computeIfAbsent(username, k -> new ArrayList<>());
        userImageList.add(newImageDetails);
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