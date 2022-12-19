package domino;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import elements.Deck;
import interfaces.Placeable.Direction;
import utilities.Pair;

/**
 * A deck of dominoes.
 */
public class DeckDomino extends Deck {
    // attributes
    private ArrayList<TileDomino> tiles; // Tiles in the deck

    // constructor

    /**
     * Creates a deck of dominoes of size {@code size}.
     */
    public DeckDomino(int size) {
        tiles = new ArrayList<>(size);
        createDeck(size);
    }

    // getters

    // methods

    /**
     * Fills the list of tiles with the correct number of tiles. The algorithm also
     * creates a game which has a solution.
     * 
     */
    private void createDeck(int nbTiles) {
        Random rand = new Random();

        // Creates the first tile of the game (completely random)
        tiles.add(new TileDomino());

        while (tiles.size() < nbTiles) {

            // Generates a random number of tiles to add to the deck. These tiles will be
            // linked to a new tile (just after the last one)

            int toGenerate = rand.nextInt(3);
            // We handle the case where the number of tiles to be generated is bigger than
            // the number of tiles left to generate

            toGenerate = Math.min(toGenerate, nbTiles - tiles.size() - 1);

            // We choose a random free side of the last tile of the list to link to a new
            // tile
            List<Pair<SideDomino, Direction>> sides = tiles.get(tiles.size() - 1).getUnlinkedSides();

            Pair<SideDomino, Direction> side = sides.get(rand.nextInt(sides.size()));

            // We generate the new tile
            TileDomino tile = new TileDomino(side.first, side.second);

            tiles.add(tile);

            // We generate the other tiles to link to the new one
            for (int i = 0; i < toGenerate; i++) {
                // Random side selector
                sides = tile.getUnlinkedSides();
                side = sides.get(rand.nextInt(sides.size()));

                // We add this new list to the deck
                tiles.add(new TileDomino(side.first, side.second));
            }

        }

        // Unlink all the sides of the tiles
        tiles.forEach(TileDomino::unlink);
    }

    /**
     * Returns {@code true} if the deck is empty.
     * 
     * @return
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    /**
     * Returns the number of dominoes in the deck.
     * 
     * @return
     */
    public int size() {
        return tiles.size();
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Random rand = new Random();

        for (int i = 0; i < tiles.size(); i++) {
            int j = rand.nextInt(tiles.size());

            TileDomino temp = tiles.get(i);
            tiles.set(i, tiles.get(j));
            tiles.set(j, temp);
        }
    }

    /**
     * Returns the domino at the top of the deck.
     * 
     * @return
     */
    public TileDomino draw() {
        return tiles.remove(0);
    }
}