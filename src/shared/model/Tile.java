package shared.model;

import java.util.List;

import domino.model.SideDomino;
import exceptions.UnableToTurnException;
import interfaces.Placeable;
import utilities.Pair;

public abstract class Tile<T extends Side> implements Placeable<T> {

    protected T[] sides; // Sides of the tile
    // tab[0] = upperSide, tab[1] = right side, tab[2] = lower side, tab[3] = left
    // side

    protected boolean isPlaced = false; // True if the tile is placed on the board

    // Getters

    @Override
    public T getSide(Direction direction) {
        return sides[directionToInt(direction)];
    }

    @Override
    public T[] getSides() {
        return sides;
    }

    // Setters

    /**
     * Sets the side of the object in the direction {@code direction} to
     * {@code side}.
     * 
     * @param side      the side to set
     * @param direction the direction of the side to set
     */
    public void setSide(T side, Direction direction) {
        this.sides[directionToInt(direction)] = side;
    }

    /**
     * The sides of the tile are replaced by {@code tab}.
     * 
     * @param tab the new sides of the tile
     */
    public void setAllSides(T[] tab) {
        validSides(tab);
        this.sides = tab;
    }
    // Methods

    public abstract boolean validSides(T[] tab);

    /**
     * Returns a copy of the tile.
     * 
     * @return a copy of the tile
     */
    public abstract Tile<T> copy();

    // TODO : decide if turn methods must be in Placeable or here

    /**
     * Turns left the {@code sides} {@code n} times.
     * 
     * @param n Number of times to turn
     * @throws UnableToTurnException If the tile is already placed on the board
     */
    public void turnLeft(int n) throws UnableToTurnException {
        if (isPlaced) {
            throw new UnableToTurnException();
        }
        n = n % sides.length;

        // Rotate the sides array by n times toward left
        for (int i = 0; i < n; i++) {
            int j;
            T first = sides[0];
            // Stores the first element of the array

            for (j = 0; j < sides.length - 1; j++) {
                // Shift element of array by one
                sides[j] = sides[j + 1];
            }
            // First element of array will be put at the end of the array
            sides[j] = first;

            if (first instanceof SideDomino) {
                // Reverse the order of the sides
                ((SideDomino) sides[1]).reverseOrder();
                ((SideDomino) sides[3]).reverseOrder();
            }
        }
    }

    /**
     * Turns right the {@code sides} {@code n} times.
     * 
     * @param n Number of times to turn
     * @throws UnableToTurnException If the tile is already placed on the board
     */
    public void turnRight(int n) throws UnableToTurnException {
        if (isPlaced)
            throw new UnableToTurnException();

        n = n % sides.length;

        // Rotate the sides array by n times toward right
        for (int i = 0; i < n; i++) {
            int j;
            T last = sides[sides.length - 1];
            // Stores the last element of array

            for (j = sides.length - 1; j > 0; j--)
                // Shift element of array by one
                sides[j] = sides[j - 1];

            // Last element of array will be put at the start of the array.
            sides[0] = last;

            if (last instanceof SideDomino) {
                // Reverse the order of the sides
                ((SideDomino) sides[0]).reverseOrder();
                ((SideDomino) sides[2]).reverseOrder();
            }
        }
    }

    @Override
    public boolean canBePlacedWithRotation(List<Pair<Placeable<T>, Direction>> tiles) {
        for (int z = 0; z < 4; z++) {
            if (canBePlaced(tiles))
                return true;

            try {
                turnRight(1);
            } catch (UnableToTurnException e) {
                //
            }
        }
        return false;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }

}
