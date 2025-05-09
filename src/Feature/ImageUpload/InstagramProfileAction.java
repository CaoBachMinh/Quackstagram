package src.Feature.ImageUpload;

import src.Components.User.LoggedinUser;
import src.Components.User.User;
import src.DataManager.DataManager;
import src.DataManager.FollowingManager;
import src.Components.UIComponents.HeaderComponents;
import src.Pages.InstagramProfileUI;
import src.SQLDatabase.Database;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstagramProfileAction {
    private InstagramProfileUI instagramProfileUI;
    private HeaderComponents headerComponents;

    public InstagramProfileAction(HeaderComponents headerComponents) {
        this.headerComponents = headerComponents;
    }
    
    public InstagramProfileAction(InstagramProfileUI instagramProfileUI) {
        this.instagramProfileUI = instagramProfileUI;
    }
    
    protected void handleFollowAction(DataManager dataManager, User currentUser) {
        String currentUserUsername = "";
        LoggedinUser loggedinUser = LoggedinUser.getInstance();
        currentUserUsername = loggedinUser.getUsername();
            System.out.println("Real user is "+currentUserUsername);
            if (!currentUserUsername.isEmpty()) {

                FollowingManager.updateCurrentUser(currentUser);
            }
    }

    // Check if the current user is already being followed by the logged-in user
    public void followButton(JButton followButton, String loggedInUsername, User currentUser, DataManager dataManager) {
        AtomicBoolean isFollowing = new AtomicBoolean(false);
        try {
            ResultSet followedUsers = Database.getFollowersTable(loggedInUsername);
            while (followedUsers.next()) {
                String followedUsername = followedUsers.getString("user_followed");
                if (followedUsername.equals(currentUser.getUsername())) {
                    followButton.setText("Following");
                    isFollowing.set(true);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        followButton.addActionListener(e -> {
            handleFollowAction(dataManager, currentUser);
            try {
                if (!isFollowing.get()) {
                    Database.insertDataToFollows(loggedInUsername,currentUser.getUsername());
                    isFollowing.set(true);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            followButton.setText("Following");
        });
    }
          
    public void displayImage(ImageIcon imageIcon, JPanel contentPanel) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout());

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
