import java.util.Map;
import java.util.Set;

public class FollowingQuery extends FollowingManager {
    public FollowingQuery() {};
    private static Map <User,FollowDetails> followMap= getFollowMap();

    public Set<String> getFollowingOfLoggedInUser(){
        LoggedinUser loggedinUser = LoggedinUser.getInstance();
        FollowDetails followDetails = followMap.get(loggedinUser);
        return followDetails.getFollowingList();
    }
}
