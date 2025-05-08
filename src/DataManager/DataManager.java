package src.DataManager;

import src.Components.User.User;

import java.util.HashMap;
import java.util.Map;

public  abstract class  DataManager {
    private static Map <String, User> userMap = new HashMap<>();

    public abstract void readDatabase();
    
    protected static void updateUserMap (String user,User userDetails){
        userMap.put(user,userDetails);
    }

    public static User getUserDetails(String username) {
        return userMap.get(username);
    }

    public static Map <String,User> getUserMap() {
        return userMap;
    }

}





