package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.*;

public class ExploreUI extends UIManager {
    private JPanel mainContentPanel;
    private SearchManager searchManager;

    public ExploreUI() {
        setTitle("Explore");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setCloseOperation();
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        getContentPane().removeAll(); // Clear existing components
        setLayout(new BorderLayout()); // Reset the layout manager

        JPanel headerPanel = createHeaderPanel(" Explore 🐥"); // Method from your InstagramProfileUI class
        JPanel navigationPanel = createNavigationPanel(); // Method from your InstagramProfileUI class
        mainContentPanel = new JPanel(new BorderLayout());
        createMainContentPanel(null);

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
    
    private JPanel createMainContentPanel(String searchText) {
        mainContentPanel.removeAll();
        // Create the main content panel with search and image grid
        // Search bar at the top
        setUpSearchPanel();

        if(searchText != null){
            searchManager = new SearchManager(searchText);
            searchManager.processSearch();
        }

        // Image Grid
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows
        // Load images from the uploaded folder
        loadButtontoPanel(imageGridPanel);
        loadImageToPanel(imageGridPanel);

        // Set up scroll
        setUpScrollPanel(imageGridPanel);

        // Main content panel that holds both the search bar and the image grid
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        return mainContentPanel;
    }

    private void setUpSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        setSearchAction(searchField);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)); // Limit the height
        mainContentPanel.add(searchPanel);
    }

    private void setUpScrollPanel(JPanel imageGridPanel) {
        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
    }

    private void loadImageToPanel(JPanel imageGridPanel) {
        File imageDir = new File("img/uploaded");
        if (imageDir.exists() && imageDir.isDirectory()) {
            File[] imageFiles = imageDir.listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg)"));
            
            if(searchManager !=null){
                imageFiles = searchManager.getImageToDisplay();
            }

            if (imageFiles == null) {return;}

            if (isPostDataValid(imageFiles,imageGridPanel)){return;}
            
            for (File imageFile : imageFiles) {
                Image image = createImage(imageFile);
                ImageIcon imageIcon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(imageIcon);
                displayImageClickEvent(imageLabel,imageFile.getPath());
                imageGridPanel.add(imageLabel);
            }
        }
    }

    private boolean isPostDataValid(File[] imageFiles, JPanel imageGridPanel) {
        if (imageFiles.length != 0) {
            return false;
        }
        JPanel noPostPanel = new JPanel(new BorderLayout());
        JLabel noPost = new JLabel("No Post Found!");
        noPost.setFont(new Font("Arial", Font.BOLD, 16));
        noPostPanel.add(noPost, BorderLayout.CENTER);
        imageGridPanel.add(noPostPanel);
        return true;
    }

    private void setSearchAction(JTextField searchField) {
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = searchField.getText();
                inputText = (inputText.equals(" Search Users"))? null : inputText;
                System.out.println("Searching for: " + inputText + "...");
                createMainContentPanel(inputText);
            }
        });
    }

    private void displayImageClickEvent(JLabel imageLabel, String imagePath) {
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayImage(imagePath); // Call method to display the clicked image
            }
        });
    }

    private Image createImage(File imageFile) {
        Image image = new ImageIcon(imageFile.getPath()).getImage();
        return image.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
    }

    private void displayImage(String imagePath) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Add the header and navigation panels back
        add(createHeaderPanel(" Explore 🐥"), BorderLayout.NORTH);
        add(createNavigationPanel(), BorderLayout.SOUTH);

        //create image display
        ImageBox imageBox = new ImageBox(imagePath);
        userButtonClickEvent(imageBox.getUsernameButton(),imageBox.getUsername());
        setUpBackButtonPanel(imageBox.getBackButtonPanel());

        // Container panel for image and details
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(imageBox.getTopPanel(), BorderLayout.NORTH);
        containerPanel.add(imageBox.getImageLabel(), BorderLayout.CENTER);
        containerPanel.add(imageBox.getBottomPanel(), BorderLayout.SOUTH);

        // Add the container panel and back button panel to the frame
        add(imageBox.getBackButtonPanel(), BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void setUpBackButtonPanel(JPanel backButtonPanel){
        JButton backButton = new JButton("Back");
        // Make the button take up the full width
        backButton.setPreferredSize(new Dimension(WIDTH-20, backButton.getPreferredSize().height));
        backButtonPanel.add(backButton);
        backButton.addActionListener(e -> {
            getContentPane().removeAll();
            add(createHeaderPanel(" Explore 🐥"), BorderLayout.NORTH);
            add(createMainContentPanel(null), BorderLayout.CENTER);
            add(createNavigationPanel(), BorderLayout.SOUTH);
            revalidate();
            repaint();
        });
    }

    private void userButtonClickEvent(JButton usernameButton, String username) {
        usernameButton.addActionListener(e -> {
            User user = new User(username); // Assuming User class has a constructor that takes a username
            InstagramProfileUI profileUI = new InstagramProfileUI(user);
            profileUI.setVisible(true);
            dispose(); // Close the current frame
        } );
    }

    public void loadButtontoPanel(JPanel backButtonPanel) {
        Set<String> usersToDisplay = new HashSet<>();
        if(searchManager !=null){
            usersToDisplay = searchManager.getUserToDisplay();
        }

        if(usersToDisplay == null) return;

        
        for (String username : usersToDisplay) {
            JButton userButton =createButtonUser(username);
            backButtonPanel.add(userButton);
        }
    }

    private JButton createButtonUser(String username){
        JButton userButton = new JButton(username);
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = DataManager.getUserDetails(username);
                InstagramProfileUI profileUI = new InstagramProfileUI(user);
                profileUI.setVisible(true);
                dispose();
            }
        });
        return userButton;
    }


}
