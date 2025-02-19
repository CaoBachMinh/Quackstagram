
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FollowingManager extends DataManager {
    // private Set <String> followers;
    private static Map <User,FollowDetails> followMap= new HashMap<>();
    // private static final  String filePath = "data/following.txt"; 
    private static Path followingFilePath = Paths.get("data", "following.txt");

    private static User currentUser ;

    public FollowingManager() {}

    class FollowDetails{
        // private Set<String> followers;
        private int followersCount;
        private int followingCount;

        FollowDetails(int followersCount, int followingCount) {
            // this.followers = followers;
            this.followersCount = followersCount;
            this.followingCount = followingCount;
        }

        int getFollowersCount(){
            return followersCount;
        }
        int getFollowingCount(){
            return followingCount;
        }
    }
    @Override
    public void readFile() {
        try (BufferedReader followingReader = Files.newBufferedReader(followingFilePath)) {
            String line;
            int followingCount=0;
            int followersCount=0;


            while ((line = followingReader.readLine()) != null) {
                String[] parts = line.split(":");
                if(parts.length!=2) continue;
                else if(parts[1].trim().isEmpty()) continue;
                
                String username = parts[0].trim();
                String[] followingUsers = parts[1].split(";");


                String currentUsername = currentUser.getUsername(); 
                if (username.equals(currentUsername)) {
                    followingCount = followingUsers.length;
                } else {
                    for (String followingUser : followingUsers) {
                        if (followingUser.trim().equals(currentUsername)) {
                            followersCount++;
                        }
                    }
                }

                FollowDetails follwerDetails = new FollowDetails(followersCount,followingCount);
                // followers = new HashSet<>();
                // for (String follower : followingUsers) {
                //     followers.add(follower);
                // }
                followMap.put(currentUser,follwerDetails);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateFile(){
            boolean found = false;
            StringBuilder newContent = new StringBuilder();
            LoggedinUser loggedinUser = LoggedinUser.getInstance();
            String loggedinUsername = loggedinUser.getUsername();
            String usernameToFollow = currentUser.getUsername();
        try{
            // Read and process following.txt
            if (Files.exists(followingFilePath)) {
                try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts[0].trim().equals(loggedinUsername)) {
                            found = true;
                            if (!line.contains(usernameToFollow)) {
                                line = line.concat(line.endsWith(":") ? "" : "; ").concat(usernameToFollow);
                            }
                        }
                        newContent.append(line).append("\n");
                    }
                }
            }
            // If the current user was not found in following.txt, add them
            if (!found) {

                newContent.append(loggedinUsername).append(": ").append(usernameToFollow).append("\n");
            }

            // updateFollowerToCache(found);

            // Write the updated content back to following.txt
            try (BufferedWriter writer = Files.newBufferedWriter(followingFilePath)) {
                writer.write(newContent.toString());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    // private void updateFollowDetailToCache(boolean isFound) {
    //     if (isFound) {
    //         followersMap.computeIfAbsent(usernameToFollow, k -> new HashSet<>()).add(currentUserUsername);
    //     } else {
    //         followersMap.put(usernameToFollow, new HashSet<>(Collections.singleton(currentUserUsername)));
    //     }
    // }


    public static void updateCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
    }

    protected static Map <User,FollowDetails> getFollowMap(){
        return followMap;
    }

    // public void printFollowersMap() {
    //     if (followersMap.isEmpty()) {
    //         System.out.println("The followers map is empty.");
    //         return;
    //     }

    //     for (Map.Entry<String, Set<String>> entry : followersMap.entrySet()) {
    //         String username = entry.getKey();
    //         Set<String> followers = entry.getValue();

    //         // Print the username
    //         System.out.println(username + ":");

    //         // Print each follower
    //         if (followers.isEmpty()) {
    //             System.out.println("  No followers.");
    //         } else {
    //             for (String follower : followers) {
    //                 System.out.println("  - " + follower);
    //             }
    //         }
    //         System.out.println(); // Add a blank line for readability
    //     }
    // }
}
