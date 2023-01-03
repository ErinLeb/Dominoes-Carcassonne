package carcassonne.model;

import java.awt.Color;

import shared.model.Player;

public class PlayerCarcassonne extends Player {
    // Atributes

    private static int nbPlayers;
    private int id;
    protected int remainingPawns = 10;

    private final Color pawnColor;

    // Constructor
    public PlayerCarcassonne(Color colorPawn) {
        super();
        id = nbPlayers++;
        name = "Player " + id;
        this.pawnColor = colorPawn;
    }

    public PlayerCarcassonne(String name, Color colorPawn) {
        super(name);
        id = nbPlayers++;
        this.pawnColor = colorPawn;
    }

    // Getters

    public static int getNbPlayers() {
        return nbPlayers;
    }

    public int getId() {
        return id;
    }

    public Color getPawnColor() {
        return pawnColor;
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
