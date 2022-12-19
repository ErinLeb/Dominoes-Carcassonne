package domino.model;

import java.util.ArrayList;
import java.util.List;

import shared.model.Tile;
import exceptions.UnableToTurnException;

import interfaces.Placeable;

import utilities.Pair;

//TODO: decide what to do with the methods that are not used
//TODO: decide what to do with the link between the tiles
//TODO: (E) implement toString()

/**
 * This class represents a domino tile.
 */
public class TileDomino extends Tile<SideDomino> {
    // Attributes
    public static final int LENGTH_OF_LINE = 10; // The length of the line in the getStringRepresentation method.
    public static final int COLUMNS_LENGTH = 7; // The length of the columns in the getStringRepresentation method.

    private static int nbTile = 0; // Total number of tiles generated

    private int id; // id of the tile

    // TODO : (E) put sides in final
    // TODO : (E)remove sides setters
    private SideDomino[] sides = new SideDomino[4]; // Sides of the tile
    // tab[0] = upperSide, tab[1] = right side, tab[2] = lower side, tab[3] = left
    // side

    private TileDomino[] neighbors = new TileDomino[4]; // Neighbors of the tile

    // Constructors

    // TODO: add linking of sides

    public TileDomino(SideDomino[] tab) {
        this.id = nbTile++;
        this.sides = tab;
    }

    /**
     * Creates a random tile with random figures on each side
     */
    public TileDomino() {
        // TODO : initialize id when the tile is placed on the board rather than at its
        // construction
        // The id represents the order in which tiles are placed, not the order in which
        // they are built
        this.id = nbTile++;

        this.sides = new SideDomino[4];
        for (int i = 0; i < 4; i++)
            this.sides[i] = new SideDomino();
    }

    /**
     * Creates a tile linked to {@code side} with the same figures on one side
     */
    public TileDomino(SideDomino side, Direction direction) {
        this();
        this.setSide(side, Placeable.getOpposite(direction));
        getSide(Placeable.getOpposite(direction)).setLinked(side);
    }

    /**
     * Creates a tile with the given figures on each side of {@code sides}
     */
    public TileDomino(SideDomino[] sides, Direction[] directions) {
        this(); // generate random sides first
        for (int i = 0; i < sides.length; i++)
            this.setSide(sides[i], directions[i]); // then set the sides given
    }

    // Getters

    public int getId() {
        return id;
    }

    public SideDomino[] getSides() {
        return sides;
    }

    @Override
    public SideDomino getSide(Direction direction) {
        return sides[directionToInt(direction)];
    }

    /**
     * Returns the list of sides which are not linked to another tile.
     * 
     * @return the list of sides which are not linked to another tile
     */
    public List<Pair<SideDomino, Direction>> getUnlinkedSides() {
        ArrayList<Pair<SideDomino, Direction>> unlinkedSides = new ArrayList<>();

        for (int i = 0; i < sides.length; i++) {
            if (!sides[i].isLinked())
                unlinkedSides.add(new Pair<>(sides[i], intToDirection(i)));
        }

        return unlinkedSides;
    }

    // TODO : (E) neighbors' getters should be in the interface Placeable

    /**
     * Returns the tile linked with the side indicated by {@code direction}
     */
    public TileDomino getNeighbor(Direction direction) {
        return neighbors[directionToInt(direction)];
    }

