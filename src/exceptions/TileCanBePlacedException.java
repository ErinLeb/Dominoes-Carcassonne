package exceptions;

public class TileCanBePlacedException extends Exception {

    public TileCanBePlacedException() {
        super("You can't pass, you have at least one possible placement");
    }
}
