package domino.model;

import java.util.ArrayList;
import java.util.List;

import exceptions.TileNotFoundException;
import exceptions.UnableToTurnException;
import interfaces.Placeable;
import interfaces.Placeable.Direction;
import utilities.Expandable2DArray;
import utilities.Pair;

/**
 * Represents a game of Domino
 */
public class GameDomino {
    // TODO: Implement player naming and information.

    // Attributes
    // Length of the side of the square of tiles to show
    // It should be an odd number
    private static final int NB_TILES_TO_SHOW = 5;

    private PlayerDomino[] players; // Players of the game
    private DeckDomino deck; // Deck of the game

    private Expandable2DArray<TileDomino> board; // Board of the game

    private TileDomino currentTileDomino; // Current tile on the board
    private Pair<Integer, Integer> currentPosition; // Current position of the current tile on the board
    private TileDomino tileToPlace; // Tile to place

    private int currentPlayer; // Index of the current player

    private int nbRounds = 0; // Number of rounds played

    private boolean isGameOn = true; // Whether the game is on or not

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
        for (int i = 0; i < nbPlayers; i++)
            players[i] = new PlayerDomino();

        // Creation of the deck
        deck = new DeckDomino(nbTiles);

        currentTileDomino = deck.draw();
        currentPosition = new Pair<>(0, 0);
        board = new Expandable2DArray<>(currentTileDomino);

