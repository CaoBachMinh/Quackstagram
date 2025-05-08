package src.DataManager;

import src.Components.User.LoggedinUser;
import src.Components.User.User;
import src.SQLDatabase.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FollowingManager extends DataManager {

    private static Map <User,FollowDetails> followMap= new HashMap<>();

    private static User currentUser ;

    public FollowingManager() {}

    class FollowDetails{
        private Set<String> following;
        private int followersCount;
        private int followingCount;

        FollowDetails(Set<String> following,int followersCount, int followingCount) {
            this.following = following;
            this.followersCount = followersCount;
            this.followingCount = followingCount;
        }

        protected Set<String> getFollowingList(){
            return following;
        }


    }
    @Override
    public void readDatabase() {
        try {
            ResultSet dataset = Database.getUserTable();
            int followingCount=0;
            int followersCount=0;
            Set<String> following = new HashSet<>();
            String currentUsername = currentUser.getUsername(); 
            User currentUserInfo = getUserDetails(currentUsername);
            String bio = currentUserInfo.getBio();

            while (dataset.next()) {
                String username = dataset.getString("username");
                ResultSet followingUsers = Database.getFollowersTable(username);
                
                if (username.equals(currentUsername)) {
                    int count = 0;
                    while (followingUsers.next()) {
                        count++;
                        following.add(followingUsers.getString("user_followed"));
                    }
                    followingCount = count;
                } else {
                    while (followingUsers.next()) {
                        if (followingUsers.getString("user_followed").equals(currentUsername)) {
                            followingCount++;
                        }
                    }
                }                
            }
            FollowDetails follwerDetails = new FollowDetails(following,followersCount,followingCount);
            currentUser.setFollowersCount(followersCount);
            currentUser.setFollowingCount(followingCount);
            currentUser.setBio(bio);
            followMap.put(currentUser,follwerDetails);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
    }

    protected static Map <User,FollowDetails> getFollowMap(){
        return followMap;
    }

}
