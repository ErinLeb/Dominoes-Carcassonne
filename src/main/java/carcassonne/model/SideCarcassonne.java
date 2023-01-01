package carcassonne.model;

import shared.model.Side;

public class SideCarcassonne extends Side {
    /**
     * Represents the type of the side
     */
    public enum Type {
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

    public SideCarcassonne copy() {
        return new SideCarcassonne(type);
    }

    public boolean hasSameType(SideCarcassonne side) {
        return type == side.getType();
    }

    public String toString() {
        switch (type) {
            case CITY:
                return "city";
            case MEADOW:
                return "meadow";
            case PATH:
                return "path";
            default:
                return "";
        }
    }

}
