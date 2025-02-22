package src;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuakstagramHomeUI extends UIManager {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;
    private NotificationManager notificationManager;

    public QuakstagramHomeUI() {
        notificationManager = new NotificationManager();
        notificationManager.readFile();

        setTitle("Quakstagram Home");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setCloseOperation();
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        initializeUI();

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "Home"); // Start with the home view
        
        // Header Panel (reuse from InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel =  createHeaderPanel("🐥 Quackstagram 🐥");
        add(headerPanel, BorderLayout.NORTH);

        // Navigation Bar
        JPanel navigationPanel = createNavigationPanel();
        add(navigationPanel, BorderLayout.SOUTH);
    }


    private void initializeUI() {
        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow horizontal scrolling

        List<ImageDetails> sampleData = ImageDetailQuery.getFollowerImageDetails();


        populateContentPanel(contentPanel, sampleData);
        add(scrollPane, BorderLayout.CENTER);

         // Set up the home panel
         contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
         homePanel.add(scrollPane, BorderLayout.CENTER);
    }


    private void populateContentPanel(JPanel panel, List<ImageDetails> sampleData) {
         for (ImageDetails postData : sampleData) {

             // create object holds post's data
             ContentBox post = new ContentBox(postData, IMAGE_WIDTH, IMAGE_HEIGHT, LIKE_BUTTON_COLOR);

             // set up like and display event for post
             setUpLikeButtonEvent(post);
             setUpDisplayImageEvent(post);

             panel.add(post.getItemPanel());

            // Grey spacing panel
            JPanel spacingPanel = new JPanel();
            spacingPanel.setPreferredSize(new Dimension(WIDTH-10, 5)); // Set the height for spacing
            spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
            panel.add(spacingPanel);
        }
    }

    private void handleLikeAction(ContentBox postData) {
        ImageDetailQuery.incrementLikes(postData.getImageId());
        postData.setLikesLabel("Likes: " + ImageDetailQuery.getLikes(postData.getImageId()));
        updateNotification(postData);
    }

    private void updateNotification(ContentBox post) {
        String currentUser = getCurrentUser();
        String imageOwner = ImageDetailQuery.getUsername(post.getImageId());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String notification = String.format("%s;%s;%s;%s\n",imageOwner,currentUser,post.getImageId(),timestamp);
        NotificationQuery.updateNotificationToCache(notification);
    }

    private void setUpLikeButtonEvent(ContentBox postData){
        JButton likeButton = postData.getLikeButton();
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLikeAction(postData);
            }
        });
    }

    private String getCurrentUser(){
        User currentUser = LoggedinUser.getInstance();
        return currentUser.getUsername();
    }
    
    private void displayImage(ImageDetails imageDetails) {
        imageViewPanel.removeAll(); // Clear previous content

        ContentBox currentPost = new ContentBox(
                imageDetails,
                IMAGE_WIDTH,
                IMAGE_HEIGHT,
                LIKE_BUTTON_COLOR
        );

        currentPost.setUpFullSizeImage(WIDTH-20, HEIGHT-20);

        //User Info
        JPanel userPanel = currentPost.getUserPanel();

        // Set up like and display event for post
        setUpLikeButtonEvent(currentPost);
        setUpRefreshDisplayImage(currentPost);

        // Information panel at the bottom
        JPanel infoPanel = currentPost.getInfoPanel();

        imageViewPanel.add(currentPost.getImageLabel(), BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
        imageViewPanel.add(userPanel,BorderLayout.NORTH);

        imageViewPanel.revalidate();
        imageViewPanel.repaint();


        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    private void setUpRefreshDisplayImage(ContentBox postData) {
        JButton likeButton = postData.getLikeButton();
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshDisplayImage(postData);
            }
        });
    }

    private void setUpDisplayImageEvent(ContentBox postData){
        JLabel imageLabel = postData.getImageLabel();
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayImage(postData.getImageDetails()); // Call a method to switch to the image view
            }
        });
    }

    private void refreshDisplayImage(ContentBox postData) {
        // update display
        postData.setLikesLabel("Likes: " + ImageDetailQuery.getLikes(postData.getImageId()));
        // Call displayImage with updated postData
        displayImage(postData.getImageDetails());
    }



}