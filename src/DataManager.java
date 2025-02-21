package src;

import java.util.HashMap;
import java.util.Map;

public  abstract class  DataManager {
    private static Map <String,User> userMap = new HashMap<>();
 
    abstract void readFile();
    abstract void updateFile();
    
    protected void updateUserMap (String user,User userDetails){
        userMap.put(user,userDetails);
    }

    protected User getUserDetails(String username) {
        return userMap.get(username);
    }
}



