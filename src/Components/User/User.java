package src.Components.User;
import src.Components.Query.ImageDetailQuery;
import src.Components.UIComponents.Picture;

import java.util.*;

// Represents a user on Quackstagram
public class User {
    private String username;
    private String bio;
    private int postsCount;
    private int followersCount;
    private int followingCount;
    private List<Picture> pictures;


    public User(String username) {
        this.username = username;
        this.pictures = new ArrayList<>();

        this.postsCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
    }

    public User(String username, String bio, int postsCount, int followersCount, int followingCount) {
        this.username = username;
        this.bio = bio;
        this.postsCount = postsCount;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.pictures = new ArrayList<>();
    }

    // Getter methods for user details
    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public void setBio(String bio) {this.bio = bio; }
    public int getPostsCount() { 
        ImageDetailQuery imageDetailQuery = new ImageDetailQuery();
        return imageDetailQuery.getImageUserCount(this);
    }
    public int getFollowersCount() { return followersCount; }
    public int getFollowingCount() { return followingCount; }

    public List<Picture> getPictures() { return pictures; }

    // Setter methods for followers and following counts
    public void setFollowersCount(int followersCount) { this.followersCount = followersCount; }
    public void setFollowingCount(int followingCount) { this.followingCount = followingCount; }
    public void setPostCount(int postCount) { this.postsCount = postCount;}
    // Implement the toString method for saving user information
    @Override
    public String toString() {
    return username + ":" + bio ; // Format as needed
}

}


