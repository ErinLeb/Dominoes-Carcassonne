package shared.view;

import shared.model.Player;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import domino.model.BotDomino;

/**
 * The EndMenu class is the panel that contains the end menu.
 */
public class EndMenu extends JPanel {
    // Attributes
    private int nbPlayers;
    private List<Player> winners;
    private Player[] ranking;

    private JButton home;
    private JButton quit;
    private JPanel rankingPanel;
    private JLabel winnersLabel;

    protected static final Color LIGHT_RED = new Color(0xde1738);

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
    public EndMenu(int nbPlayers, List<Player> winners, Player[] ranking, StartMenu homeMenu, JFrame frame) {
        // Attributes
        this.nbPlayers = nbPlayers;
        this.winners = winners;
        this.ranking = ranking;

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Background
        setBackground(new Color(0xAFEEEE));

        // Border
        setBorder(new TitledBorder(new EtchedBorder(), "End of the game"));
        ((TitledBorder) getBorder()).setTitleJustification(TitledBorder.CENTER);

        // Button Home
        home = new JButton("Home");
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // homeMenu.init();
            }
        });
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(40, 0, 0, 20);
        add(home, c);

        // Button Quit
        quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        quit.setBackground(LIGHT_RED);
        quit.setForeground(Color.WHITE);
        c.gridx = 2;
        c.gridy = 2;
        c.insets = new Insets(40, 20, 0, 0);
        add(quit, c);

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

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        Player p1 = new Player("Yago");
        Player p2 = new Player("Erin");
        Player p3 = new BotDomino();

        p1.incrementScore(23);
        p2.incrementScore(24);
        p2.setInGame(false);
        p3.incrementScore(23);

        List<Player> winners = new ArrayList<>();
        winners.add(p1);
        winners.add(p3);

        Player[] ranking = { p1, p3, p2 };

        EndMenu menu = new EndMenu(3, winners, ranking, null, frame);
        frame.add(menu);
        frame.setVisible(true);
    }

}
