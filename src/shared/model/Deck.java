package shared.model;

import interfaces.InterfaceDeck;

public abstract class Deck implements InterfaceDeck {
     // Methods

     /**
      * Returns the number of dominoes in the deck
      *
      * @return the number of dominoes in the deck
      */
     public abstract int size();

     /**
      * Returns {@code true} if the deck is empty, {@code false} otherwise.
      *
      * @return {@code true} if the deck is empty, {@code false} otherwise.
      */
     public abstract boolean isEmpty();

     /**
      * Shuffles the deck.
      */
     public abstract void shuffle();

     /**
      * Returns the domino at the top of the deck
      * 
      * @return the domino at the top of the deck
      */
     public abstract <T extends Side> Tile<T> draw();

}
