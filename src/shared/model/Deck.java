package shared.model;

public abstract class Deck {
    // Getters

    // Methods

    /**
     * Returns {@code true} if the deck is empty
     * 
     * @return
     */
    public abstract boolean isEmpty();

    /**
     * Returns the number of dominoes in the deck
     * 
     * @return
     */
    public abstract int size();

    /**
     * Returns the domino at the top of the deck
     * 
     * @return
     */
    public abstract <T extends Side> Tile<T> draw();
}
