package domino.view.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import domino.model.BotDomino;
import domino.model.GameDomino;
import domino.model.PlayerDomino;
import domino.model.SideDomino;
import domino.model.TileDomino;
import shared.view.GamePanel;

/**
 * The GameDominoPanel class is the panel that contains the game of Domino.
 */
public class GameDominoPanel extends GamePanel<SideDomino, TileDomino> {

    private TileDominoPanel tileToPlace;

    private BoardDomino board;

    public GameDominoPanel(GameDomino model, JFrame frame) {
        this.gameModel = model;
        this.frame = frame;

        playerLabel = new JLabel("Player: " + model.getCurrentPlayer().getName());
        scoreLabel = new JLabel("Score: " + model.getCurrentPlayer().getScore());
        nbTiles = new JLabel(String.valueOf(model.getNbRemainingTiles()));
        infoScreenLabel = new JLabel("Click on a tile to place it on the board.");

        tileToPlace = new TileDominoPanel(model.getTileToPlace());

        init();
    }

    @Override
    protected void initScore() {
        // Score
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(scoreLabel, constraints);
    }

    @Override
    protected void initBoard() {
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 5;
        constraints.gridheight = 5;
        board = new BoardDomino();
        add(board, constraints);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
    }

    @Override
    protected void initTileToPlace() {
        constraints.gridx = 4;
        constraints.gridy = 8;
        add(tileToPlace, constraints);
    }

    /**
     * Updates the game round. If the game is over, the end game screen is
     * displayed. Otherwise, the game is updated.
     * 
     */
    @Override
    protected void updateGameRound() {

        if (!gameModel.isGameOn()) {
            endGame();
            return;
        }

        gameModel.updateGameRound();

        updateView();

        if (gameModel.getCurrentPlayer() instanceof BotDomino) {
            updateBot();
        }
    }

    private void updateBot() {
        BotDomino bot = (BotDomino) gameModel.getCurrentPlayer();

        String message;
        try {
            bot.play((GameDomino) gameModel);
            message = "Bot " + bot.getName() + " has placed a tile. " + bot.getName() + " has " + bot.getScore()
                    + " points.";

        } catch (Exception e) {
            message = ("Bot " + bot.getName() + " has passed.");
        }

        JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);

        revalidate();
        repaint();

        updateGameRound();
    }

    /**
     * Updates the view of the game.
     */
    @Override
    protected void updateView() {
        playerLabel.setText("Player: " + gameModel.getCurrentPlayer().getName());
        scoreLabel.setText("Score: " + gameModel.getCurrentPlayer().getScore());
        nbTiles.setText(String.valueOf(gameModel.getNbRemainingTiles()));

        tileToPlace.updateModel(gameModel.getTileToPlace());

        board.update();

        revalidate();
        repaint();
    }

    protected class BoardDomino extends Board {

        @Override
        protected void initTile(int x, int y, TileDomino tile) {
            tiles[x + y * 5] = new TileDominoPanel(tile);
        }

    }

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GameDomino model = new GameDomino(
                new PlayerDomino[] { new PlayerDomino("Erin"), new BotDomino(), new BotDomino(), new BotDomino() },
                28);
        model.updateGameRound();
        GameDominoPanel game = new GameDominoPanel(
                model, frame);

        frame.setBackground(java.awt.Color.WHITE);
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
    }
}
