import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ImageDetailManager extends DataManager {
    private static final  String filePath = "img/image_details.txt"; 
    private static Map<String,ImageDetails> imagesMap = new HashMap<>();
    private static Map<String,List<ImageDetails>> userToImageMap = new HashMap<>();
    private static List<ImageDetails> imageDetailsList = new LinkedList<>(); //or arraylist?

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
            ImageDetails imageDetails = new ImageDetails (imageID, user, imagePath, description, timestamp, likes);
            imagesMap.put(imageID, imageDetails);
            imageDetailsList.add(imageDetails);
            userToImageMap.computeIfAbsent(username, k -> new LinkedList<>()).add(imageDetails);
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

    protected static Map<String,List<ImageDetails>> getUserToImage() {
        return userToImageMap;
    }
    protected static Map<String,ImageDetails> getImagesMap() {
        return imagesMap;
    }

    protected static List<ImageDetails> getImagesDetailsList() {
        return  imageDetailsList;
    }

}
