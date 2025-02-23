package src;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import javax.swing.*;

import java.awt.event.MouseEvent;
import java.util.List;


public class InstagramProfileUI extends UIManager {
    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private JPanel headerPanel;   // Panel for the header
    private JPanel navigationPanel; // Panel for the navigation
    private User currentUser; // User object to store the current user's information
    private DataManager dataManager;
    private InstagramProfileAction action;
    private boolean isCurrentUser;

    public InstagramProfileUI(User user) {
        this.currentUser = user;
        this.isCurrentUser = false;
        this.action = new InstagramProfileAction(this);
         // Initialize counts
        dataManager =  new FollowingManager();
        FollowingManager.updateCurrentUser(user);
        dataManager.readFile();

        ImageDetailQuery imageDetailQuery = new ImageDetailQuery();
        int imageCount = imageDetailQuery.getImageUserCount(user);
        int followersCount = user.getFollowersCount();
        int followingCount = user.getFollowingCount();

        String bio = user.getBio();

        System.out.println("Bio for " + currentUser.getUsername() + ": " + bio); //print
        currentUser.setBio(bio);
        currentUser.setFollowersCount(followersCount);
        currentUser.setFollowingCount(followingCount);
        currentUser.setPostCount(imageCount);
        System.out.println(currentUser.getPostsCount()); //print

        setTitle("DACS Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        headerPanel = createHeaderPanel();       // Initialize header panel
        navigationPanel = createNavigationPanel();

        initializeUI();
    }
    
    protected void initializeUI() {
        getContentPane().removeAll(); // Clear existing components

        // Re-add the header and navigation panels
        add(headerPanel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);

        // Initialize the image grid
        initializeImageGrid();

        revalidate();
        repaint();


    }

    @Override
    JPanel createHeaderPanel(String headerTitle){return null;}
    
    JPanel createHeaderPanel() {
        String loggedInUsername = "";

        LoggedinUser loggedinUser = LoggedinUser.getInstance();

        HeaderComponents headerComponents = new HeaderComponents(currentUser, loggedinUser, dataManager, isCurrentUser);
        isCurrentUser = headerComponents.getIsCurrentUser();

        JButton followButton;
            if (isCurrentUser) {
                followButton = new JButton("Edit Profile");
            } else {
                followButton = new JButton("Follow");
                action.followButton(followButton, loggedInUsername, currentUser, dataManager);
            };   
        

     // Profile Name and Bio Panel
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

        headerPanel = new JPanel();
        headerPanel.add(profileNameAndBioPanel);


        return headerComponents.getHeaderPanel();

    }

    private void initializeImageGrid() {
        contentPanel.removeAll(); // Clear existing content
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid
        try {
            ImageIconCreate imageIconCreate = new ImageIconCreate();
            List<ImageIcon> imageIcons = imageIconCreate.loadUserImages(currentUser);
            for (ImageIcon icon : imageIcons) {
                JLabel imageLabel = new JLabel(icon);
                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        action.displayImage(icon, contentPanel);
                    }
                });
                contentPanel.add(imageLabel);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

    revalidate();
    repaint();
}
}