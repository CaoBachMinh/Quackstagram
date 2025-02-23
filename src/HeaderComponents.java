package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HeaderComponents {
    private JPanel headerPanel;
    private JButton followButton;
    private JPanel statsPanel;
    private boolean isCurrentUser;

    public HeaderComponents(User currentUser, LoggedinUser loggedinUser, DataManager dataManager, boolean isCurrentUser) {
        headerPanel = new JPanel();
        statsPanel = new JPanel();
        followButton = new JButton();
        this.isCurrentUser = loggedinUser.getUsername().equals(currentUser.getUsername());
        setupComponents(currentUser, loggedinUser, dataManager);
    }

    private void setupComponents(User currentUser, LoggedinUser loggedinUser, DataManager dataManager) {
        // Thiết lập layout cho headerPanel
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        // Tạo topHeaderPanel
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Tạo profile image
        try {
            ImageIcon profileIcon = new ImageIconCreate().loadProfileImage(currentUser);
            JLabel profileImage = new JLabel(profileIcon);
            profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            topHeaderPanel.add(profileImage, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tạo statsPanel
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));

        // Tạo followButton
        followButton = new JButton(isCurrentUser ? "Edit Profile" : "Follow");
        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height));
        followButton.setBackground(new Color(225, 228, 232));
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Thêm statsPanel và followButton vào topHeaderPanel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        
        headerPanel.add(topHeaderPanel);
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
}
