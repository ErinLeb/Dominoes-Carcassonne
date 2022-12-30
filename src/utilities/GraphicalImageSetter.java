package utilities;

import javax.swing.ImageIcon;
import java.awt.Image;

public class GraphicalImageSetter {

    private GraphicalImageSetter() {
        // Prevents instantiation
    }

    /**
     * Generates an {@code ImageIcon} from the given path. An {@code ImageIcon} is
     * used to display an image.
     * 
     * @param path the path of the image
     * @return the {@code ImageIcon}
     */
    public static ImageIcon initImageAsImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);

        // Width and height of the image
        int width = 50;
        int height = 50;

        Image resizedImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

}
