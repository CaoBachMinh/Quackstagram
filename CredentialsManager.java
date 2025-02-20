import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public  class CredentialsManager extends DataManager {
    //this only temporarily stores password, 
    //and will be erased after  user login successfully
    private static Map <String,String> userPassword = new HashMap<>(); 

    private static final  String filePath = "data/credentials.txt";
    private static String username;
    private static String bio;
    private static String password ;

    public CredentialsManager(){};

    @Override
    public  void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            String username = parts[0];
            String bio = parts[2];
            String password = parts[1];
            User userDetails = new User(username, bio);
            userPassword.put(username,password);
            super.updateUserMap(username,userDetails);
        }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(username + ":" + password + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addNewCredential (String newUsername,String newPassword,String newBio) {
        username = newUsername;
        password = newPassword;
        bio = newBio;
    }
  
    protected boolean verifyCredentialsInternal(String username, String password) {
        if (!userPassword.containsKey(username)) {
            return false;
        }
        if(userPassword.get(username).equals(password)) {
            LoggedinUser.setLoggedinUser(username);
            userPassword.clear();
            return true;
        }
        else return false;
    }
    
}
