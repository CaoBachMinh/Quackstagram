package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.*;
import javax.swing.*;

public class ImageUploadUI extends UIManager {

    private ImageUploadHandler imageUploadHandler ;
    private JLabel imagePreviewLabel;
    private JTextArea bioTextArea;
    private JButton uploadButton;
    private JButton saveButton;
    private String bioText;
    // private boolean imageUploaded = false;

    public ImageUploadUI() {
        setTitle("Upload Image");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setCloseOperation();
        setLayout(new BorderLayout());
        initializeUI();
        imageUploadHandler = new ImageUploadHandler(this);
    }

    private void initializeUI() {
        JPanel headerPanel = createHeaderPanel(" Upload Image 🐥");
        JPanel navigationPanel = createNavigationPanel(); 
        JPanel contentPanel = createContentPanel();

        createImagePreviewLabel();
        createBioTextArea();
        JScrollPane bioScrollPane = createBioScrollPane();
        createUploadButton();
        createBioSaveButton();

        contentPanel.add(imagePreviewLabel);
        contentPanel.add(bioScrollPane);
        contentPanel.add(uploadButton);
        contentPanel.add(saveButton);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }
    
    private void createImagePreviewLabel(){
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));
        setIconToImagePreviewLabel();
    }

    private void setIconToImagePreviewLabel(){
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);
    }

    private JPanel createContentPanel(){
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        return contentPanel;
    }
    private void createBioTextArea(){
        bioTextArea = new JTextArea("Enter a caption");
        bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
    } 

    private JScrollPane createBioScrollPane(){
        JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
        bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        return bioScrollPane;
    }

    private void createUploadButton(){
        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
    }

    private void createBioSaveButton() {
        saveButton = new JButton("Save Caption");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setVisible(false);
        saveButton.addActionListener(this::saveBioAction);
    }

    private void uploadAction(ActionEvent event) {
        imageUploadHandler.uploadImage();
        setImageIcon();
        uploadButton.setText("Upload Another Image");
        if(!saveButton.isVisible())
        saveButton.setVisible(true);
    }

    private void setImageIcon() {
        Path destPath = imageUploadHandler.getDestPath();
        ImageIcon imageIcon = new ImageIcon(destPath.toString());
        if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
            Image image = imageIcon.getImage();
            int previewWidth = imagePreviewLabel.getWidth();
            int previewHeight = imagePreviewLabel.getHeight();
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            double scale = Math.min((double) previewWidth / imageWidth, (double) previewHeight / imageHeight);
            int scaledWidth = (int) (scale * imageWidth);
            int scaledHeight = (int) (scale * imageHeight);

            imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
        }
        imagePreviewLabel.setIcon(imageIcon);
    }

    private void saveBioAction(ActionEvent event) {
        // Here you would handle saving the bio text
        saveButton.setVisible(false);
        bioText = bioTextArea.getText();
        String imageId = imageUploadHandler.getImageId();
        String username = imageUploadHandler.getUsername();
        ImageDetailManager.addImageDetails(imageId,username,bioText);
        JOptionPane.showMessageDialog(this, "Caption saved: " + bioText);
    }

    public String getImageBio(){
        return bioText;
    }
}
