package shared.model;

import java.util.List;

import interfaces.Placeable;
import utilities.Pair;

public abstract class Tile<T extends Side> implements Placeable<T> {
    // Getters

    // TODO : remove functions about links if Carcassonne doesn't use it

    /**
     * Returns the list of sides which are not linked to another tile.
     * 
     * @return the list of sides which are not linked to another tile
     */
    public abstract List<Pair<T, Direction>> getUnlinkedSides();

    /**
     * Returns the tile linked with the side indicated by {@code direction}
     *
     * @return the tile linked with the side indicated by {@code direction}
     */
    public abstract Tile<T> getNeighbor(Direction direction);

    /**
     * Returns an array of the tiles linked to each side of the tile. Null if a side
     * is not linked.
     *
     * @return an array of the tiles linked to each side of the tile. Null if a side
     *         is not linked.
     */
    public abstract Tile<T>[] getNeighbors();

    // Setters

    /**
     * Sets the side of the object in the direction {@code direction} to
     * {@code side}.
     * 
     * @param side      the side to set
     * @param direction the direction of the side to set
     */
    public abstract void setSide(T side, Direction direction);

    /**
     * The sides of the tile are replaced by {@code tab}.
     * 
     * @param tab the new sides of the tile
     */
    public abstract void setAllSides(T[] tab);

    // Methods

    public abstract void validSides(T[] tab);

    /**
     * Returns {@code true} if the tile is already linked to another.
     * 
     * @return {@code true} if the tile is already linked to another.
     */
    public abstract boolean isLinked();

    /**
     * Unlinks all the sides of the tile.
     */
    public abstract void unlink();

    /**
     * Returns a copy of the tile.
     * 
     * @return a copy of the tile
     */
    public abstract Tile<T> copy();

    // TODO : decide if turn methods must be in Placeable or here

}
