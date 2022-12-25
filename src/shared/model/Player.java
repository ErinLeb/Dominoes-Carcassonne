package shared.model;

public class Player {
    protected int score = 0;

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