package domino.view.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import shared.model.Player;
import shared.view.EndMenu;
import shared.view.StartMenu;

/**
 * The EndMenuDomino class is the panel that contains the end menu for the
 * dominoes.
 */
public class EndMenuDomino extends EndMenu {
    // Attributes
    private int nbPlayers;
    private List<Player> winners;
    private JLabel winnersLabel;

    private Player[] ranking;
    private JPanel rankingPanel;

    // Constructor
    /**
     * Creates a JPanel representing the menu displayed at the end of a game, with
     * the {@code winners} and the {@code ranking}. The {@code homeMenu} is
     * accessible with a button. The {@code frame} in which the EndMenu is can be
     * closed with another button.
     * 
     * @param nbPlayers the number of players of the game that ended
     * @param winners   the list of the winners
     * @param ranking   the array of the ranking of the players
     * @param homeMenu  the StartMenu we want to be accessible
     * @param frame     the frame in which this panel is displayed
     */
    public EndMenuDomino(int nbPlayers, List<Player> winners, Player[] ranking, StartMenu homeMenu, JFrame frame) {
        super(homeMenu, frame);
        // Attributes
        this.nbPlayers = nbPlayers;
        this.winners = winners;
        this.ranking = ranking;

        // Label winners
        winnersLabel = new JLabel(getWinners());
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 40, 0);
        add(winnersLabel, c);
        c.insets = new Insets(0, 0, 0, 0);

        // Ranking
        rankingPanel = getRanking();
        c.gridy = 1;
        add(rankingPanel, c);
    }

    private String getWinners() {
        if (winners == null || winners.isEmpty() || winners.get(0) == null) {
            return "There is no winner !";
        }

        String message = "";
        for (int i = 0; i < winners.size(); i++) {
            if (i == winners.size() - 1) {
                message += winners.get(i).getName() + " ";
            } else if (i == winners.size() - 2) {
                message += winners.get(i).getName() + " and ";
            } else {
                message += winners.get(i).getName() + ", ";
            }
        }

        if (winners.size() > 1) {
            message = "Equality : " + message;
        }
        return message + "won !";
    }

    private JPanel getRanking() {
        JPanel panel = new JPanel(new GridLayout(nbPlayers, 2));

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        panel.setBackground(new Color(0xAFEEEE));

        for (int i = 0; i < nbPlayers; i++) {
            JLabel score = new JLabel("" + ranking[i].getScore());
            score.setBorder(border);
            panel.add(score);

            JLabel name = new JLabel();
            name.setBorder(border);
            if (!ranking[i].isInGame()) {
                name.setText(ranking[i].getName() + " (surrendered)");
            } else {
                name.setText(ranking[i].getName());
            }
            panel.add(name);
        }

        return panel;
    }
}
