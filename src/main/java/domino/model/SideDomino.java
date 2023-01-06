package domino.model;

import java.util.Arrays;
import java.util.Random;

import shared.model.Side;

/**
 * This class represents the side of a domino tile.
 */
public class SideDomino extends Side {
    // Attributes
    public static final int MAX_VALUE = 3; // Max value of a side

    private final int[] fig; // The figures on the side

    // Constructors
    public SideDomino(int[] tab) {
        if (isValidFig(tab)) {
            fig = tab;
        } else
            throw new IllegalArgumentException("The array is not valid");
    }

    /**
     * Creates a random side.
     */
    public SideDomino() {
        Random rand = new Random();

        fig = new int[3];
        for (int i = 0; i < 3; i++) {
            fig[i] = rand.nextInt(MAX_VALUE + 1);
        }
    }

    // Getters
    public int[] getFig() {
        return fig;
    }

    /**
     * Returns the sum of the figures on the side.
     * 
     * @return The sum of the figures on the side.
     */
    public int getFigSum() {
        int sum = 0;
        for (int i : fig)
            sum += i;
        return sum;
    }

    // Methods

    /**
     * Verifies if {@code tab} is an array of 3 integers between 0 and
     * {@code MAX_VALUE}
     * 
     * @param tab the array to check
     * @return {@code true} if {@code tab} is an array of 3 integers between 0 and
     *         {@code MAX_VALUE}
     */
    public static boolean isValidFig(int[] tab) {
        if (tab.length != 3)
            return false;

        for (int i = 0; i < 3; i++) {
            if (tab[i] < 0 || tab[i] > MAX_VALUE) {
                return false;
            }
        }

        return true;
    }

    /**
     * Reverse the order of the figures on the side. Useful to turn correctly the
     * side.
     */
    public void reverseOrder() {
        int tmp = fig[0];
        fig[0] = fig[2];
        fig[2] = tmp;
    }

    /**
     * Returns {@code true} if the figures are identical to the figures on
     * {@code side}.
     * 
     * @param side {@code SideDomino} to compare
     * @return {@code true} if the figures are identical to the figures on
     *         {@code side}
     *
     */
    public boolean hasSameFig(SideDomino side) {
        return Arrays.equals(fig, side.getFig());
    }

    /**
     * Returns a copy of the side.
     * 
     * @return a copy of the side
     */
    @Override
    public SideDomino copy() {
        return new SideDomino(new int[] { fig[0], fig[1], fig[2] });
    }

    @Override
    public String toString() {
        return fig[0] + " " + fig[1] + " " + fig[2];
    }

}
