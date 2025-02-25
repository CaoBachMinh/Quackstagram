package src;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImageDetailQuery extends ImageDetailManager {
    public ImageDetailQuery (){}
    private static Map<String,ImageDetails> imagesMap = getImagesMap();
    private static Map<String,List<ImageDetails>> userToImageMap = getUserToImage();
    private static List<ImageDetails> imageDetailsList = getImagesDetailsList();
    private static Map<String, List<ImageDetails>> keywordImageDetails = getKeywordImageDetails();
    private static Map<String, List<ImageDetails>> hashtagImageDetails = getHashtagImageDetails();


    private static class InvalidHashtagException extends RuntimeException {
        public InvalidHashtagException(String message){
            super(message);
        }
    }


    public static List<ImageDetails> getImageListByKeyword (String keyword){
        try {
            List<ImageDetails> imageList = keywordImageDetails.get(keyword);
            if (imageList == null) {
                throw new NullPointerException("Keyword: "+ keyword + " not found");
            }
            return imageList;
        }catch (NullPointerException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public static List<ImageDetails> getImageListByHashtag (String hashtag){
        try {
            if(!hashtag.startsWith("#"))
                throw new InvalidHashtagException ("Hashtag must start with '#'");
            List<ImageDetails> imageList = hashtagImageDetails.get(hashtag);
            if (imageList == null) 
                throw new InvalidHashtagException("Hashtag: "+ hashtag + " not found");

            return imageList;
        }catch (InvalidHashtagException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public static List<ImageDetails> getFollowerImageDetails(){
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


    public static  List<ImageDetails> getImageListByUsername(String username) {
        return userToImageMap.get(username);
    }

    public static  List<ImageDetails> getImageListByUser(User user) {
        String username = user.getUsername();
        return userToImageMap.get(username);
    }

    public static int getImageUserCount(String username) {
        if (userToImageMap.get(username) ==null)
            return 0;
        else
            return userToImageMap.get(username).size();
    }

    public static int getImageUserCount(User user){
        String username = user.getUsername();
        if(userToImageMap.get(username) ==null)
            return 0;
        else
            return userToImageMap.get(username).size();
    }

    public static String getUsername(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getUserName();
    }
    public static String getDescription(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getDescription();
    }

    public static String getImagePath(String imageID) {
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

    public static User getUser(String imageID) {
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.getUser();
    }

    public static String getImageDetails(String imageID){
        ImageDetails imageDetails = imagesMap.get(imageID);
        return imageDetails.toString();
    }
}


