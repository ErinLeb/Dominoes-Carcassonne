package domino.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import interfaces.Placeable.Direction;
import interfaces.Placeable;
import shared.model.Deck;

/**
 * A deck of dominoes.
 */
public class DeckDomino extends Deck<SideDomino, TileDomino> {
    // Attributes
    private final int nbTiles; // initial number of tiles

    // Constructor
    /**
     * Creates a deck of dominoes of size {@code size}.
     */
    public DeckDomino(int size) {
        nbTiles = size;

        tiles = new ArrayList<>(size);
        createDeck();
    }

    // Methods

    /**
     * Fills the list of tiles with the correct number of tiles. The algorithm also
     * creates a game which has a solution.
     */
    @Override
    public void createDeck() {
        Random rand = new Random();

        // Creates the first tile of the game (completely random)
        tiles.add(new TileDomino());

        Direction occupied = Direction.UP;

        while (tiles.size() < nbTiles) {

            // Generates a random number of tiles to add to the deck. These tiles will be
            // linked to a new tile (just after the last one).
            // We handle the case where the number of tiles to be generated is bigger than
            // the number of tiles left to generate
            int toGenerate = Math.min(rand.nextInt(3), nbTiles - tiles.size() - 1);

            // We choose a random free side of the last tile of the list to link to a new
            // tile\

            Direction freeDirection = occupied;

            while (freeDirection == occupied) {
                freeDirection = Direction.values()[rand.nextInt(4)];
            }

            SideDomino side = tiles.get(tiles.size() - 1).getSide(freeDirection);

            // We generate the new tile
            TileDomino tile = new TileDomino(side, freeDirection);

            tiles.add(tile);

            Direction directionToGenerate = freeDirection;

            Set<Direction> usedDirections = new HashSet<>();

            usedDirections.add(Placeable.getOpposite(freeDirection));
            // We generate the other tiles to link to the new one
            for (int i = 0; i < toGenerate - 1; i++) {
                // Random side selector

                do {
                    directionToGenerate = Direction.values()[rand.nextInt(4)];
                } while (usedDirections.contains(Placeable.getOpposite(directionToGenerate)));

                usedDirections.add(directionToGenerate);

                side = tiles.get(tiles.size() - 1).getSide(directionToGenerate);

                // We add this new list to the deck
                tiles.add(new TileDomino(side, directionToGenerate));
            }

            occupied = Placeable.getOpposite(directionToGenerate);
        }
    }

}