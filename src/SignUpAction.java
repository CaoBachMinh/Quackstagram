package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SignUpAction {
    private final String credentialsFilePath = "data/credentials.txt";
    private final String profilePhotoStoragePath = "img/storage/profile/";
    private final SignUpUI signUpUI;
    private File selectedProfilePicture = null;

    public SignUpAction(SignUpUI signUpUI) {
        this.signUpUI = signUpUI;
    }

    public void photoUpload(JPanel fieldsPanel, JButton btnUploadPhoto, JTextField txtUsername) {
        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload(txtUsername);
            }
        });
    }

    // Method to handle profile picture upload
    private void handleProfilePictureUpload(JTextField txtUsername) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(signUpUI) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveProfilePicture(selectedFile, txtUsername.getText());
            selectedProfilePicture = selectedFile;
        }
    }

    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRegisterButton(JButton btnRegister, JTextField txtUsername, JTextField txtPassword, JTextField txtBio) {
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRegisterClicked(txtUsername, txtPassword, txtBio);
            }
        });
    }

    private void onRegisterClicked(JTextField txtUsername, JTextField txtPassword, JTextField txtBio) {
        // Handle the register button click event
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String bio = txtBio.getText();

        if (doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(signUpUI, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (selectedProfilePicture == null) {
                JOptionPane.showMessageDialog(signUpUI, "Please select a profile picture.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                saveProfilePicture(selectedProfilePicture, username);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(signUpUI, "Error saving profile picture", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CredentialsManager.addNewCredential(username, password, bio);
            DataManager credentialsManager = new CredentialsManager();
            credentialsManager.updateFile();
            handleRegistrationResult(true);
        }
    }

    private boolean doesUsernameExist(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void buttonSignIn(JButton btnSignIn, SignUpUI signUpUI) {
        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenSignUI.OpenSignIn();
            }
        });
    }

    private void handleRegistrationResult(boolean success) {
        if (success) {
            signUpUI.dispose();
            JOptionPane.showMessageDialog(signUpUI, "Registration Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            OpenSignUI.OpenSignIn();
        }
    }
}
