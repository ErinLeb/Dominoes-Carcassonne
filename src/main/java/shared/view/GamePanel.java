package shared.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import interfaces.Placeable.Direction;
import shared.model.Game;
import shared.model.Side;
import shared.model.Tile;
import utils.GraphicalImageSetter;

/**
 * Abstract class that contains the common elements of the game panel.
 */
public abstract class GamePanel<S extends Side, T extends Tile<S>> extends JPanel {

    protected JFrame frame;

    protected Game<S, T> gameModel;

    protected JLabel playerLabel;
    protected JLabel scoreLabel;
    protected JLabel nbTiles;
    protected JLabel infoScreenLabel;

    protected static final Color LIGHT_RED = new Color(0xde1738);

    protected boolean wantedToPass = false;

    protected GridBagConstraints constraints = new GridBagConstraints();

    /**
     * Initializes the panel.
     */
    protected void init() {
        setLayout(new GridBagLayout());

        // Information elements (player, score, info screen, remaining tiles)
        initInformationElements();

        // Buttons (quit, pass, surrender)
        initRedButtons();

        // Move buttons
        initMoveButtons();

        // Board
        initBoard();

        // Tile to place
        initTileToPlace();

        // Turn buttons
        initTurnButtons();

    }

    protected void initInformationElements() {
        // Player
        constraints.gridx = 0;
        constraints.gridy = 0;
        playerLabel.setPreferredSize(new Dimension(150, 50));
        playerLabel.setHorizontalAlignment(JLabel.CENTER);

        add(playerLabel, constraints);

        initScore();

        // Info screen
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(0, 75, 0, 0); // Top, left, bottom, right
        add(infoScreenLabel, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridwidth = 1;

        // Remaining tiles
        constraints.gridx = 0;
        constraints.gridy = 8;
        nbTiles.setIcon(GraphicalImageSetter.initImageAsImageIcon("src/main/resources/bag.png"));
        add(nbTiles, constraints);
    }

    protected abstract void initScore();

    protected void initRedButtons() {
        // Quit button
        constraints.gridx = 7;
        constraints.gridy = 0;
        JButton quiButton = new JButton("Quit");
        initRedButton(quiButton, e -> endGame());

        // Pass

        constraints.gridx = 7;
        constraints.gridy = 9;
        JButton passButton = new JButton("Pass");
        initRedButton(passButton, e -> pass());

        // Surrender button

        constraints.gridx = 0;
        constraints.gridy = 2;
        JButton surrenderButton = new JButton("Surrender");
        initRedButton(surrenderButton, e -> surrender());
    }

    protected void initRedButton(JButton button, ActionListener actionListener) {
        button.setBackground(LIGHT_RED);
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        add(button, constraints);
    }

    /**
     * Initializes the move buttons.
     */
    protected void initMoveButtons() {
        // LEFT
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.insets = new Insets(270, 0, 0, 0);
        JButton goLeftButton = new JButton("<");
        goLeftButton.addActionListener(e -> moveTo(Direction.LEFT));
        add(goLeftButton, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);

        // UP
        constraints.gridx = 4;
        constraints.gridy = 1;
        JButton goUpButton = new JButton("^");
        goUpButton.addActionListener(e -> moveTo(Direction.UP));
        add(goUpButton, constraints);

        // DOWN
        constraints.gridx = 4;
        constraints.gridy = 7;
        JButton goDownButton = new JButton("v");
        goDownButton.addActionListener(e -> moveTo(Direction.DOWN));
        add(goDownButton, constraints);

        // RIGHT
        constraints.gridx = 7;
        constraints.gridy = 4;
        constraints.insets = new Insets(270, 0, 0, 0);
        JButton goRightButton = new JButton(">");
        goRightButton.addActionListener(e -> moveTo(Direction.RIGHT));
        add(goRightButton, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);
    }

    protected abstract void initBoard();

    protected abstract void initTileToPlace();

    protected void initTurnButtons() {
        // Turn Left
        constraints.gridx = 3;
        constraints.gridy = 8;
        constraints.insets = new Insets(0, 200, 0, 0);
        JButton turnLeftButton = new JButton();
        turnLeftButton.setIcon(GraphicalImageSetter.initImageAsImageIcon("src/main/resources/turn_left.png"));
        turnLeftButton.setPreferredSize(new Dimension(50, 50));
        turnLeftButton.addActionListener(e -> turn(false));
        add(turnLeftButton, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);

        // Turn Right
        constraints.gridx = 5;
        constraints.gridy = 8;
        JButton turnRightButton = new JButton();
        turnRightButton.setIcon(GraphicalImageSetter.initImageAsImageIcon("src/main/resources/turn_right.png"));
        turnRightButton.setPreferredSize(new Dimension(50, 50));
        turnRightButton.addActionListener(e -> turn(true));
        add(turnRightButton, constraints);
    }

    /**
     * Ends the game and displays the end game screen.(the implementation can be
     * different but keep in mind that the game should be ended when this method is
     * called)
     * 
     */
    protected void endGame() {
        // TODO: replace this with an actual end game screen
        infoScreenLabel.setText("Game over");
    }

    /**
     * Passes the turn if the player has no possible placement. Otherwise, the
     * player is asked to place a tile.The player can pass if they decide to
     * press the pass button twice.
     */
    protected void pass() {
        if (wantedToPass || gameModel.findPossiblePlacements().isEmpty()) {
            wantedToPass = false;
            updateGameRound();
        } else {
            wantedToPass = true;
            JOptionPane.showMessageDialog(this, "You have at least on possible placement. Click again to pass", "",
                    JOptionPane.INFORMATION_MESSAGE);
            updateView();
        }
    }

    /**
     * The current player surrenders.
     */
    protected void surrender() {
        gameModel.surrender(gameModel.getCurrentPlayer());
        String message = "Player " + gameModel.getCurrentPlayer().getName() + " has surrendered.";
        JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);

        updateView();
        updateGameRound();
    }

    /**
     * Moves the minimap in the given direction.
     * 
     * @param direction the direction in which the minimap should be moved
     * 
     */
    protected void moveTo(Direction direction) {
        try {
            gameModel.move(direction);
            updateView();
        } catch (Exception e1) {
            infoScreenLabel.setText(e1.getMessage());
        }
    }

    /**
     * Rotates the tile to place in the given direction.
     * 
     * @param clockwise true if the tile should be rotated clockwise, false
     *                  otherwise
     */
    protected void turn(boolean clockwise) {
        try {
            gameModel.turn(clockwise, 1);
            updateView();
        } catch (Exception e) {
            //
        }
    }

    protected abstract void updateGameRound();

    protected abstract void updateView();

    /**
     * Private class that represents the board.
     */
    @SuppressWarnings("unchecked")
    protected abstract class Board extends JPanel {

        protected TilePanel<T>[] tiles = new TilePanel[25]; // Change to TileDominoGui

        protected Board() {
            setLayout(new GridLayout(5, 5));

            gameModel.iteriMinimap((x, y, tile) -> {
                initTile(x, y, tile);

                tiles[x + y * 5].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            gameModel.place(String.valueOf(x + 1), String.valueOf(y + 1), gameModel.getCurrentPlayer());
                            updateGameRound();
                        } catch (Exception e1) {
                            infoScreenLabel.setText(e1.getMessage());
                        }
                    }
                });
                add(tiles[x + y * 5]);
            });
        }

        /**
         * Updates the board.
         */
        public abstract void update();

        protected abstract void initTile(int x, int y, T tile);

    }

}
