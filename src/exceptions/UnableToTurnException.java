package exceptions;

/**
 * This exception is thrown when a tile cannot be turned.
 */
public class UnableToTurnException extends Exception {
    // Constructor
    public UnableToTurnException() {
        super("Cannot turn tile.");
    }
}