    /**
     * Returns an array of the tiles linked to each side of the tile. Null if a side
     * is not linked.
     */
    public TileDomino[] getNeighbors() {
        return neighbors;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    /**
     * The sides of the tile are replaced by {@code tab}
     */
    public void setAllSides(SideDomino[] tab) {
        // TODO : (E) verify the validity of tab (e.g. none of tab[i] is null)
        this.sides = tab;
    }

    @Override
    public void setSide(SideDomino side, Direction direction) {
        SideDomino toChange = this.sides[directionToInt(direction)];

        // if the side we want to change is already linked, we can't change it
        if (!toChange.isLinked()) {
            toChange = side;
        }
    }

    // TODO : (E) the neighbors' setters should be inherited from Placeable

    public void setNeighbor(TileDomino tile, Direction direction) {
        if (this.neighbors[directionToInt(direction)] != tile) { // test needed to avoid a loop
            this.neighbors[directionToInt(direction)] = tile;
            // The neighbor is also set on the other side
            tile.setNeighbor(this, Placeable.getOpposite(direction));
        }

    }

    public void setNeighbors(TileDomino[] tiles, Direction[] directions) {
        for (int i = 0; i < tiles.length; i++)
            setNeighbor(tiles[i], directions[i]);
    }

    // Methods

    @Override
    public boolean doesSideMatch(SideDomino side, Direction direction) {
        return sides[directionToInt(direction)].hasSameFig(side);
    }

    /**
     * Turns left the {@code sides} {@code n} times.
     * 
     * @param n Number of times to turn
     * @throws UnableToTurnException If the tile is already linked to at least one
     *                               tile
     */
    public void turnLeft(int n) throws UnableToTurnException {
        if (isLinked()) {
            throw new UnableToTurnException();
        }
        n = n % sides.length;

        // Rotate the sides array by n times toward left
        for (int i = 0; i < n; i++) {
            int j;
            SideDomino first = sides[0];
            // Stores the first element of the array

            for (j = 0; j < sides.length - 1; j++) {
                // Shift element of array by one
                sides[j] = sides[j + 1];
            }
            // First element of array will be put at the end of the array
            sides[j] = first;
        }
    }

    /**
     * Turns right the {@code sides} {@code n} times.
     * 
     * @param n Number of times to turn
     * @throws UnableToTurnException If the tile is already linked to at least one
     *                               tile
     */

    public void turnRight(int n) throws UnableToTurnException {
        if (isLinked())
            throw new UnableToTurnException();

        n = n % sides.length;

        // Rotate the sides array by n times toward right
        for (int i = 0; i < n; i++) {
            int j;
            SideDomino last = sides[sides.length - 1];
            // Stores the last element of array

            for (j = sides.length - 1; j > 0; j--)
                // Shift element of array by one
                sides[j] = sides[j - 1];

            // Last element of array will be put at the start of the array.
            sides[0] = last;
        }
    }

    /**
     * Returns {@code true} if the tile is already linked to another.
     * 
     * @return {@code true} if the tile is already linked to another.
     */
    public boolean isLinked() {
        for (SideDomino side : sides)
            if (side.isLinked())
                return true;

        for (TileDomino neighbor : neighbors)
            if (neighbor != null)
                return true;

        return false;
    }

    /**
     * Unlinks all the sides of the tile.
     */
    public void unlink() {
        for (SideDomino side : sides)
            side.unlink();
    }

    /**
     * Returns a list of strings which represents the tile.
     * Every element of the list represents a line of the tile.
     * 
     * @return a list of strings which represents the tile
     */
    public String[] getStringRepresentation() {

        String line0 = " |" + sides[0] + "|  ";

        String[] sideValues1 = sides[1].toString().split(" ");
        String[] sideValues3 = sides[3].toString().split(" ");
        String vide = " ".repeat(5);

        String line3 = sideValues3[0] + "|" + vide + "|" + sideValues1[0] + " ";
        String line5 = sideValues3[1] + "|" + " " + String.format("%03d", id) + " " + "|" + sideValues1[1] + " ";
        String line7 = sideValues3[2] + "|" + vide + "|" + sideValues1[2] + " ";

        String line9 = " |" + sides[2] + "|  ";
        String separatorLine = "--" + "-".repeat(5) + "-- ";

        return new String[] { line0, separatorLine, line3, line5, line7, separatorLine, line9 };
    }

    /**
     * Prints the result from {@link #getStringRepresentation()}.
     */
    public void printTile() {
        String[] tileInfo = getStringRepresentation();
        for (String line : tileInfo)
            System.out.println(line);
    }

    public static void main(String[] args) {
        // Test
        SideDomino[] tab = new SideDomino[4];
        tab[0] = new SideDomino(new int[] { 0, 0, 0 });
        tab[1] = new SideDomino(new int[] { 1, 1, 1 });
        tab[2] = new SideDomino(new int[] { 2, 2, 2 });
        tab[3] = new SideDomino(new int[] { 3, 3, 3 });

        TileDomino tile = new TileDomino(tab);

        String[] tileInfo = tile.getStringRepresentation();
        for (String line : tileInfo)
            System.out.println(line.repeat(5));

        for (int i = 0; i < 4; i++) {
            new TileDomino().printTile();
        }

        System.out.println("Turn left");

        for (int i = 0; i < 4; i++) {
            try {
                tile.turnLeft(1);
                tile.printTile();

            } catch (UnableToTurnException e) {
                System.out.println("Unable to turn");
            }

        }

        System.out.println("Turn right");

        for (int i = 0; i < 4; i++) {
            try {
                tile.turnRight(1);
                tile.printTile();

            } catch (UnableToTurnException e) {
                System.out.println("Unable to turn");
            }

        }

        try {
            tile.turnRight(4);
            tile.printTile();

        } catch (UnableToTurnException e) {
            System.out.println("Unable to turn");
        }

        try {
            tile.turnLeft(4);
            tile.printTile();

        } catch (UnableToTurnException e) {
            System.out.println("Unable to turn");
        }

        try {
            tile.turnRight(3);
            tile.printTile();

        } catch (UnableToTurnException e) {
            System.out.println("Unable to turn");
        }

        try {
            tile.turnLeft(3);
            tile.printTile();

        } catch (UnableToTurnException e) {
            System.out.println("Unable to turn");
        }

    }

}
