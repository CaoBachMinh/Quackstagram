package src;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchByUsername extends SearchManager {
    Set<String> matchingUsernames;

    public void searchUsername() {
        String nametoSearch = usernameToSearch();
        Map<String, User> userMap = DataManager.getUserMap();
        matchingUsernames = new HashSet<>();
        for (String username : userMap.keySet()) {
            if (username.contains(nametoSearch)) {
                matchingUsernames.add(username);
            }
        }
    }

    public Set<String> getMatchingUsernames() {
        searchUsername();
        return matchingUsernames;
    }
}
