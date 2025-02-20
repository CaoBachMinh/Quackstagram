import java.awt.*;
import javax.swing.*;


public class SignInUI extends BaseUI {

    protected JTextField txtUsername;
    protected JTextField txtPassword;
    private JButton btnSignIn, btnRegisterNow;
    // private User newUser;

    DataManager credentialsManager;
    // DataManager followingManager;
    DataManager imageDetailManager;
        
    private SignInAction action;

    public SignInUI() {
        super();
        setTitle("Quackstagram - Sign In");
        setupCommonUIProperties();
        action = new SignInAction(this);
        initializeUI();

        credentialsManager = new CredentialsManager();
        credentialsManager.readFile();

        // followingManager = new FollowingManager();
        // followingManager.readFile();

        imageDetailManager = new ImageDetailManager();
        imageDetailManager.readFile();
    }

    private void initializeUI() {
        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));

        Listener(txtUsername, "Username");
        Listener(txtPassword, "Password");

        // Login button with black text
        btnSignIn = new JButton("Sign-In");
        btnSignIn.setBackground(new Color(255, 90, 95));
        btnSignIn.setForeground(Color.BLACK);
        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel loginPanel = new JPanel(new BorderLayout()); // Panel to contain the login button
        loginPanel.setBackground(Color.WHITE);
        loginPanel.add(btnSignIn, BorderLayout.CENTER);

        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.setBackground(Color.WHITE);
        btnRegisterNow.setForeground(Color.BLACK);
        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 1 row, 2 columns
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnRegisterNow);

        // Adding components to the frame
        add(loginPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);

        action.configureButtons(btnSignIn, loginPanel, btnRegisterNow, buttonPanel);
    }

    public static void main(String[] args) {
        OpenSignUI.OpenSignIn();
    }
}


