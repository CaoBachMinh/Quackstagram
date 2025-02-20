
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImageDetailQuery extends ImageDetailManager {
    public ImageDetailQuery (){}

    private static Map<String,ImageDetails> imagesMap = getImagesMap();
    private static Map<String,List<ImageDetails>> userToImageMap = getUserToImage();
    private static List<ImageDetails> imageDetailsList = getImagesDetailsList();

    //getter
    public int getImageUserCount(String username) {
        return userToImageMap.get(username).size();
    }

    public int getImageUserCount(User user){
        String username = user.getUsername();
        return userToImageMap.get(username).size();
    }

    public List<ImageDetails> getFollowerImageDetails(){
        FollowingQuery followingQuery = new FollowingQuery();
        Set<String> followersOfLoggedinUser = followingQuery.getFollowingOfLoggedInUser();
        List<ImageDetails> imageDetailsOfFollowers = new LinkedList<>();
        for(ImageDetails imageDetails:imageDetailsList){
            String followerUsername = imageDetails.getUserName();
            if(followersOfLoggedinUser.contains(followerUsername)){
                imageDetailsOfFollowers.add(imageDetails);
            }
        }
        return imageDetailsOfFollowers;
    }

    public String getUsername(String imageID) {
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
    public String getTimestamp(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getTimestamp();
    }

    public void incrementLikes(String imageID){
        ImageDetails imageDetails = imagesMap.get(imageID);
        imageDetails.incrementLikes();
    }
    
    public int getLikes(String imageID) {
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
