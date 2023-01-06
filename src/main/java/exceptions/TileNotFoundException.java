package exceptions;

/**
 * This exception is thrown when a tile is not found.
 */
public class TileNotFoundException extends Exception {
    // Constructors
    public TileNotFoundException(int id) {
        super("Tile with id: " + id + "not found.");
    }

    public TileNotFoundException() {
        super("Tile not found.");
    }
}
