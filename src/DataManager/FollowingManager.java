package src.DataManager;

import src.Components.User.LoggedinUser;
import src.Components.User.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class FollowingManager extends DataManager {

    private static Map <User,FollowDetails> followMap= new HashMap<>();
    // private static final  String filePath = "data/following.txt"; 
    private static Path followingFilePath = Paths.get("data", "following.txt");

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
        try (BufferedReader followingReader = Files.newBufferedReader(followingFilePath)) {
            String line;
            int followingCount=0;
            int followersCount=0;
            Set<String> following = new HashSet<>();
            String currentUsername = currentUser.getUsername(); 
            User currentUserInfo = getUserDetails(currentUsername);
            String bio = currentUserInfo.getBio();

            while ((line = followingReader.readLine()) != null) {
                String[] parts = line.split(":");
                if(parts.length!=2) continue;
                else if(parts[1].trim().isEmpty()) continue;
                
                String username = parts[0].trim();
                String[] followingUsers = parts[1].split(";");

                
                if (username.equals(currentUsername)) {
                    followingCount = followingUsers.length;
                    following = Arrays.stream(followingUsers)
                    .map(String::trim)
                    .collect(Collectors.toSet());//only get follower list of loggedin user
                    
                } else {
                    for (String followingUser : followingUsers) {
                        if (followingUser.trim().equals(currentUsername)) {
                            followersCount++;
                        }
                    }
                }                
            }
            FollowDetails follwerDetails = new FollowDetails(following,followersCount,followingCount);
            currentUser.setFollowersCount(followersCount);
            currentUser.setFollowingCount(followingCount);
            currentUser.setBio(bio);
            followMap.put(currentUser,follwerDetails);

        } catch (IOException e) {
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
