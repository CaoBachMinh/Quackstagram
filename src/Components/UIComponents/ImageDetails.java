package src.Components.UIComponents;

import src.Components.User.LoggedinUser;
import src.Components.User.User;
import src.SQLDatabase.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDetails {
    private String imageID;
    private User user;
    private String imagePath;
    private String description;
    private List<String> hashtagList;
    private String timestampString;
    private int likes;

    public ImageDetails(String imageID, User user, String imagePath, String description, String timestampString, int likes) {
        this.imageID= imageID;
        this.user = user;
        this.description= description;
        this.imagePath= imagePath;
        this.timestampString = timestampString;
        this.likes = likes;
        this.hashtagList = new ArrayList<>();
    }
    public String getUserName(){
        return user.getUsername();
    }
    public String getImagePath(){
        return imagePath;
    }
    public String getDescription(){
        return description;
    }
    public void addHashtag(String hashtag){
        hashtagList.add(hashtag);
    }

    public List<String> getHashtagList(){
        return hashtagList;
    }
    public User getUser(){
        return user;
    }
    public String getImageId(){
        return imageID;
    }

    public String getTimestamp() {
        return timestampString;
    }

    public void incrementLikes(){
        likes++;
        try {
            Database.likeIncrement(imageID,likes, LoggedinUser.getInstance().getUsername(), user.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getLikes() {
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
