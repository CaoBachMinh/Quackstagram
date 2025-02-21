package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ContentBox {
    private JPanel itemPanel;
    private JLabel nameLabel;
    private JLabel imageLabel;
    private String imageId;
    private JLabel descriptionLabel;
    private JLabel likesLabel;
    private JButton likeButton;
    private ImageDetails imageDetails;

    public ContentBox(ImageDetails imageDetails, int imageWidth, int imageHeight, Color likeButtonColor) {
        this.setUpItemPanel();
        this.setUpImage(imageDetails,imageWidth, imageHeight);
        this.setUpNameLabel();
        this.setUpDescription();
        this.setUpLikeButton(likeButtonColor);
    }

    public ContentBox(){}

    private void setUpItemPanel() {
        this.itemPanel = new JPanel();
        this.itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        this.itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
        this.itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void setUpNameLabel() {
        this.nameLabel = new JLabel(imageDetails.getUserName());
        this.nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void setUpLikeButton(Color likeButtonColor) {
        this.likesLabel = new JLabel("Likes: "+imageDetails.getLikes());
        this.likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.likeButton = new JButton("❤");
        likeButton.setBackground(likeButtonColor); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border
    }

    private void setUpImage(ImageDetails imageDetails,int imageWidth, int imageHeight) {
        this.imageDetails = imageDetails;
        this.imageLabel = new JLabel();
        this.imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.imageLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
        this.imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label
        this.imageId = imageDetails.getImageId();
        this.cropImage(imageWidth,imageHeight);
    }


    public void cropImage(int imageWidth, int imageHeight) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(this.imageDetails.getImagePath()));
            int minWidth = Math.min(originalImage.getWidth(), imageWidth);
            int minHeight = Math.min(originalImage.getHeight(), imageHeight);
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, minWidth, minHeight);
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            this.imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            this.imageLabel.setText("Image not found");
        }
    }

    public void setUpFullSizeImage(int imageWidth, int imageHeight) {
        this.imageLabel = new JLabel();
        this.imageLabel.setHorizontalAlignment(JLabel.CENTER);
        cropImage(imageWidth,imageHeight);

    }

    private void setUpDescription() {
        this.descriptionLabel = new JLabel(imageDetails.getDescription());
        this.descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public JPanel getItemPanel() {
        this.itemPanel.add(nameLabel);
        this.itemPanel.add(imageLabel);
        this.itemPanel.add(descriptionLabel);
        this.itemPanel.add(likesLabel);
        this.itemPanel.add(likeButton);
        return this.itemPanel;
    }

    public JButton getLikeButton() {
        return this.likeButton;
    }

    public JLabel getImageLabel() {
        return this.imageLabel;
    }

    public String getImageId() {
        return this.imageId;
    }

    public JLabel getLikesLabel() {
        return this.likesLabel;
    }

    public ImageDetails getImageDetails() {
        return this.imageDetails;
    }

    public JLabel getNameLabel() {
        return this.nameLabel;
    }

    public JLabel getDescriptionLabel() {
        return this.descriptionLabel;
    }

    public void setLikesLabel(String likesLabel) {
        this.likesLabel.setText(likesLabel);
    }

    public JPanel getUserPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.Y_AXIS));
        JLabel userName = this.nameLabel;
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName);//User Name
        return userPanel;
    }

    public JPanel getInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(this.descriptionLabel); // Description
        infoPanel.add(this.likesLabel); // Likes
        infoPanel.add(this.likeButton);
        return infoPanel;
    }

    public String getPath(){
        return this.imageDetails.getImagePath();
    }
}
