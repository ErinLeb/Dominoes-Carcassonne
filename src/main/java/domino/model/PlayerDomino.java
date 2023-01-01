package domino.model;

import shared.model.Player;

/**
 * Represents a player of the domino game.
 */
public class PlayerDomino extends Player {
    // Attributes
    private static int nbPlayers;
    private int id;

    // Constructors

    public PlayerDomino() {
        super();
        id = nbPlayers++;
        name = "Player " + id;
    }

    public PlayerDomino(String name) {
        super(name);
        id = nbPlayers++;
    }

    // Getters

    public static int getNbPlayers() {
        return nbPlayers;
    }

    public int getId() {
        return id;
    }

    // Methods

    /**
     * Prints the player's id, name and score.
     */
    public void print() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Player " + id + " : " + name + "\nScore : " + score;
    }

}
