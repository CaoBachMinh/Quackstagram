package src.DataManager;

import src.Components.User.LoggedinUser;
import src.Components.User.User;
import src.SQLDatabase.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CredentialsManager extends DataManager {

    public CredentialsManager(){};

    @Override
    public void readDatabase() {
        try {
            ResultSet dataset = Database.getUserTable();
            while (dataset.next()) {
                String username = dataset.getString("username");
                String bio = dataset.getString("bio");
                String password = dataset.getString("password");
                User user = new User(username);
                user.setBio(bio);
                super.updateUserMap(username,user);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addNewCredential(String newUsername, String newPassword, String newBio) {
        try {
            Database.insertDataToUser(newUsername,newPassword,newBio);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
  
    protected boolean verifyCredentialsInternal(String username, String password) {
        try {
            String query = "Select COUNT(*) " +
                    "From users " +
                    "Where username=\'"+username+"\' and password=\'"+password+"\'";
            ResultSet dataset = Database.getDatasetFromQuery(query);
            while (dataset.next()) {
                int count = dataset.getInt("COUNT(*)");
                if (count == 1) {
                    LoggedinUser.setLoggedinUser(username);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
