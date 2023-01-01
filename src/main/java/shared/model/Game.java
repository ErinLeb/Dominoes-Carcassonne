package shared.model;

import java.util.ArrayList;
import java.util.List;

import exceptions.TileNotFoundException;
import exceptions.UnableToTurnException;
import interfaces.Placeable;
import interfaces.Placeable.Direction;
import utilities.Expandable2DArray;
import utilities.Pair;
import utilities.TriConsumer;

public abstract class Game<S extends Side, T extends Tile<S>> {

    protected Player[] players; // Players of the game
    protected Deck<S, T> deck; // Deck of the game

    // Attributes
    // Length of the side of the square of tiles to show
    // It should be an odd number
    protected static final int NB_TILES_TO_SHOW = 5;

    protected int deckSize; // Total number of tiles

    protected Expandable2DArray<T> board; // Board of the game

    protected T currentTile; // Current tile on the board
    protected Pair<Integer, Integer> currentPosition; // Current position of the current tile on the board
    protected T tileToPlace; // Tile to place

    protected int currentPlayer; // Index of the current player

    protected int nbRounds = 0; // Number of rounds played

    protected boolean isGameOn = true; // Whether the game is on or not

    // Getters

    public int getNbPlayers() {
        return players.length;
    }

    /**
     * Returns the current tile on the board.
     * 
     * @return The current tile on the board
     */
    public T getCurrentTile() {
        return currentTile;
    }

    /**
     * Returns the number of rounds played.
     * 
     * @return The number of rounds played
     */
    public int getNbRounds() {
        return nbRounds;
    }

    /**
     * Returns {@code true} if the game is on, {@code false} otherwise.
     * 
     * @return {@code true} if the game is on, {@code false} otherwise
     */
    public boolean isGameOn() {
        return isGameOn;
    }

    /**
     * Returns the relative array centered on {@code x,y}.
     * 
     * @param x      X coordinate of the center
     * @param y      Y coordinate of the center
     * @param length Length of the side of the square
     * @return The relative array centered on {@code x,y}
     */
    public List<List<T>> getSubArray(int x, int y, int length) {
        return board.getSubArray(x, y, length);
    }

    /**
     * Returns the relative array centered on the current tile. It uses the
     * {@code NB_TILES_TO_SHOW} constant to determine the length of the side of the
     * square.
     * 
     * @return The relative array centered on the current tile
     */
    public List<List<T>> getCurrentSubArray() {
        return board.getSubArray(currentPosition.first, currentPosition.second,
                NB_TILES_TO_SHOW);
    }

    /**
     * Returns the tile to place.
     * 
     * @return The tile to place
     */
    public T getTileToPlace() {
        return tileToPlace;
    }

    /**
     * Returns the current player, the one who is playing.
     * 
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    public Pair<Integer, Integer> getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Returns the player(s) with the highest score.
     * 
     * @return a list of the player(s) still in the game with the highest score.
     */
    public List<Player> getWinners() {
        Player[] ranking = getRanking();
        ArrayList<Player> winners = new ArrayList<>();
        winners.add(ranking[0]);

        int scoreMax = ranking[0].getScore();
        int i = 1;

        while (i < ranking.length && ranking[i].isInGame() && ranking[i].getScore() == scoreMax) {
            winners.add(ranking[i]);
            i++;
        }

        return winners;
    }

    /**
     * Returns an array of {@code PlayerDomino} sorted by their {@code score}.
     * 
     * @return an array of {@code PlayerDomino} sorted by their {@code score}.
     */
    public Player[] getRanking() {
        Player[] ranking = players.clone();

        // Differentiate players who surrendered
        List<Player> surrendered = new ArrayList<>();
        List<Player> stillInGame = new ArrayList<>();

        for (int i = 0; i < ranking.length; i++) {

            if (ranking[i].isInGame()) {
                stillInGame.add(ranking[i]);

            } else {
                surrendered.add(ranking[i]);
            }
        }

        // Sort by score
        surrendered.sort((a, b) -> b.getScore() - a.getScore());
        stillInGame.sort((a, b) -> b.getScore() - a.getScore());

        // Add players still in the game, then those who surrendered
        for (int i = 0; i < ranking.length; i++) {
            if (i < stillInGame.size()) {
                ranking[i] = stillInGame.get(i);
            } else {
                ranking[i] = surrendered.get(i - stillInGame.size());
            }
        }

        return ranking;
    }

    // Setters

    // TODO : check if we still need this setter
    public void setIsGameOn(boolean b) {
        isGameOn = b;
    }

    // Methods

