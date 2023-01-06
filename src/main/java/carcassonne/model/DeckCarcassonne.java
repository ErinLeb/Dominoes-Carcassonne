package carcassonne.model;

import java.util.ArrayList;

import shared.model.Deck;

/**
 * A deck of the 72 tiles of Carcassonne.
 */
public class DeckCarcassonne extends Deck<SideCarcassonne, TileCarcassonne> {
    // Constructor

    /**
     * Creates a deck of 72 tiles.
     */
    public DeckCarcassonne() {
        tiles = new ArrayList<>(72);
        createDeck();
        shuffle();
    }
    // Methods

    /**
     * Fills the list with the 72 tiles of the game.
     */
    @Override
    public void createDeck() {
        for (int i = 0; i < 24; i++) {
            switch (i) {
                case 0:
                    addTile(i, 9);
                    break;
                case 1:
                case 5:
                case 6:
                case 11:
                case 14:
                case 17:
                case 23:
                    addTile(i, 3);
                    break;
                case 2:
                case 10:
                case 13:
                case 15:
                case 19:
                case 22:
                    addTile(i, 2);
                    break;
                case 3:
                case 4:
                case 18:
                case 20:
                case 21:
                    addTile(i, 1);
                    break;
                case 7:
                    addTile(i, 8);
                    break;
                case 8:
                case 12:
                case 16:
                    addTile(i, 4);
                    break;
                case 9:
                    addTile(i, 5);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Adds {@code j} times a tile of id {@code i} to the deck.
     */
    private void addTile(int i, int j) {
        for (int x = 0; x < j; x++) {
            tiles.add(new TileCarcassonne(i));
        }
    }
}
