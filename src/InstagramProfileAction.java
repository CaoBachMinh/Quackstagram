package src;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstagramProfileAction {
    private InstagramProfileUI instagramProfileUI;

    public InstagramProfileAction(InstagramProfileUI instagramProfileUI) {
        this.instagramProfileUI = instagramProfileUI;
    }
    
    protected void handleFollowAction(DataManager dataManager, User currentUser) {
        String currentUserUsername = "";
        LoggedinUser loggedinUser = LoggedinUser.getInstance();
        currentUserUsername = loggedinUser.getUsername();
            System.out.println("Real user is "+currentUserUsername);
            // If currentUserUsername is not empty, process following.txt
            if (!currentUserUsername.isEmpty()) {

                FollowingManager.updateCurrentUser(currentUser);
                dataManager.updateFile();
            }
    }

    // Check if the current user is already being followed by the logged-in user
    protected void followButton(JButton followButton,String loggedInUsername, User currentUser, DataManager dataManager) {
        Path followingFilePath = Paths.get("data", "following.txt");
        try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].trim().equals(loggedInUsername)) {
                    String[] followedUsers = parts[1].split(";");
                    for (String followedUser : followedUsers) {
                        if (followedUser.trim().equals(currentUser.getUsername())) {
                            followButton.setText("Following");
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        followButton.addActionListener(e -> {
            handleFollowAction(dataManager, currentUser);
            followButton.setText("Following");
        });
    }
          
    protected void displayImage(ImageIcon imageIcon, JPanel contentPanel) { //hành động xuất hình ảnh
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            contentPanel.removeAll(); // Remove all components from the frame
            instagramProfileUI.initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
        }