    /**
     * Resets the parameters of the game. If {@code initScore} is {@code true},
     * every score is reset at 0, otherwise, they stay as they are. The deck is
     * generated again with the same number of tiles in it. The board is reset with
     * a new tile to begin with. The current player is the first one declared and
     * the number of rounds is reset too.
     */
    public abstract void initGame(boolean resetScore);

    /**
     * Moves the current tile on the board in the given direction once.
     * 
     * @param direction Direction to move the current tile
     */
    public void move(Direction direction) {
        move(direction, 1);

    }

    /**
     * Moves the center of the minimap to the tile with the given id.
     * 
     * @param id Id of the tile to move to
     * @throws TileNotFoundException If the tile is not on the board
     */
    public void move(int id) throws TileNotFoundException {
        Pair<Integer, Integer> position = board.findIndex(t -> t != null && t.getId() == id);

        if (position == null)
            throw new TileNotFoundException();

        this.currentPosition = position;
        this.currentTile = board.get(currentPosition);
    }

    /**
     * Moves the current position of the current tile on the board in the given
     * {@code direction} by {@code nbTiles}.
     * 
     * @param direction Direction to move
     * @param nbTiles   Number of tiles to move
     */
    public void move(Direction direction, int nbTiles) {
        if (nbTiles < 0)
            throw new IllegalArgumentException("The number of tiles to move must be positive");

        if (nbTiles == 0)
            return;

        Pair<Integer, Integer> nextPosition = new Pair<>(currentPosition.first, currentPosition.second);

        switch (direction) {
            case UP:
                nextPosition.first -= nbTiles;
                break;
            case RIGHT:
                nextPosition.second += nbTiles;
                break;
            case DOWN:
                nextPosition.first += nbTiles;
                break;
            case LEFT:
                nextPosition.second -= nbTiles;
                break;
        }

        if (board.isOutOfBounds(nextPosition))
            throw new IllegalArgumentException("The next position is out of bounds.");

        this.currentPosition = nextPosition;
        this.currentTile = board.get(currentPosition);
    }

    /**
     * Places the tile to place on the board at the given position.
     * 
     * @param x X position of the tile
     * @param y Y position of the tile
     * @throws TileNotFoundException If the tile is null
     */
    public void place(int x, int y, Player player) throws TileNotFoundException {

        handlePlaceInputErrors(x, y);

        List<Pair<Placeable<S>, Direction>> neighbors = getNeighborsFromPosition(x, y);

        if (neighbors.isEmpty())
            throw new IllegalArgumentException("The tile must be placed next to another tile");

        if (!tileToPlace.canBePlaced(neighbors))
            throw new IllegalArgumentException("The tile cannot be placed at this position");

        board.add(x, y, tileToPlace);

        tileToPlace.setPlaced(true);

        // Takes care of the coordinates of the current tile if the board has been
        // expanded
        if (x == -1)
            x = 0;
        if (y == -1)
            y = 0;

        currentPosition = new Pair<>(x, y);
        currentTile = tileToPlace;
    }

    /**
     * Throws an exception if the tile to place is null, if the position is out of
     * bounds or if there is already a tile at this position.
     * 
     * @param x X position of the tile
     * @param y Y position of the tile
     * @throws TileNotFoundException If the tile to place is null
     */
    private void handlePlaceInputErrors(int x, int y) throws TileNotFoundException {
        if (tileToPlace == null)
            throw new TileNotFoundException();

        if (!board.isInsideExpandableBounds(x, y))
            throw new IndexOutOfBoundsException();

        if (!board.isOutOfBounds(x, y) && board.get(x, y) != null)
            throw new IllegalArgumentException("There is already a tile at this position");
    }

    /**
     * Gets the neighbors of the tile at the given position and upcasts them to
     * {@code Placeable<S>}.
     * 
     * 
     * @param x X position of the tile
     * @param y Y position of the tile
     * @return The neighbors of the tile at the given position *
     */
    public List<Pair<Placeable<S>, Direction>> getNeighborsFromPosition(int x, int y) {
        List<Pair<Placeable<S>, Direction>> neighbors = new ArrayList<>();
        board.getNeighbors(x, y).forEach(p -> neighbors.add(new Pair<>(p.first, p.second)));
        return neighbors;
    }

    /**
     * Places the tile on the board in the given direction.
     * 
     * @param id        Id of the tile next to which we want to place out tile
     * @param direction Direction to place the tile
     * @param player    Player who wants to place the tile
     * @throws TileNotFoundException If the tile to place is not on the board
     */
    public void place(int id, Direction direction, Player player) throws TileNotFoundException {
        Pair<Integer, Integer> index = board.findIndex(t -> t != null && t.getId() == id);
        Placeable.updateCoordinatesFromDirection(index, direction);

        place(index.first, index.second, player);
    }

