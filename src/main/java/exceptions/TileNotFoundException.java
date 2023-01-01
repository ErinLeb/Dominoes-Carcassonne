package exceptions;

/**
 * This exception is thrown when a tile is not found.
 */
public class TileNotFoundException extends Exception {
    // Constructors
    public TileNotFoundException(int id) {
        System.out.println("Tile with id: " + id + "not found.");
    }

    public TileNotFoundException() {
        System.out.println("Tile not found.");
    }
}
