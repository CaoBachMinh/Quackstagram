package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ImageBox {
    private String imageId;
    private String username;
    private String bio;
    private String timestampString;
    private int likes;
    private String timeSincePosting;
    private JPanel topPanel;
    private JLabel imageLabel;
    private JPanel bottomPanel;
    private JPanel backButtonPanel;
    private String imagePath;
    private JButton usernameButton;

    public ImageBox(String imagePath){
        // Extract image ID from the imagePath
        this.imageId = new File(imagePath).getName().split("\\.")[0];
        this.imagePath = imagePath;

        // Read image details
        this.username = ImageDetailQuery.getUsername(imageId);
        this.bio = ImageDetailQuery.getDescription(imageId);
        this.timestampString = ImageDetailQuery.getTimestamp(imageId);
        this.likes = ImageDetailQuery.getLikes(imageId);

        // Calculate time since posting
        this.timeSincePosting = calculateTimePosting(timestampString);

        // Top panel for username and time since posting
        this.topPanel = new JPanel(new BorderLayout());
        setUpTopPanel();

        // Prepare the image for display
        this.imageLabel = new JLabel();
        setUpImage();

        // Bottom panel for bio and likes
        this.bottomPanel = new JPanel(new BorderLayout());
        setUpBottomPanel();

        // Panel for the back button
        this.backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    }

    private String calculateTimePosting(String timestampString){
        if (timestampString.isEmpty()){
            return "Unknown";
        }
        LocalDateTime timestamp = LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(timestamp, now);
        String timeSincePosting = (days > 1 ? "days" : "day") + " ago";
        return timeSincePosting;
    }

    private void setUpTopPanel() {
        usernameButton = new JButton(username);
        JLabel timeLabel = new JLabel(timeSincePosting);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(usernameButton, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);
    }

    private void setUpImage() {
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            ImageIcon imageIcon = new ImageIcon(originalImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            imageLabel.setText("Image not found");
        }
    }

    private void setUpBottomPanel(){
        JTextArea bioTextArea = new JTextArea(bio);
        bioTextArea.setEditable(false);
        JLabel likesLabel = new JLabel("Likes: " + likes);
        bottomPanel.add(bioTextArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public JPanel getBottomPanel() {
        return bottomPanel;
    }

    public JButton getUsernameButton() {
        return usernameButton;
    }

    public JPanel getBackButtonPanel(){
        return backButtonPanel;
    }


    public String getUsername(){
        return username;
    }

}