    /**
     * Places the given tile on the board at the given position.
     * 
     * @param arg1   X position of the tile
     * @param arg2   Y position of the tile
     * @param player
     * @throws TileNotFoundException If the tile is null
     */
    public void place(String arg1, String arg2, Player player) throws TileNotFoundException {

        try {
            if (arg2.matches("^\\d+") || arg2.matches("^-\\d+")) {
                // The arguments given are coordinates of the tile to place on the board
                // relative to the minimap
                int dx = Integer.parseInt(arg1) - (NB_TILES_TO_SHOW / 2 + 1);
                int dy = Integer.parseInt(arg2) - (NB_TILES_TO_SHOW / 2 + 1);
                place(currentPosition.first + dx, currentPosition.second + dy, player);
            } else {
                int id = Integer.parseInt(arg1);
                Direction direction = Placeable.stringToDirection(arg2.toUpperCase());
                if (direction == null)
                    throw new IllegalArgumentException("Invalid direction");
                place(id, direction, player);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid position");
        }
    }

    /**
     * Turns a tile {@code times} to the right if {@code clockwise} is {@code true}
     * and to the left otherwise.
     * 
     * @param clockwise If {@code true} turns the tile clockwise, otherwise turns it
     *                  anticlockwise
     * @param times     Number of times to turn the tile
     * 
     * @throws UnableToTurnException If the tile to turn is null or if the tile is
     *                               already placed on the board
     */
    public void turn(boolean clockwise, int times) throws UnableToTurnException {
        if (tileToPlace == null)
            throw new UnableToTurnException();

        if (clockwise) {
            tileToPlace.turnRight(times);
        } else {
            tileToPlace.turnLeft(times);
        }
    }

    /**
     * Updates the game information when a round is over.
     */
    public void updateGameRound() {
        nbRounds++;

        if (nbRounds != 1) {
            // The next player is the next still in the game
            do {
                currentPlayer = (currentPlayer + 1) % players.length;
            } while (!players[currentPlayer].isInGame());
        }

        tileToPlace = deck.draw();

        if (deck.isEmpty()) {
            isGameOn = false;
        }
    }

    /**
     * {@code player} surrenders.
     * 
     * @param player Player who surrenders
     */
    public void surrender(Player player) {
        player.setInGame(false);

        // We check if isGameOn is still true
        int nbPlayersLeft = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i].isInGame()) {
                nbPlayersLeft++;
            }
        }
        if (nbPlayersLeft <= 1) {
            isGameOn = false;
        }
    }

    /**
     * Quits the game.
     */
    public void quit() {
        isGameOn = false;
    }

    /**
     * Returns the list of possible locations where the tile can be placed.
     * 
     * @return List of possible locations
     */
    public List<Pair<Integer, Integer>> findPossiblePlacements() {
        List<Pair<Integer, Integer>> possibleLocations = new ArrayList<>();

        // We create a copy of the tile to place so that we can rotate it without
        // modifying the original tile
        Tile<S> tileToPlaceCopy = tileToPlace.copy();

        // We check all the coordinates of the board and the immediate cases around it
        // (-1 and width/height)
        for (int i = -1; i <= board.getWidth(); i++) {
            for (int j = -1; j <= board.getHeight(); j++) {
                // If there is no tile at the given position or if the position is out of bounds
                // we check if the tile can be placed there
                if (board.isOutOfBounds(i, j) || board.get(i, j) == null) {
                    // We get the neighbors of the tile
                    List<Pair<Placeable<S>, Direction>> neighbors = getNeighborsFromPosition(i, j);

                    // We check if the tile can be placed ignoring the rotations
                    // If it can, we add the position to the list of possible locations
                    if (tileToPlaceCopy.canBePlacedWithRotation(neighbors)) {
                        possibleLocations.add(new Pair<>(i, j));
                    }
                }
            }
        }
        return possibleLocations;
    }

    /**
     * If {@code isGameOn} is {@code false} or the deck is empty, switches isGameOn
     * to false and return {@code true}.
     * 
     * @return {@code true} if the deck is empty.
     */
    public boolean endGame() {
        if (deck.isEmpty() || !isGameOn) {
            isGameOn = false;
            return true;
        }
        return false;
    }

    /**
     * Apply a function to the minimap. The function takes as argument the x
     * position of the tile, the y position of the tile and the tile itself.
     * 
     * @param func The function to apply
     */
    public void applyFunctionMinimap(TriConsumer<Integer, Integer, T> func) {
        board.iteriSubArray(currentPosition.first, currentPosition.second, NB_TILES_TO_SHOW, func);
    }

    public int getNbRemainingTiles() {
        return deck.size();
    }

}
