package domino;

import java.util.Arrays;
import java.util.Random;

import elements.Side;

//TODO: decide what to do with the link between the sides.

/**
 * This class represents the side of a domino tile.
 */
public class SideDomino extends Side {
    // Attributes
    public static final int MAX_VALUE = 3; // Max value of a side

    // TODO : (E) put fig final
    // TODO : (E) remove fig setters
    private int[] fig = new int[3]; // The figures on the side
    private SideDomino linked = null; // The linked side

    // Constructors
    public SideDomino(int[] tab) {
        // TODO : (E) fonction qui vérifie la validité de tab
        fig = tab;
    }

    public SideDomino() {
        Random rand = new Random();

        for (int i = 0; i < 3; i++)
            fig[i] = rand.nextInt(MAX_VALUE + 1);
    }

    // Getters
    public int[] getFig() {
        return fig;
    }

    public SideDomino getLinked() {
        return linked;
    }

    public int getFigSum() {
        int sum = 0;
        for (int i : fig)
            sum += i;
        return sum;
    }

    // Setters

    public void setFig(int[] fig) {
        this.fig = fig;
    }

    public void setLinked(SideDomino linked) {
        if (linked != this.linked) {
            this.linked = linked;
            // The link is also set on the other side
            linked.setLinked(this);
        }
    }

    // Methods

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

    @Override
    public String toString() {
        return fig[0] + " " + fig[1] + " " + fig[2];
    }

}
