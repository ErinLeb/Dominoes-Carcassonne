package domino.view.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domino.model.SideDomino;
import domino.model.TileDomino;

public class TileDominoGui extends JPanel {

    private TileDomino model;

    public TileDominoGui(TileDomino model) {
        this.model = model;

        if (model == null) {
            initNull();
        } else {
            init();
        }
    }

    /**
     * Generates a black square.
     * 
     * @return a black square
     */
    private JPanel generateBlackSquare() {
        JPanel square = new JPanel();
        square.setBackground(Color.BLACK);
        return square;
    }

    /**
     * Generates a white square.
     * 
     * @return a white square
     */
    private JPanel generateWhiteSquare() {
        JPanel square = new JPanel();
        square.setBackground(Color.WHITE);
        return square;
    }

    /**
     * Generates a square with a number and a black border.
     * 
     * @param number the number to display
     * @return a square with a number and a black border
     */
    private JPanel generateNumberSquare(int number) {
        JPanel square = new JPanel();
        square.setBackground(Color.WHITE);
        square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        square.add(new JLabel(Integer.toString(number)));
        return square;
    }

    /**
     * Adds three white squares to the panel.
     */
    private void addWhiteSquares() {
        add(generateWhiteSquare());
        add(generateWhiteSquare());
        add(generateWhiteSquare());
    }

    /**
     * Initializes the panel with the model.
     */
    private void init() {
        setLayout(new GridLayout(5, 5));

        SideDomino[] sides = model.getSides();

        // First row

        add(generateBlackSquare());
        for (int i : sides[0].getFig()) {
            add(generateNumberSquare(i));
        }
        add(generateBlackSquare());

        // Second to fourth rows
        for (int i = 0; i < 3; i++) {
            add(generateNumberSquare(sides[3].getFig()[i]));
            addWhiteSquares();
            add(generateNumberSquare(sides[1].getFig()[i]));
        }

        // Fifth row

        add(generateBlackSquare());
        for (int i : sides[2].getFig()) {
            add(generateNumberSquare(i));
        }
        add(generateBlackSquare());
    }

    /**
     * Initializes the panel with a null model.
     */

    private void initNull() {
        setLayout(new GridLayout(5, 5));

        for (int i = 0; i < 25; i++) {
            add(generateWhiteSquare());
        }

    }

    /**
     * Updates the panel.
     */
    public void update() {
        removeAll();
        init();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {

        // Test with a non-null tile
        TileDominoGui tile = new TileDominoGui(new TileDomino());
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.add(tile);
        frame.pack();
        frame.setVisible(true);

        // Test with a null tile
        TileDominoGui tile2 = new TileDominoGui(null);
        javax.swing.JFrame frame2 = new javax.swing.JFrame();
        frame2.add(tile2);
        frame2.pack();
        frame2.setVisible(true);

    }

}
