package shared.model;

import java.util.ArrayList;
import java.util.Random;

import interfaces.InterfaceDeck;

public abstract class Deck<T extends Tile> implements InterfaceDeck {
     // Attributes
     protected ArrayList<T> tiles; // Tiles in the deck

     // Methods

     /**
      * Fills the list of Tiles with Tiles of type {@code T}.
      */
     public abstract void createDeck();

     /**
      * Returns the number of dominoes in the deck
      *
      * @return the number of dominoes in the deck
      */
     public int size() {
          return tiles.size();
     }

     /**
      * Returns {@code true} if the deck is empty, {@code false} otherwise.
      *
      * @return {@code true} if the deck is empty, {@code false} otherwise.
      */
     public boolean isEmpty() {
          return tiles.isEmpty();
     }

     /**
      * Shuffles the deck.
      */
     public void shuffle() {
          Random rand = new Random();

          for (int i = 0; i < tiles.size(); i++) {
               int j = rand.nextInt(tiles.size());

               T temp = tiles.get(i);
               tiles.set(i, tiles.get(j));
               tiles.set(j, temp);
          }
     }

     /**
      * Returns the domino at the top of the deck
      * 
      * @return the domino at the top of the deck
      */
     public T draw() {
          return tiles.remove(0);
     }

}
