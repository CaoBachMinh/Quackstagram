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
    private String[] postData;

    public ContentBox(String[] postData, int imageWidth, int imageHeight, Color likeButtonColor) {
        this.postData = postData;
        this.itemPanel = new JPanel();
        this.itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        this.itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
        this.itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.nameLabel = new JLabel(postData[0]);
        this.nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.imageLabel = new JLabel();
        this.imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.imageLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
        this.imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label
        this.imageId = new File(postData[3]).getName().split("\\.")[0];
        this.cropImage(imageWidth,imageHeight);

        this.descriptionLabel = new JLabel(postData[1]);
        this.descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.likesLabel = new JLabel(postData[2]);
        this.likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.likeButton = new JButton("❤");
        this.likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(likeButtonColor); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border
    }

    private void cropImage(int imageWidth, int imageHeight) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(this.postData[3]));
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

    public String[] getPostData() {
        return this.postData;
    }


}
