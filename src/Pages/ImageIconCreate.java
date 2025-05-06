package src.Pages;

import src.Components.User.User;

import javax.swing.ImageIcon;

import java.awt.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageIconCreate extends UIManager {
    private int pathIcon;
    public ImageIconCreate() {}

    public ImageIcon loadProfileImage(User user) throws IOException {
        pathIcon = 0;
        String profileImagePath = "img/storage/profile/" + user.getUsername() + ".png";
        return createScaledImageIcon(Paths.get(profileImagePath));
    }

    public List<ImageIcon> loadUserImages(User user) throws IOException {
    pathIcon = 1;
    Path imageDir = Paths.get("img", "uploaded");
    List<ImageIcon> imageIcons = new ArrayList<>();

    try (Stream<Path> paths = Files.list(imageDir)) {
        List<Path> pathList = paths.collect(Collectors.toList());

        for (Path path : pathList) {
            if (path.getFileName().toString().startsWith(user.getUsername() + "_")) {
                imageIcons.add(createScaledImageIcon(path));
            }
        }
    }
    return imageIcons;
}
    
    private ImageIcon createScaledImageIcon(Path path) {
        if (pathIcon == 0) {
            return new ImageIcon(new ImageIcon(path.toString()).getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        }
        else if (pathIcon == 1) {
            return new ImageIcon(new ImageIcon(path.toString()).getImage().getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
        }
        return null;
    }
}
