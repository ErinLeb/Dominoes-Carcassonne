package shared.model;

public abstract class Side {
    // Setters
    // public abstract <S extends Side> void setLinked(S linked);

    // Methods

    // TODO : check if Carcassonne Sides need isLinked() and unlink()

    /**
     * Returns {@code true} if the side is linked to another side.
     * 
     * @return {@code true} if the side is linked to another side
     */
    public abstract boolean isLinked();

    /**
     * Sets the linked side to {@code null}.
     */
    public abstract void unlink();
}
