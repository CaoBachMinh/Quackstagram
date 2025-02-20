import java.util.Map;

public class ImageDetailQuery extends ImageDetailManager {
    public ImageDetailQuery (){}

    private static Map<String,ImageDetails> imagesMap = getImagesMap();
    private static Map<String,Integer> userImageCounts = getUserImageCounts();

    //getter
    public static int getImageUserCount(String username) {
        return userImageCounts.get(username);
    }
    public static String getUsername(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getUserName();
    }
    public String getDescription(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getDescription();
    }

    public String getImagePath(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getImagePath();
    }
    public static String getTimestamp(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getTimestamp();
    }

    public static void incrementLikes(String imageID){
        ImageDetails imageDetails = imagesMap.get(imageID);
        imageDetails.incrementLikes();
    }
    
    public static int getLikes(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getLikes();
    }

    public User getUser(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getUser();
    }

    public String getImageDetails(String imageID){
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.toString();
    }
}
