import java.util.Map;

public class FollowingQuery extends FollowingManager {
    public FollowingQuery() {};
    private static Map <User,FollowDetails> followMap= getFollowMap();

    public int getFollowersCount(User currentUser) {
        FollowDetails followDetails = followMap.get(currentUser);
        return followDetails.getFollowersCount();
    }

    public int getFollowingCount(User currentUser) {
        FollowDetails followDetails = followMap.get(currentUser);
        return followDetails.getFollowingCount();
    }

    public int getFollowersCount(String username) {
        User currentUser = getUserDetails(username);
        FollowDetails followDetails = followMap.get(currentUser);
        return followDetails.getFollowersCount();
    }

    public int getFollowingCount(String username) {
        User currentUser = getUserDetails(username);
        FollowDetails followDetails = followMap.get(currentUser);
        return followDetails.getFollowingCount();
    }

}
