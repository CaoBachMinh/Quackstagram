import java.awt.*;
import javax.swing.*;

public abstract class UIManager extends JFrame{

    //InstagramProfileUI / NotificationsUI / ExploreUI
    static final int WIDTH = 300;
    static final int HEIGHT = 500;
    static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
    static final int NAV_ICON_SIZE = 20; // Corrected static size for bottom icons

    //ExploreUI
    static final int IMAGE_SIZE = WIDTH / 3; // Size for each image in the grid

    //QuakstagramHomeUI
    static final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    static final int IMAGE_HEIGHT = 150; // Height for the image posts
    static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button


    JPanel createHeaderPanel(){
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel("🐥 Quackstagram 🐥");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel; 
    }

    JPanel createNavigationPanel() {
        NavigationPanel navigationPanel = new NavigationPanel(this);
        return navigationPanel.createNavigationPanel();
    }


}       
