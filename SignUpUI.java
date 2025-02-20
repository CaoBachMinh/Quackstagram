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
        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");
        txtBio = new JTextField("Bio");
        btnUploadPhoto = new JButton("Upload Photo");
        btnRegister = new JButton("Register");
        btnSignIn = new JButton("Already have an account? Sign In");

        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));

        OpenSignUI listen = new OpenSignUI();
        Listener(txtUsername, "Username");
        Listener(txtPassword, "Password");

        //Bio
        txtBio.setForeground(Color.GRAY);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtBio);
        Listener(txtBio, "Bio");

        //Photo
        JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoUploadPanel.add(btnUploadPhoto);
        fieldsPanel.add(photoUploadPanel);
        action.photoUpload(fieldsPanel, btnUploadPhoto, txtUsername);

        // Register button with black text
        btnRegister.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnRegister.setForeground(Color.BLACK); // Set the text color to black
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        action.addRegisterButton(btnRegister, txtUsername, txtPassword, txtBio);

        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnRegister, BorderLayout.CENTER);

        // Adding components to the frame
        add(registerPanel, BorderLayout.SOUTH);

         // Adding the sign in button to the register panel or another suitable panel
        action.buttonSignIn(btnSignIn, this);
        registerPanel.add(btnSignIn, BorderLayout.SOUTH);
    }
}
