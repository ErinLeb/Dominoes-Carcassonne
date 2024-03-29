package domino.model;

import interfaces.Placeable;
import shared.model.Tile;
import utils.StringOperations;

/**
 * This class represents a domino tile.
 */
public class TileDomino extends Tile<SideDomino> {
    // Attributes
    public static final int LENGTH_OF_LINE = 10; // The length of the line in the getStringRepresentation method.
    public static final int COLUMNS_LENGTH = 7; // The length of the columns in the getStringRepresentation method.

    // Constructors
    public TileDomino(SideDomino[] tab) {
        if (validSides(tab)) {
            this.sides = tab;
        } else {
            throw new IllegalArgumentException("The sides of the tile are not valid");
        }
    }

    /**
     * Creates a random tile with random figures on each side
     */
    public TileDomino() {
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

    @Override
    public SideDomino[] getSides() {
        return sides;
    }

    @Override
    public SideDomino getSide(Direction direction) {
        return sides[Placeable.directionToInt(direction)];
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setAllSides(SideDomino[] tab) {
        validSides(tab);
        this.sides = tab;
    }

    @Override
    public void setSide(SideDomino side, Direction direction) {
        this.sides[Placeable.directionToInt(direction)] = side;
    }

    // Methods

    @Override
    public boolean validSides(SideDomino[] tab) {
        if (tab.length != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (tab[i] == null || !SideDomino.isValidFig(tab[i].getFig())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean doesSideMatch(SideDomino side, Direction direction) {
        return sides[Placeable.directionToInt(direction)].hasSameFig(side);
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
        String vide = StringOperations.repeat(" ", 5);

        String line3 = sideValues3[0] + "|" + vide + "|" + sideValues1[0] + " ";
        String line5 = sideValues3[1] + "|" + " " + String.format("%03d", id) + " " + "|" + sideValues1[1] + " ";
        String line7 = sideValues3[2] + "|" + vide + "|" + sideValues1[2] + " ";

        String line9 = " |" + sides[2] + "|  ";
        String separatorLine = "--" + StringOperations.repeat("-", 5) + "-- ";

        return new String[] { line0, separatorLine, line3, line5, line7, separatorLine, line9 };
    }

    /**
     * Prints the tile line by line, using the result from
     * {@link #getStringRepresentation()}.
     */
    public void printTile() {
        String[] tileInfo = getStringRepresentation();
        for (String line : tileInfo)
            System.out.println(line);
    }

    @Override
    public TileDomino copy() {
        return new TileDomino(new SideDomino[] { sides[0].copy(), sides[1].copy(), sides[2].copy(), sides[3].copy() });
    }
}
