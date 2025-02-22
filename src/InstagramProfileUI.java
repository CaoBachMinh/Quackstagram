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

    public InstagramProfileUI(User user) {
        this.currentUser = user;
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
        boolean isCurrentUser = false;
        String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
        // try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
        //     String line = reader.readLine();
        //     if (line != null) {
        //         loggedInUsername = line.split(":")[0].trim();
        //         isCurrentUser = loggedInUsername.equals(currentUser.getUsername());
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        LoggedinUser loggedinUser = LoggedinUser.getInstance();
        loggedInUsername = loggedinUser.getUsername();
        isCurrentUser = loggedInUsername.equals(currentUser.getUsername());

    
       // Header Panel
        JPanel headerPanel = new JPanel();
        // try (Stream<String> lines = Files.lines(Paths.get("data", "users.txt"))) {
        //     isCurrentUser = lines.anyMatch(line -> line.startsWith(currentUser.getUsername() + ":"));
        // } catch (IOException e) {
        //     e.printStackTrace();  // Log or handle the exception as appropriate
        // }

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);
        
        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        try {
            ImageIcon profileIcon = new ImageIconCreate().loadProfileImage(currentUser);
            JLabel profileImage = new JLabel(profileIcon);
            profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            topHeaderPanel.add(profileImage, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user"+currentUser.getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()) , "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        
// Follow Button
// Follow or Edit Profile Button
// followButton.addActionListener(e -> handleFollowAction(currentUser.getUsername()));
        JButton followButton;
            if (isCurrentUser) {
                followButton = new JButton("Edit Profile");
            } else {
                followButton = new JButton("Follow");
                action.followButton(followButton, loggedInUsername, currentUser, dataManager);
            };   

        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal space
        followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding

        
        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);

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

        headerPanel.add(profileNameAndBioPanel);


        return headerPanel;

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

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }
}