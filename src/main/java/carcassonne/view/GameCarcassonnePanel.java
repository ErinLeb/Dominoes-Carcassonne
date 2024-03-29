package carcassonne.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import carcassonne.model.BotCarcassonne;
import carcassonne.model.GameCarcassonne;
import carcassonne.model.PlayerCarcassonne;
import carcassonne.model.SideCarcassonne;
import carcassonne.model.TileCarcassonne;
import shared.view.GamePanel;
import shared.view.StartMenu;

/**
 * This class is the panel that contains the game board and the game information
 * of the game Carcassonne.
 */
public class GameCarcassonnePanel extends GamePanel<SideCarcassonne, TileCarcassonne> {

    private TileCarcassonnePanel tileToPlace;

    private JLabel numberOfPawns;

    private BoardCarcassonne board;

    public GameCarcassonnePanel(GameCarcassonne gameModel, JFrame frame, StartMenu homeMenu) {
        this.gameModel = gameModel;
        this.frame = frame;
        this.homeMenu = homeMenu;

        gameModel.updateGameRound();

        updateView();

        if (gameModel.getCurrentPlayer() instanceof BotCarcassonne) {
            EventQueue.invokeLater(this::updateBot);
        }
    }

    @Override
    protected void init() {
        playerLabel = new JLabel("Player: " + gameModel.getCurrentPlayer().getName());

        numberOfPawns = new JLabel(
                "Remaining pawns: " + ((PlayerCarcassonne) gameModel.getCurrentPlayer()).getRemainingPawns());

        nbTiles = new JLabel(String.valueOf(gameModel.getNbRemainingTiles()));

        String infoMessage = "<html> Click on a tile to place it on the board.<br>";

        infoScreenLabel = new JLabel(infoMessage);

        tileToPlace = new TileCarcassonnePanel(gameModel.getTileToPlace());
        tileToPlace.setCanPawnBePlaced(((PlayerCarcassonne) gameModel.getCurrentPlayer()).getRemainingPawns() > 0);

        super.init();

        initNumberOfPawns();

        board.updatePawns();

    }

    @Override
    protected void initScore() {
        // Do nothing here.
    }

    protected void initNumberOfPawns() {
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(numberOfPawns, constraints);
    }

    @Override
    protected void initBoard() {
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 5;
        constraints.gridheight = 5;
        board = new BoardCarcassonne();
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

    @Override
    protected void updateGameRound() {
        if (!gameModel.isGameOn()) {
            endGame();
            return;
        }

        gameModel.updateGameRound();

        updateView();

        if (gameModel.getCurrentPlayer() instanceof BotCarcassonne) {
            updateBot();
        }
    }

    private void updateBot() {
        BotCarcassonne bot = (BotCarcassonne) gameModel.getCurrentPlayer();

        String message;
        try {
            bot.play((GameCarcassonne) gameModel);
            message = "Bot " + bot.getName() + " has placed a tile.";

        } catch (Exception e) {
            message = ("Bot " + bot.getName() + " has passed.");
        }

        JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);

        revalidate();
        repaint();

        updateGameRound();
    }

    @Override
    protected void updateView() {
        this.removeAll();
        revalidate();
        repaint();

        init();
        revalidate();
        repaint();
    }

    @Override
    protected void pass() {
        if (gameModel.findPossiblePlacements().isEmpty()) {
            updateGameRound();
        } else {
            JOptionPane.showMessageDialog(this, "You have at least on possible placement", "",
                    JOptionPane.ERROR_MESSAGE);
            updateView();
        }
    }

    @Override
    protected void endGame() {
        EndMenuCarcassonne endMenu = new EndMenuCarcassonne(homeMenu, frame);
        frame.setContentPane(endMenu);
        frame.revalidate();
    }

    protected class BoardCarcassonne extends Board {
        @Override
        protected void initTile(int x, int y, TileCarcassonne tile) {
            // Null color because the tiles were not placed by someone.
            tiles[x + y * 5] = new TileCarcassonnePanel(tile);
        }

        @Override
        public void update() {
            gameModel.iteriMinimap((x, y, tile) -> tiles[x + y * 5] = new TileCarcassonnePanel(tile));
        }

        public void updatePawns() {
            gameModel.iteriMinimap((x, y, tile) -> ((TileCarcassonnePanel) tiles[x + y * 5]).drawPawn());
        }
    }
}
