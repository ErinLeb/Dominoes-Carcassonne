package domino.model;

import java.util.ArrayList;
import java.util.List;

import exceptions.TileNotFoundException;
import interfaces.Placeable;
import interfaces.Placeable.Direction;
import shared.model.Game;
import shared.model.Player;
import utils.Expandable2DArray;
import utils.Pair;

/**
 * Represents a game of Domino
 */
public class GameDomino extends Game<SideDomino, TileDomino> {

    // Constructor

    /**
     * Creates a game of Domino with {@code nbPlayers} players and {@code nbTiles}
     * tiles.
     * 
     * @param nbPlayers Number of players
     * @param nbTiles   Number of tiles
     */
    public GameDomino(int nbPlayers, int nbTiles) {
        // Creation of the players
        players = new PlayerDomino[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            players[i] = new PlayerDomino();
        }
        // Creation of the deck
        deck = new DeckDomino(nbTiles);
        deckSize = nbTiles;

        currentTile = deck.draw();
        currentPosition = new Pair<>(0, 0);
        board = new Expandable2DArray<>(currentTile);

        currentPlayer = 0;

        deck.shuffle();
    }

    public GameDomino(PlayerDomino[] players, int nbTiles) {
        // Creation of the players
        this.players = players;
        // Creation of the deck
        deck = new DeckDomino(nbTiles);
        deckSize = nbTiles;

        currentTile = deck.draw();
        currentPosition = new Pair<>(0, 0);
        board = new Expandable2DArray<>(currentTile);

        currentPlayer = 0;

        deck.shuffle();
    }

    // Methods

    /**
     * Resets the parameters of the game. If {@code initScore} is {@code true},
     * every score is reset at 0, otherwise, they stay as they are. The deck is
     * generated again with the same number of tiles in it. The board is reset with
     * a new tile to begin with. The current player is the first one declared and
     * the number of rounds is reset too.
     */
    @Override
    public void initGame(boolean resetScore) {
        // isInGame
        for (int i = 0; i < players.length; i++) {
            players[i].setInGame(true);
        }

        // scores
        if (resetScore) {
            for (int i = 0; i < players.length; i++) {
                players[i].resetScore();
            }
        }

        // deck
        deck = new DeckDomino(deckSize);
        deck.shuffle();

        // currentTile & currentPosition
        currentTile = deck.draw();
        currentPosition = new Pair<>(0, 0);

        // board
        board = new Expandable2DArray<>(currentTile);

        // currentPlayer
        currentPlayer = 0;

        // nbRounds
        nbRounds = 0;

        // isGameOn
        isGameOn = true;
    }

    /**
     * Places the tile to place on the board at the given position and updates the
     * score of {@code player}
     * 
     * @param x X position of the tile
     * @param y Y position of the tile
     * @throws TileNotFoundException If the tile is null
     */
    @Override
    public void place(int x, int y, Player player) throws TileNotFoundException {
        super.place(x, y, player);

        List<Pair<Placeable<SideDomino>, Direction>> neighbors = getNeighborsFromPosition(x, y);
        // Increment the score of the player
        incrementPlayerScore(neighbors, player);
    }

    /**
     * Increments the score of the player who placed the tile.
     * 
     * @param neighbors Neighbors of the tile
     * @param player    Player who placed the tile
     */
    protected void incrementPlayerScore(List<Pair<Placeable<SideDomino>, Direction>> neighbors, Player player) {

        // increments score according to what's just been played
        int score = 0;

        for (Pair<Placeable<SideDomino>, Direction> p : neighbors)
            score += tileToPlace.getSide(p.second).getFigSum();

        player.incrementScore(score);
    }

    /**
     * Returns the amount of points a player would score if {@code tile} is placed
     * at ({@code x}, {@code y})
     * 
     * @param x    X position of the tile
     * @param y    Y position of the tile
     * @param tile Tile to place
     * @return amount of points that the player would get if he placed the tile at
     *         the given position
     * @throws IllegalArgumentException  If the tile is null or if there is already
     *                                   a
     *                                   tile at the given position
     * @throws IndexOutOfBoundsException If the position is out of bounds
     *
     */
    public int pointsIfPlaced(int x, int y, TileDomino tile) {

        handleInputPointsIfPlaced(x, y, tile);

        List<Pair<Placeable<SideDomino>, Direction>> neighbors = new ArrayList<>();

        board.getNeighbors(x, y).forEach(p -> neighbors.add(new Pair<>(p.first, p.second)));

        if (neighbors.isEmpty())
            throw new IllegalArgumentException("The tile must be placed next to another tile");

        if (!tile.canBePlaced(neighbors))
            return -1;

        int score = 0;

        for (Pair<Placeable<SideDomino>, Direction> p : neighbors)
            score += tile.getSide(p.second).getFigSum();

        return score;

    }

    private void handleInputPointsIfPlaced(int x, int y, TileDomino tile) {
        if (tile == null)
            throw new IllegalArgumentException("Tile is null");
        if (!board.isInsideExpandableBounds(x, y))
            throw new IndexOutOfBoundsException("Position is out of bounds");
        if (!board.isOutOfBounds(x, y) && board.get(x, y) != null)
            throw new IllegalArgumentException("There is already a tile at this position");
    }
}
