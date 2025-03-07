package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HeaderComponents {
    private JPanel headerPanel;
    private JButton followButton;
    private JPanel statsPanel;
    private boolean isCurrentUser;
    private DataManager dataManager;
    private InstagramProfileAction action;
    private String loggedInUsername = LoggedinUser.getInstance().getUsername();

    public HeaderComponents(User currentUser, LoggedinUser loggedinUser, boolean isCurrentUser) {
        headerPanel = new JPanel();
        statsPanel = new JPanel();
        followButton = new JButton();
        this.isCurrentUser = loggedinUser.getUsername().equals(currentUser.getUsername());
        this.action = new InstagramProfileAction(this);
        dataManager =  new FollowingManager();
        dataManager.readFile();
        setupComponents(currentUser);
    }

    private void setupComponents(User currentUser) {
        //layout
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        JPanel topHeaderPanel = createTopHeaderPanel(currentUser);

        createProfileImagePanel(currentUser, topHeaderPanel);

        createstatsPanel(currentUser);

        JPanel profileNameAndBioPanel = createProfileNameAndBioPanel(currentUser);

        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);
        headerPanel.add(topHeaderPanel);
        headerPanel.add(profileNameAndBioPanel);

        if (!isCurrentUser) {
            followButton = createFollowButton();
            statsFollowPanel.add(followButton, BorderLayout.EAST);
            action.followButton(followButton, loggedInUsername, currentUser, dataManager);
        }
    }

    public JPanel getHeaderPanel() {
        return headerPanel;
    }

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    public boolean getIsCurrentUser() {
        return isCurrentUser;
    }

    private void createstatsPanel(User currentUser) {
        // Tạo statsPanel
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));
    }

    private void createProfileImagePanel(User currentUser, JPanel topHeaderPanel) {
    try {
        ImageIcon profileIcon = new ImageIconCreate().loadProfileImage(currentUser);
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private JPanel createProfileNameAndBioPanel(User currentUser) {
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(currentUser.getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

        JTextArea profileBio = new JTextArea(currentUser.getBio());
        System.out.println("This is the bio "+currentUser.getUsername());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);
        return profileNameAndBioPanel;
    }

    private JPanel createTopHeaderPanel(User currentUser) {
        // Tạo topHeaderPanel
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));
        return topHeaderPanel;
    }

    private JButton createFollowButton(){
        followButton = new JButton("Follow");
        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height));
        followButton.setBackground(new Color(225, 228, 232));
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return followButton;
    }
}