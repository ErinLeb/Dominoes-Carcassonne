package shared.model;

import interfaces.InterfaceDeck;

public abstract class Deck implements InterfaceDeck {
    // Getters

    // Methods

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
