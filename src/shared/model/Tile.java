package shared.model;

import interfaces.Placeable;

public abstract class Tile<T extends Side> implements Placeable<T> {

    /**
     * Returns the tile linked with the side indicated by {@code direction}
     */
    public abstract Tile<T> getNeighbor(Direction direction);

    /**
     * Returns an array of the tiles linked to each side of the tile. Null if a side
     * is not linked.
     */
    public abstract Tile<T>[] getNeighbors();

    // TODO : think about genericity for setNeighbor
    // public abstract <S extends Tile<T>> void setNeighbor(S tile, Direction
    // direction);

    // public abstract void setNeighbors(Tile<T>[] tiles, Direction[] directions);

    public abstract void validSides(T[] tab);
}
