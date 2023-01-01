package carcassonne.model;

import java.awt.Color;

import shared.model.Player;

public class PlayerCarcassonne extends Player {
    // Atributes

    private static int nbPlayers;
    private int id;
    protected int remainingPawns = 10;

    private final Color colorPawn;

    // Constructor
    public PlayerCarcassonne(Color colorPawn) {
        super();
        id = nbPlayers++;
        name = "Player " + id;
        this.colorPawn = colorPawn;
    }

    public PlayerCarcassonne(String name, Color colorPawn) {
        super(name);
        id = nbPlayers++;
        this.colorPawn = colorPawn;
    }

    // Getters

    public static int getNbPlayers() {
        return nbPlayers;
    }

    public int getId() {
        return id;
    }

    public Color getColorPawn() {
        return colorPawn;
    }

    public int getRemainingPawns() {
        return remainingPawns;
    }

    public void decreaseRemainingPawns() {
        remainingPawns--;
    }

    public void increaseRemainingPawns() {
        remainingPawns++;
    }
}
