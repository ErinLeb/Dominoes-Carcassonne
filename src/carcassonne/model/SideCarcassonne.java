package carcassonne.model;

import shared.model.Side;

public class SideCarcassonne /* extends Side */ {
    /**
     * Represents the type of the side
     */
    enum Type {
        CITY, MEADOW, PATH
    }

    // Attributes
    private Type type;

    // Constructor
    public SideCarcassonne(Type type) {
        this.type = type;
    }

    // Getters
    public Type getType() {
        return type;
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }
    // Methods
}
