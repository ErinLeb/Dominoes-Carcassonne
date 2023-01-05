package domino.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domino.model.SideDomino;
import domino.model.TileDomino;
import shared.view.TilePanel;

public class TileDominoPanel extends TilePanel<TileDomino> {

    private TileDomino tileModel;

    public TileDominoPanel(TileDomino model) {
        this.tileModel = model;

        if (model == null) {
            initNull();
        } else {
            init();
        }
    }

    /**
     * Initializes the panel with a null model.
     */

    private void initNull() {
        setLayout(new GridLayout(1, 0));

        JPanel square = new JPanel();
        square.setBackground(Color.WHITE);
        square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(square);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(125, 125);
    }

    /**
     * Initializes the panel with the model.
     */
    private void init() {
        setLayout(new GridLayout(5, 5));

        SideDomino[] sides = tileModel.getSides();

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
     * Generates a square with a number and a black border.
     * 
     * @param number the number to display
     * @return a square with a number and a black border
     */
    private JPanel generateNumberSquare(int number) {
        JPanel square = new JPanel();
        square.setBackground(Color.WHITE);
        square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
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
     * Updates the model of the panel.
     * 
     * @param model the new model
     */
    public void updateModel(TileDomino model) {
        this.tileModel = model;
        update();
    }

    /**
     * Updates the panel.
     */
    public void update() {
        removeAll();
        if (tileModel == null) {
            initNull();
        } else {
            init();
        }
        revalidate();
        repaint();
    }

}
