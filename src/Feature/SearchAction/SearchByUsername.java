package src.Feature.SearchAction;

import src.Components.User.User;
import src.DataManager.DataManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchByUsername extends SearchManager {
    Set<String> matchingUsernames;

    private void searchUsername() {
        matchingUsernames = new HashSet<>();
        String nametoSearch = usernameToSearch();
        Map<String, User> userMap = DataManager.getUserMap();
        
        for (String username : userMap.keySet()) {
            if (username.toLowerCase().contains(nametoSearch)) {
                matchingUsernames.add(username);
            }
        }
    }

    public Set<String> getMatchingUsernames() {
        searchUsername();
        return matchingUsernames;
    }
}
