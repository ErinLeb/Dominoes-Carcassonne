package domino.model;

import java.util.ArrayList;
import java.util.List;

import interfaces.Placeable;
import interfaces.Placeable.Direction;
import shared.model.Game;
import shared.model.Player;
import utilities.Expandable2DArray;
import utilities.Pair;

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
        for (int i = 0; i < nbPlayers; i++)
            players[i] = new PlayerDomino();

        // Creation of the deck
        deck = new DeckDomino(nbTiles);
        deckSize = nbTiles;

        currentTileDomino = deck.draw();
        currentPosition = new Pair<>(0, 0);
        board = new Expandable2DArray<>(currentTileDomino);

        currentPlayer = 0;

        deck.shuffle();
    }

    public GameDomino(PlayerDomino[] players, int nbTiles) {
        // Creation of the players
        this.players = players;
        // Creation of the deck
        deck = new DeckDomino(nbTiles);
        deckSize = nbTiles;

        currentTileDomino = deck.draw();
        currentPosition = new Pair<>(0, 0);
        board = new Expandable2DArray<>(currentTileDomino);

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
        currentTileDomino = deck.draw();
        currentPosition = new Pair<>(0, 0);

        // board
        board = new Expandable2DArray<>(currentTileDomino);

        // currentPlayer
        currentPlayer = 0;

        // nbRounds
        nbRounds = 0;

        // isGameOn
        isGameOn = true;
    }

    @Override
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
     * @return the amount of points the player would score
     */
    public int pointsIfPlaced(int x, int y, TileDomino tile) {

        if (tile == null)
            throw new IllegalArgumentException("Tile is null");

        if (!board.isInsideExpandableBounds(x, y))
            throw new IndexOutOfBoundsException();

        if (!board.isOutOfBounds(x, y) && board.get(x, y) != null)
            throw new IllegalArgumentException("There is already a tile at this position");

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

    public static void main(String[] args) {
        // Random tests for the moment
        /*
         * GameDomino game = new GameDomino(2, 28);
         * // game.printBoard();// game.parseInput("d", null);
         * 
         * int[] tab0 = { 0, 0, 0 };
         * int[] tab1 = { 1, 1, 1 };
         * int[] tab2 = { 2, 2, 2 };
         * int[] tab3 = { 3, 3, 3 };
         * 
         * SideDomino side0 = new SideDomino(tab0);
         * SideDomino side1 = new SideDomino(tab1);
         * SideDomino side2 = new SideDomino(tab2);
         * SideDomino side3 = new SideDomino(tab3);
         * 
         * TileDomino initialTile = new TileDomino(new SideDomino[] { side0, side1,
         * side2, side3 });
         * 
         * game.currentTileDomino = initialTile;
         * game.board.set(0, 0, initialTile);
         * 
         * // game.printBoard();
         * 
         * SideDomino[] sides0 = new SideDomino[] { side0, side0, side0, side0 };
         * SideDomino[] sides1 = new SideDomino[] { side1, side1, side1, side1 };
         * SideDomino[] sides2 = new SideDomino[] { side2, side2, side2, side2 };
         * SideDomino[] sides3 = new SideDomino[] { side3, side3, side3, side3 };
         * 
         * TileDomino tile0 = new TileDomino(sides0);
         * TileDomino tile1 = new TileDomino(sides1);
         * TileDomino tile2 = new TileDomino(sides2);
         * TileDomino tile3 = new TileDomino(sides3);
         * 
         * game.currentTileDomino.setNeighbors(new TileDomino[] { tile0, tile1, tile2,
         * tile3 }, Direction.values());
         * 
         * System.out.println("-----------------------");
         * 
         * System.out.println("Testing place");
         * System.out.println("-----------------------");
         * 
         * game.tileToPlace = tile0;
         * // game.parseInput("place -1 0", null);
         * game.tileToPlace = tile3;
         * // game.parseInput("place 1 -1", null);
         * game.tileToPlace = tile2;
         * // game.parseInput("place 2 1", null);
         * game.tileToPlace = tile1;
         * // game.parseInput("place 1 2", null);
         * 
         * // System.out.println("----------------------------");
         * // System.out.println(game.board.toString());
         * // game.board.add(-1, 0, tile0);
         * // System.out.println("----------------------------");
         * // System.out.println(game.board.toString());
         * // game.board.add(1, -1, tile3);
         * // System.out.println("----------------------------");
         * // System.out.println(game.board.toString());
         * // game.board.add(2, 1, tile2);
         * // System.out.println("----------------------------");
         * // System.out.println(game.board.toString());
         * // game.board.add(1, 2, tile1);
         * 
         * System.out.println("-----------------------");
         * 
         * System.out.println("Current tile: " + game.currentTileDomino);//
         * game.currentTileDomino.getNeighbor(Direction.UP).printTile();
         * game.currentPosition.first = 1;
         * game.currentPosition.second = 1;
         * 
         * System.out.println("-----------------------");
         * 
         * // game.printBoard();
         * 
         * System.out.println(game.currentPosition);
         * 
         * // game.parseInput("move u", null);
         * // game.printBoard();
         * 
         * System.out.println("-----------------------");
         * 
         * game.board.forEach(System.out::println);
         * 
         * System.out.println(game.currentPosition);
         * 
         * System.out.println("-----------------------");
         * 
         * // game.printBoard();
         */

        PlayerDomino p1 = new PlayerDomino("Erin");
        PlayerDomino p2 = new PlayerDomino("Yago");
        PlayerDomino p3 = new PlayerDomino("Surrendered");

        PlayerDomino[] players = { p1, p2, p3 };
        GameDomino game = new GameDomino(players, 4);

        p1.incrementScore(8);
        p2.incrementScore(10);
        p3.incrementScore(12);
        game.surrender(p3);

        Player[] rk = game.getRanking();
        for (Player p : rk) {
            System.out.println(p);
        }

    }

}
