package src.Pages;

import src.DataManager.CredentialsManager;
import src.DataManager.DataManager;
import src.DataManager.ImageDetailManager;
import src.Feature.SignInUpAction.SignInAction;
import src.SQLDatabase.Database;

import java.awt.*;
import javax.swing.*;

public class SignInUI extends BaseUI {
    public JTextField txtUsername;
    public JTextField txtPassword;
    private JButton btnSignIn, btnRegisterNow;

    private DataManager credentialsManager;
    private DataManager imageDetailManager;
        
    private SignInAction action;

    public SignInUI() {
        super();
        setTitle("Quackstagram - Sign In");
        setupCommonUIProperties();
        action = new SignInAction(this);
        initializeUI();

        Database.openConnection();

        credentialsManager = new CredentialsManager();
        credentialsManager.readDatabase();

        imageDetailManager = new ImageDetailManager();
        imageDetailManager.readDatabase();
    }

    private void initializeUI() {
        UserAndPassText();

        // Login button with black text
        btnSignIn = createButtonSignIn();

        //Login Panel
        JPanel loginPanel = createLoginPanel();

        btnRegisterNow = toRegisterButton();

        JPanel buttonPanel = ButtonPanel(btnSignIn, btnRegisterNow);

        // Adding components to the frame
        add(loginPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);

        action.configureButtons(btnSignIn, btnRegisterNow);
    }

    public static void main(String[] args) {
        OpenSignUI.OpenSignIn();
    }

    private JButton createButtonSignIn() {
        btnSignIn = new JButton("Sign-In");
        btnSignIn.setBackground(new Color(255, 90, 95));
        btnSignIn.setForeground(Color.BLACK);
        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));
        return btnSignIn;
    }

    private JPanel createLoginPanel () {
        JPanel loginPanel = new JPanel(new BorderLayout()); // Panel to contain the login button
        loginPanel.setBackground(Color.WHITE);
        loginPanel.add(btnSignIn, BorderLayout.CENTER);
        return loginPanel;
    }

    private JButton toRegisterButton () {
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.setBackground(Color.WHITE);
        btnRegisterNow.setForeground(Color.BLACK);
        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);
        return btnRegisterNow;
    }

    private JPanel ButtonPanel(JButton btnSignIn, JButton btnRegisterNow) {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnRegisterNow);
        return buttonPanel;
    }

    private void UserAndPassText() {
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
    }
}