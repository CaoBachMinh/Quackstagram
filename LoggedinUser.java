public class LoggedinUser extends User {
    private static LoggedinUser instance;
    
    private LoggedinUser(String username){
        super(username);
    }

    public static void setLoggedinUser (String username) {
        try {
            if (instance != null) 
                throw  new IllegalStateException("A logged-in user already exists");
            else 
                instance = new LoggedinUser(username);
        } catch (Exception e) {
            System.err.println(e.getMessage()); 
            e.printStackTrace(); 
        }
    }

    public static LoggedinUser getInstance() {
        try {
            if (instance == null) {
                throw new IllegalStateException("No logged-in user exists. Please set a logged-in user");
            } else {
                return instance;
            }
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage()); 
            e.printStackTrace(); 
            return null;
        }
    }

}




