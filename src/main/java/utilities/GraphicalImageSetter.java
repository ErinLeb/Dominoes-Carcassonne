package utilities;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GraphicalImageSetter {

    private GraphicalImageSetter() {
        // Prevents instantiation
    }

    /**
     * Generates an {@code ImageIcon} from the given path with a size of 50. An
     * {@code ImageIcon} is used to display an image.
     * 
     * @param path the path of the image
     * @return the {@code ImageIcon}
     */
    public static ImageIcon initImageAsImageIcon(String path) {
        return initImageAsImageIcon(path, 50, 50);
    }

    /**
     * Generates an {@code ImageIcon} from the given path with the given size. An
     * {@code ImageIcon} is used to display an image.
     * 
     * @param path   the path of the image
     * @param width  the width of the image
     * @param height the height of the image
     * @return the {@code ImageIcon}
     */
    public static ImageIcon initImageAsImageIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);

        Image resizedImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

}
