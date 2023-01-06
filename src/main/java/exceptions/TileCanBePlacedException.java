package exceptions;

public class TileCanBePlacedException extends Exception {

    public TileCanBePlacedException() {
        super("You have at least one possible placement.");
    }
}
