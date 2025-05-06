package src.Components.Query;

import src.DataManager.CredentialsManager;

public class CredentialsQuery extends CredentialsManager {

    public CredentialsQuery(){};
    public  boolean verifyCredentials(String username, String password) {
        return verifyCredentialsInternal(username,password);
    }
}
