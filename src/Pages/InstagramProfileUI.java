package src.Pages;

import src.DataManager.DataManager;
import src.DataManager.FollowingManager;
import src.Components.UIComponents.HeaderComponents;
import src.Feature.ImageUpload.InstagramProfileAction;
import src.Components.User.LoggedinUser;
import src.Components.User.User;

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
        dataManager.readDatabase();
        
        setTitle("DACS Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setCloseOperation();
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        headerPanel = createHeaderPanel();       // Initialize header panel
        navigationPanel = createNavigationPanel();

        initializeUI();
    }

      public InstagramProfileUI() {

        setTitle("DACS Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setCloseOperation();
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        headerPanel = createHeaderPanel();       // Initialize header panel
        navigationPanel = createNavigationPanel();
        initializeUI();
    }
    
    public void initializeUI() {
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
        LoggedinUser loggedinUser = LoggedinUser.getInstance();

        HeaderComponents headerComponents = new HeaderComponents(currentUser, loggedinUser, isCurrentUser);
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