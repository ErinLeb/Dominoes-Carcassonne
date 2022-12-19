package domino;

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
    private static final int NB_TILES_TO_SHOW = 5; // Length of the side of the square of tiles to show

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

    public TileDomino getCurrentTileDomino() {
        return currentTileDomino;
    }

    public int getNbRounds() {
        return nbRounds;
    }

    public boolean isGameOn() {
        return isGameOn;
    }

    // Methods

    /**
     * Initialize the game
     */
    public void initGame() {
        // TODO: implement initGame().
    }

    // TODO: implement move more than one tile and move pov to a specific tile (id)
    // TODO: test if can be placed
    // TODO: implement turn 2 = turn right 2

    /**
     * Parses the command given by the player and executes it.
     * 
     * @param input  The command given by the player
     * @param player The player who gave the command
     * @return {@code true} if the command was executed and the turn should be
     *         finished, {@code false} otherwise
     */
    public boolean parseInput(String input, PlayerDomino player) {

        String[] args = input.split(" ");

        try {
            switch (args[0].toLowerCase()) {
                case "pass":
                    // TODO : implement pass()
                    return true;
                case "print":
                    if (args.length == 1) {
                        printBoard();
                    } else if (args.length == 2) {
                        if (args[1].toLowerCase().equals("b")) {
                            printBoard();
                        } else {
                            printTileToPlace();
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid number of arguments");
                    }
                    return false;
                case "printtile":
                    printTileToPlace();
                    return false;
                case "printboard":
                    printBoard(); 
                    return false;
                case "surrender":
                    surrender(player);
                    return true;
                case "move":
                    if (args.length != 2)
                        throw new IllegalArgumentException("Invalid number of arguments");

                    Direction direction = Placeable.stringToDirection(args[1].toUpperCase());
                    move(direction);
                    return false;
                case "turn":
                    if (args.length == 1) {
                        turn(true, 1);
                        printTileToPlace();

                    } else {
                        if (args.length != 3)
                            throw new IllegalArgumentException("Invalid number of arguments");

                        turn(args[1].toUpperCase().charAt(0) == 'R', Integer.parseInt(args[2]));
                        printTileToPlace();

                    }
                    return false;
                case "turnLeft":
                    if (args.length == 1) {
                        turn(false, 1);
                        printTileToPlace();

                    } else {
                        if (args.length != 2)
                            throw new IllegalArgumentException("Invalid number of arguments");

                        turn(false, Integer.parseInt(args[1]));
                        printTileToPlace();

                    }
                    return false;
                case "turnRight":
                    if (args.length == 1) {
                        turn(true, 1);
                        printTileToPlace();

                    } else {
                        if (args.length != 2)
                            throw new IllegalArgumentException("Invalid number of arguments");

                        turn(true, Integer.parseInt(args[1]));
                        printTileToPlace();

                    }
                    return false;
                case "place":
                    if (args.length != 3)
                        throw new IllegalArgumentException("Invalid number of arguments");
                    place(args[1], args[2], player);
                    return true;
                case "quit":
                    quit();
                    return true;
                default:
                    System.out.println("Invalid command");
                    return false;
            }
        } catch (Exception e) {
            System.out.println("Invalid command");
            System.out.println(e.getMessage());
            e.printStackTrace(); // TODO: remove this line in the final version
        }

        return false;
    }

    /**
     * Moves the current tile on the board in the given direction.
     * 
     * @param direction Direction to move the current tile
     * @throws TileNotFoundException If the tile to move is not on the board
     */
    public void move(Direction direction) throws TileNotFoundException {
        // TODO: Update this method to allow moving to null tiles (isOutOfBounds()).
        if (currentTileDomino.getNeighbor(direction) == null)
            throw new TileNotFoundException();

        currentTileDomino = currentTileDomino.getNeighbor(direction);

        switch (direction) {
            case UP:
                currentPosition.first--;
                break;
            case RIGHT:
                currentPosition.second++;
                break;
            case DOWN:
                currentPosition.first++;
                break;
            case LEFT:
                currentPosition.second--;
                break;
        }
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
        // only board.getNeighbors(x, y) i need to downcast it up to
        // Placeable<SideDomino>)
        board.getNeighbors(x, y).forEach(p -> {
            neighbors.add(new Pair<>(p.first, p.second));
            System.out.println(p.first + " " + p.second);
        });

        if (tileToPlace.canBePlaced(neighbors)) {
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
        } else
            throw new IllegalArgumentException("The tile cannot be placed at this position");

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
                int x = Integer.parseInt(arg1) - 1;
                int y = Integer.parseInt(arg2) - 1;
                place(x, y, player);
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
     * @throws UnableToTurnException
     */
    public void turn(boolean clockwise, int times) throws UnableToTurnException {
        if (tileToPlace == null)
            return;
        if (clockwise)
            tileToPlace.turnRight(times);
        else
            tileToPlace.turnLeft(times);
    }

    /**
     * Updates the game information when a round is over.
     */
    public void updateGameRound() {
        nbRounds++;
        currentPlayer = (currentPlayer + 1) % players.length;
        System.out.println(players[currentPlayer]);

        tileToPlace = deck.draw();
        printTileToPlace();

        printBoard();
    }

    /**
     * Plays a round for the player {@code} player}
     * 
     * @param player Player who plays the round
     * @return {@code true} if the command was executed and the turn
     *         should be finished, {@code false} otherwise
     */
    public boolean playRound(String command) {
        PlayerDomino player = players[currentPlayer];

        return parseInput(command, player);
    }

    /**
     * Helper function for {@link #printBoard()}. Returns an array of {@code String}
     * which represents a line of the relative board.
     * 
     * @param relativeBoard The relative board
     * @param y             Y coordinate of the line
     * @return An array of {@code String} which represents a line of the relative
     *         board
     */
    private String[] getStringLine(List<List<TileDomino>> relativeBoard, int y) {
        String[] lines = new String[TileDomino.COLUMNS_LENGTH];

        String voidLine = " ".repeat(TileDomino.LENGTH_OF_LINE);

        // Get the line number
        for (int j = 0; j < lines.length; j++)
            lines[j] = j == TileDomino.COLUMNS_LENGTH / 2 ? Integer.toString(y + 1) + " " : "  ";

        // Get the information of the lines
        for (int i = 0; i < relativeBoard.get(y).size(); i++) {
            // If the tile is null we add the corresponding spaces
            if (relativeBoard.get(y).get(i) == null) {
                for (int j = 0; j < lines.length; j++)
                    lines[j] += voidLine;

            } else {
                String[] stringRepresentation = relativeBoard.get(y).get(i).getStringRepresentation();
                for (int j = 0; j < lines.length; j++)
                    lines[j] += stringRepresentation[j];
            }
        }

        return lines;
    }

    /**
     * Prints on the screen the tile to be placed
     */
    public void printTileToPlace() {
        if (tileToPlace == null)
            return;

        System.out.println("Current tile: ");
        tileToPlace.printTile();
        System.out.println("-----------------------------------------------");
    }

    /**
     * Prints the relative board.
     */
    public void printBoard() {
        System.out.println("-------------------- Board --------------------");

        // Get the sub array to print
        List<List<TileDomino>> relativeBoard = board.getSubArray(currentPosition.first, currentPosition.second,
                NB_TILES_TO_SHOW);

        // Print the column numbers
        System.out.print("  ");

        String spaceString = " ".repeat((TileDomino.LENGTH_OF_LINE - 1) / 2);

        for (int i = 1; i <= relativeBoard.get(0).size(); i++) {
            System.out.print(spaceString + i + spaceString + " ");
        }

        System.out.println();

        // Print all the lines
        for (int i = 0; i < relativeBoard.size(); i++) {
            String[] lines = getStringLine(relativeBoard, i);

            for (String line : lines)
                System.out.println(line);
        }

        System.out.println("-----------------------------------------------");

    }

    /**
     * {@code player} surrenders the game.
     */
    public void surrender(PlayerDomino player) {
        System.out.println("Player " + player + " has surrendered");
        isGameOn = false;
    }

    /**
     * Quits the game.
     */
    public void quit() {
        // TODO: implement better way of doing it
        isGameOn = false;
    }

    public static void main(String[] args) {
        // Random tests for the moment
        GameDomino game = new GameDomino(2, 28);
        game.printBoard();// game.parseInput("d", null);

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

        game.printBoard();

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
        game.parseInput("place -1 0", null);
        game.tileToPlace = tile3;
        game.parseInput("place 1 -1", null);
        game.tileToPlace = tile2;
        game.parseInput("place 2 1", null);
        game.tileToPlace = tile1;
        game.parseInput("place 1 2", null);

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

        game.printBoard();

        System.out.println(game.currentPosition);

        // game.parseInput("move u", null);
        game.printBoard();

        System.out.println("-----------------------");

        game.board.forEach(System.out::println);

        System.out.println(game.currentPosition);

        System.out.println("-----------------------");

        game.printBoard();

    }
}
