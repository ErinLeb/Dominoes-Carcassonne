package shared.model;

public class Player {
    protected int score = 0;
    protected String name;
    protected boolean isInGame = true;

    // Constructor
    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    // Getters

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public boolean isInGame() {
        return isInGame;
    }

    // Setters

    public void setInGame(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Methods

    /**
     * Resets the score to 0.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Add {@code points} to the score of the player
     * 
     * @param points
     */
    public void incrementScore(int points) {
        this.score += points;
    }

    /**
     * Returns {@code true} if the score of the player is equal to p's score
     * 
     * @param p
     * @return {@code true} if the score of the player is equal to p's score
     */
    public boolean isScoreEqual(Player p) {
        return score == p.score;
    }

}