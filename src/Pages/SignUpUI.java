package src.Pages;

import src.Feature.SignInUpAction.SignUpAction;

import javax.swing.*;
import java.awt.*;

public class SignUpUI extends BaseUI {

    protected JTextField txtUsername;
    protected JTextField txtPassword;
    protected JTextField txtBio;
    protected JButton btnRegister;
    protected JButton btnUploadPhoto;
    protected JButton btnSignIn;
    protected SignUpAction action;


    public SignUpUI() {
        super();
        setTitle("Quackstagram - Register");
        setupCommonUIProperties();
        action = new SignUpAction(this);
        initializeUI();
    }

    private void initializeUI() {
        UserAndPassText();

        txtBio = txtBio();

        btnUploadPhoto = createUploadPhotoButton();

        btnRegister = createRegisterButton();

        btnSignIn = toSignInButton();

        JPanel registerPanel = registerPanel(btnRegister, btnSignIn);

        // Adding components to the frame
        add(registerPanel, BorderLayout.SOUTH);
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

    private JTextField txtBio() {
        txtBio = new JTextField("Bio");
        txtBio.setForeground(Color.GRAY);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtBio);
        Listener(txtBio, "Bio");
        return txtBio;
    }

    private JButton createRegisterButton() {
        btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnRegister.setForeground(Color.BLACK); // Set the text color to black
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        action.addRegisterButton(btnRegister, txtUsername, txtPassword, txtBio);
        return btnRegister;
    }

    private JButton createUploadPhotoButton() {
        btnUploadPhoto = new JButton("Upload Photo");
        JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoUploadPanel.add(btnUploadPhoto);
        fieldsPanel.add(photoUploadPanel);
        action.photoUpload(fieldsPanel, btnUploadPhoto, txtUsername);
        return btnUploadPhoto;
    }

    private JPanel registerPanel(JButton btnRegister, JButton btnSignIn) {
        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnRegister, BorderLayout.CENTER);
        registerPanel.add(btnSignIn, BorderLayout.SOUTH);
        return registerPanel;
    }

    private JButton toSignInButton() {
        btnSignIn = new JButton("Already have an account? Sign In");
        action.buttonSignIn(btnSignIn, this);
        return btnSignIn;
    }
}
