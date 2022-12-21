package domino.model;

import java.util.Arrays;
import java.util.Random;

import shared.model.Side;

//TODO: decide what to do with the link between the sides.

/**
 * This class represents the side of a domino tile.
 */
public class SideDomino extends Side {
    // Attributes
    public static final int MAX_VALUE = 3; // Max value of a side

    private final int[] fig; // The figures on the side
    private SideDomino linked = null; // The linked side

    // Constructors
    public SideDomino(int[] tab) {
        validFig(tab);
        fig = tab;
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

    public SideDomino getLinked() {
        return linked;
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

    // Setters

    public void setLinked(SideDomino linked) {
        if (linked != this.linked) {
            this.linked = linked;
            // The link is also set on the other side
            linked.setLinked(this);
        }
    }

    // Methods

    /**
     * Verifies if {@code tab} is an array of 3 integers between 0 and
     * {@code MAX_VALUE}
     */
    public static void validFig(int[] tab) {
        if (tab.length != 3) {
            throw new IllegalArgumentException("Length of the array is illegal");
        }
        for (int i = 0; i < 3; i++) {
            if (tab[i] < 0 || tab[i] > MAX_VALUE) {
                throw new IllegalArgumentException("Figures in the array are not correct");
            }
        }
    }

    /**
     * Returns {@code true} if the side is linked to another side.
     * 
     * @return {@code true} if the side is linked to another side
     */
    public boolean isLinked() {
        return linked != null;
    }

    /**
     * Returns {@code true} if the side is linked to {@code side}
     * 
     * @param side the side to check
     */
    public boolean isLinkedTo(SideDomino side) {
        return linked.equals(side);
    }

    /**
     * Sets the linked side to {@code null}.
     */
    public void unlink() {
        linked = null;
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
    public SideDomino copy() {
        return new SideDomino(new int[] { fig[0], fig[1], fig[2] });
    }

    @Override
    public String toString() {
        return fig[0] + " " + fig[1] + " " + fig[2];
    }

}
