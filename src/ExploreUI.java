package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ExploreUI extends UIManager {

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
        JPanel mainContentPanel = createMainContentPanel(null);

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
    
    private JPanel createMainContentPanel(String searchText) {
        // Create the main content panel with search and image grid
        // Search bar at the top
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        setSearchAction(searchField);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)); // Limit the height

        // Image Grid
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows

        // Load images from the uploaded folder
        loadImageToPanel(imageGridPanel,searchText);

        // Set up scroll
        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Main content panel that holds both the search bar and the image grid
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane); // This will stretch to take up remaining space

        return mainContentPanel;
    }

    private void loadImageToPanel(JPanel imageGridPanel,String searchText) {
        File imageDir = new File("img/uploaded");
        if (imageDir.exists() && imageDir.isDirectory()) {
            File[] imageFiles = imageDir.listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg)"));
            if (searchText != null) {
                SearchManager searchManager = new SearchManager(searchText);
                searchManager.processSearch();
                imageFiles = searchManager.getImageToDisplay();
            }
            if (imageFiles == null) {return;}
            for (File imageFile : imageFiles) {
                Image image = createImage(imageFile);
                ImageIcon imageIcon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(imageIcon);
                displayImageClickEvent(imageLabel,imageFile.getPath());
                imageGridPanel.add(imageLabel);
            }
        }
    }


    private void setSearchAction(JTextField searchField) {
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = searchField.getText();
                inputText = (inputText.equals(" Search Users"))? null : inputText;
                System.out.println("Searching for: " + inputText + "...");
                refreshPanel(inputText);
            }
        });
    }

    private void refreshPanel(String searchText) {
        createMainContentPanel(searchText);
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
}