        currentPlayer = 0;
    }

    // Getters

    /**
     * Returns the current tile on the board.
     * 
     * @return The current tile on the board
     */
    public TileDomino getCurrentTileDomino() {
        return currentTileDomino;
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
    public List<List<TileDomino>> getSubArray(int x, int y, int length) {
        return board.getSubArray(x, y, length);
    }

    /**
     * Returns the relative array centered on the current tile. It uses the
     * {@code NB_TILES_TO_SHOW} constant to determine the length of the side of the
     * square.
     * 
     * @return The relative array centered on the current tile
     */
    public List<List<TileDomino>> getCurrentSubArray() {
        return board.getSubArray(currentPosition.first, currentPosition.second,
                NB_TILES_TO_SHOW);
    }

    /**
     * Returns the tile to place.
     * 
     * @return The tile to place
     */
    public TileDomino getTileToPlace() {
        return tileToPlace;
    }

    /**
     * Returns the current player, the one who is playing.
     * 
     * @return The current player
     */
    public PlayerDomino getCurrentPlayer() {
        return players[currentPlayer];
    }

    public Pair<Integer, Integer> getCurrentPosition() {
        return currentPosition;
    }

    // Methods

    /**
     * Initialize the game
     */
    public void initGame() {
        // TODO: implement initGame().
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
        this.currentTileDomino = board.get(currentPosition);

    }

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
        this.currentTileDomino = board.get(currentPosition);
    }

    // TODO: implement can only place next to a tile

    /**
     * Places the tile to place on the board at the given position.
     * 
     * @param x X position of the tile
     * @param y Y position of the tile
     * @throws TileNotFoundException If the tile is null
     */
    public void place(int x, int y, PlayerDomino player) throws TileNotFoundException {

        if (tileToPlace == null)
            throw new TileNotFoundException();

        if (!board.isInsideExpandableBounds(x, y))
            throw new IndexOutOfBoundsException();

        if (!board.isOutOfBounds(x, y) && board.get(x, y) != null)
            throw new IllegalArgumentException("There is already a tile at this position");

        List<Pair<Placeable<SideDomino>, Direction>> neighbors = new ArrayList<>();

        // I do not know if this is the best way to do it, but it works. (I cannot use
        // only board.getNeighbors(x, y) i need to upcast it up to
        // Placeable<SideDomino>)
        board.getNeighbors(x, y).forEach(p -> neighbors.add(new Pair<>(p.first, p.second)));

        if (neighbors.isEmpty())
            throw new IllegalArgumentException("The tile must be placed next to another tile");

        if (!tileToPlace.canBePlaced(neighbors))
            throw new IllegalArgumentException("The tile cannot be placed at this position");

        board.add(x, y, tileToPlace);

        // Takes care of the coordinates of the current tile if the board has been
        // expanded
        if (x == -1)
            x = 0;
        if (y == -1)
            y = 0;

        currentPosition = new Pair<>(x, y);
        currentTileDomino = tileToPlace;

        // increments score according to what's just been played
        int score = 0;

        for (Pair<Placeable<SideDomino>, Direction> p : neighbors)
            score += tileToPlace.getSide(p.second).getFigSum();

        player.addScore(score);
    }

    /**
     * Places the tile on the board in the given direction.
     * 
     * @param id        Id of the tile next to which we want to place out tile
     * @param direction Direction to place the tile
     * @param player    Player who wants to place the tile
     * @throws TileNotFoundException If the tile to place is not on the board
     */
    public void place(int id, Direction direction, PlayerDomino player) throws TileNotFoundException {
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
    public void place(String arg1, String arg2, PlayerDomino player) throws TileNotFoundException {

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
        currentPlayer = (currentPlayer + 1) % players.length;

        tileToPlace = deck.draw();
    }

    /**
     * {@code player} surrenders the game.
     * 
     * @param player Player who surrenders
     */
    public void surrender(PlayerDomino player) {
        System.out.println("Player " + player + " has surrendered");
        isGameOn = false;
    }

    /**
     * The current player surrenders.
     */
    public void surrender() {
        surrender(players[currentPlayer]);
    }

    /**
     * Quits the game.
     */
    public void quit() {
        // TODO: implement better way of doing it
        isGameOn = false;
    }

    /**
     * Returns the list of possible locations where the tile can be placed.
     * 
     * @return List of possible locations
     */
    public List<Pair<Integer, Integer>> findPossiblePlacement() {
        ArrayList<Pair<Integer, Integer>> possibleLocations = new ArrayList<>();

        // We create a copy of the tile to place so that we can rotate it without
        // modifying the original tile
        TileDomino tileToPlaceCopy = tileToPlace.copy();

        // We check all the coordinates of the board and the immediate cases around it
        // (-1 and width/height)
        for (int i = -1; i <= board.getWidth(); i++) {
            for (int j = -1; j <= board.getHeight(); j++) {
                // If there is no tile at the given position or if the position is out of bounds
                // we check if the tile can be placed there
                if (board.isOutOfBounds(i, j) || board.get(i, j) == null) {
                    // We get the neighbors of the tile
                    List<Pair<Placeable<SideDomino>, Direction>> neighbors = new ArrayList<>();

                    // In order to use the function canBePlacedWithRotation, we need to have the
                    // neighbors in a list of pairs of Placeable and Direction
                    // For that we need to convert the list of neighbors of the board to a list of
                    // pairs of Placeable and Direction
                    board.getNeighbors(i, j).forEach(n -> neighbors.add(new Pair<>(n.first, n.second)));

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

    public static void main(String[] args) {
        // Random tests for the moment
        GameDomino game = new GameDomino(2, 28);
        // game.printBoard();// game.parseInput("d", null);

        int[] tab0 = { 0, 0, 0 };
        int[] tab1 = { 1, 1, 1 };
        int[] tab2 = { 2, 2, 2 };
        int[] tab3 = { 3, 3, 3 };

        SideDomino side0 = new SideDomino(tab0);
        SideDomino side1 = new SideDomino(tab1);
        SideDomino side2 = new SideDomino(tab2);
        SideDomino side3 = new SideDomino(tab3);

        TileDomino initialTile = new TileDomino(new SideDomino[] { side0, side1, side2, side3 });

        game.currentTileDomino = initialTile;
        game.board.set(0, 0, initialTile);

        // game.printBoard();

        SideDomino[] sides0 = new SideDomino[] { side0, side0, side0, side0 };
        SideDomino[] sides1 = new SideDomino[] { side1, side1, side1, side1 };
        SideDomino[] sides2 = new SideDomino[] { side2, side2, side2, side2 };
        SideDomino[] sides3 = new SideDomino[] { side3, side3, side3, side3 };

        TileDomino tile0 = new TileDomino(sides0);
        TileDomino tile1 = new TileDomino(sides1);
        TileDomino tile2 = new TileDomino(sides2);
        TileDomino tile3 = new TileDomino(sides3);

        game.currentTileDomino.setNeighbors(new TileDomino[] { tile0, tile1, tile2, tile3 }, Direction.values());

        System.out.println("-----------------------");

        System.out.println("Testing place");
        System.out.println("-----------------------");

        game.tileToPlace = tile0;
        // game.parseInput("place -1 0", null);
        game.tileToPlace = tile3;
        // game.parseInput("place 1 -1", null);
        game.tileToPlace = tile2;
        // game.parseInput("place 2 1", null);
        game.tileToPlace = tile1;
        // game.parseInput("place 1 2", null);

        // System.out.println("----------------------------");
        // System.out.println(game.board.toString());
        // game.board.add(-1, 0, tile0);
        // System.out.println("----------------------------");
        // System.out.println(game.board.toString());
        // game.board.add(1, -1, tile3);
        // System.out.println("----------------------------");
        // System.out.println(game.board.toString());
        // game.board.add(2, 1, tile2);
        // System.out.println("----------------------------");
        // System.out.println(game.board.toString());
        // game.board.add(1, 2, tile1);

        System.out.println("-----------------------");

        System.out.println("Current tile: " + game.currentTileDomino);// game.currentTileDomino.getNeighbor(Direction.UP).printTile();
        game.currentPosition.first = 1;
        game.currentPosition.second = 1;

        System.out.println("-----------------------");

        // game.printBoard();

        System.out.println(game.currentPosition);

        // game.parseInput("move u", null);
        // game.printBoard();

        System.out.println("-----------------------");

        game.board.forEach(System.out::println);

        System.out.println(game.currentPosition);

        System.out.println("-----------------------");

        // game.printBoard();

    }
}
