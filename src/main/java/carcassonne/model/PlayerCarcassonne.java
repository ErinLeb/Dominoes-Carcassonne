package carcassonne.model;

import shared.model.Player;

public class PlayerCarcassonne extends Player {
    // Atributes

    private static int nbPlayers;
    private int id;

    // Constructor
    public PlayerCarcassonne() {
        super();
        id = nbPlayers++;
        name = "Player " + id;
    }

    public PlayerCarcassonne(String name) {
        super(name);
    }

    // Getters

    public static int getNbPlayers() {
        return nbPlayers;
    }

    public int getId() {
        return id;
    }
}
