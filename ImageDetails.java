public class ImageDetails {
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
    public String getUserName(){
        return user.getUsername();
    }
    public String getImagePath(){
        return imagePath;
    }
    public String getDescription(){
        return description;
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
