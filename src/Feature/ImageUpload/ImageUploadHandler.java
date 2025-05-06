package src.Feature.ImageUpload;

import src.Components.User.LoggedinUser;
import src.Pages.ImageUploadUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ImageUploadHandler {
    private ImageUploadUI imageUploadUI;
    private boolean imageUploaded;
    private static Path destPath;
    private static Path tempDestPath;
    private String username;
    private String imageId;
    private File pngFile;

    public ImageUploadHandler(ImageUploadUI imageUploadUI ) {
        this.imageUploadUI = imageUploadUI;
    }

    public void uploadImage(){
        File selectedFile = chooseImageFile();
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(imageUploadUI, "No file selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        processFile(selectedFile);
    }
    public Path getTempDestPath(){
        return tempDestPath;
    }

    public void saveFiletoDestPath(){
        // destPath = Paths.get("img", "uploaded", newFileName);
        try {
            Files.createDirectories(destPath.getParent());
            Files.copy(pngFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(tempDestPath);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(imageUploadUI, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public String getUsername(){
        return username;
    }

    public String getImageId(){
        return imageId;
    }

    private File chooseImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(imageUploadUI);
        return returnValue == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
    }

    private void processFile(File selectedFile) {
        if (selectedFile == null) return;

        try {
            pngFile = convertToPng(selectedFile);

            username = LoggedinUser.getInstance().getUsername();
            int id = getNextImageId(username);
            String fileExtension = "png";
            imageId = username + "_" + id;
            String newFileName = username + "_" + id + "." + fileExtension;
  

            destPath = Paths.get("img", "uploaded", newFileName);

            tempDestPath = Paths.get("img", "temp_uploaded", newFileName);  
            Files.createDirectories(tempDestPath.getParent());
            Files.copy(pngFile.toPath(), tempDestPath, StandardCopyOption.REPLACE_EXISTING);
            
            File tempFile = tempDestPath.toFile();
            tempFile.deleteOnExit();

            imageUploaded = true;
            JOptionPane.showMessageDialog(imageUploadUI, "Image uploaded and preview updated!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(imageUploadUI, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private File convertToPng(File originalFile) throws IOException {
        BufferedImage image = ImageIO.read(originalFile);
        if (image == null) {
            throw new IOException("Unsupported image format");
        }

        File newpngFile = File.createTempFile("temp_", ".png");
        newpngFile.deleteOnExit();
        boolean success = ImageIO.write(image, "png", newpngFile);
        if (!success) {
            throw new IOException("Failed to convert image to PNG");
        }
        return newpngFile;
    }

    private int getNextImageId(String username) throws IOException {
        Path storageDir = Paths.get("img", "uploaded");
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
        }

        int maxId = findMaxId(username,storageDir);
        return maxId + 1;
    }

    private int getImageIdFromFile(String username,String fileName){
        int idEndIndex = fileName.lastIndexOf('.');
        if (idEndIndex == -1) {
            return  0;
        }
        String idStr = fileName.substring(username.length() + 1, idEndIndex);
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private int findMaxId(String username, Path storageDir) throws IOException{
        int maxId = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(storageDir, username + "_*")) {
            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                int id = getImageIdFromFile(username,fileName);
                if(id>maxId) {
                    maxId = id;
                }
            }
        }
        return maxId;
    }

    public void deleteTempImageFiles(){
        
    }
}
