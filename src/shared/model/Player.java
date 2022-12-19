package shared.model;

public class Player {
    private int score = 0;

    // Constructor
    public Player() {
    }

    // Getters
    public int getScore() {
        return score;
    }

    // Methods
    /**
     * Add {@code points} to the score of the player
     * 
     * @param score
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Returns {@code true} if the score of the player is equal to p's score
     * 
     * @param p
     */
    public boolean isScoreEqual(Player p) {
        return score == p.score;
    }
}