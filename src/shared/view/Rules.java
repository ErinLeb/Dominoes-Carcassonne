package shared.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

/**
 * The Rules class is the panel from which we can access the different rules of
 * the games.
 */
public class Rules extends JPanel {
    // Attributes
    private RulesGame domino;
    private RulesGame carcassonne;
    private JPanel choice;
    private JButton rulesDomino;
    private JButton rulesCarcassonne;
    private JButton home;
    private GridBagConstraints c = new GridBagConstraints();

    // Constructor
    public Rules() {
        // Layout
        setLayout(new GridBagLayout());
        setBackground(new Color(0xAFEEEE));

        // Title
        setBorder(new TitledBorder(new EtchedBorder(), "Rules"));
        ((TitledBorder) getBorder()).setTitleJustification(TitledBorder.CENTER);

        // Button Home
        home = new JButton("Home");

        c.gridx = 0;
        c.gridy = 0;
        add(home, c);

        // Panel Domino
        String dominoText = "<html>";
        dominoText += "In this version of Dominoes, the dominoes are squares and have three figures on each of their sides.<br/>";
        dominoText += "<br/> <b>How to win ?</b> <br/>";
        dominoText += "The winner is the player who has the highest score when there is no tile to place anymore. Another way to win is to be the only player to last in the game if the others surrendered.<br/>";
        dominoText += "<br/><u>How to score points ?</u><br/>";
        dominoText += "You score when you place successfully a tile on the board. The amount of points you gain is the sum of the numbers in contact with those of the neighboring tiles.<br/>";
        dominoText += "<br/><u>The rules are easy :</u> <br/>";
        dominoText += "There are between 2 and 6 players. You can also play against AIs by choosing so at the beginning.<br/>";
        dominoText += "At the beginning, you need to choose the number of tiles you want (one placed on the board to start and the others in the deck).<br/>";
        dominoText += "You can stop the game once it has begun using the command 'quit'.<br/>";
        dominoText += "A player can decide to quit the game. If after this decision there is only one player left in the game, this one wins by default, else, the game continues with the players still in the game.</html>";

        domino = new RulesGame(dominoText, "Carcassonne");

        domino.setExit();

        // Panel Carcassonne
        String carcassonneText = "<html>";
        carcassonneText += "Carcassonne follows the same concept than Dominoes but instead, the tiles represent landscapes. <br>";
        carcassonneText += "<br/> <u>How to win ?</u> <br/>";
        carcassonneText += "The winner is the player who has the highest score when there is no tile to place anymore. Another way to win is to be the only player to last in the game if the others surrendered.<br/>";
        carcassonneText += "<br/><u>How to score points ?</u><br/>";
        carcassonneText += "You score when you complete a landscape during the game. At the end of the game, you score is incremented according to where you pawns are placed on the board.</br>";
        carcassonneText += "<br><u> How to place tiles ?</u></br>";
        carcassonneText += "The tile you draw must be placed in contact with at least one tile already on the board. The type of every side that touches another tile must correspond. If you can not place the tile given the conditions, you draw another card to place instead.<br>";
        carcassonneText += "<br><u>How to place a pawn ?</u></br>";
        carcassonneText += "After you place the tile, you can place one of the pawns you still haven't place on the tile you placed. You can place it on one of the zones represented in the tile as shown in the exemples.</br>";

        carcassonne = new RulesGame(carcassonneText, "Domino");

        carcassonne.setExit();

        // Set switch between the panels
        domino.setSwitch("Carcassonne");
        carcassonne.setSwitch("Domino");

        // Buttons
        choice = new JPanel(new GridBagLayout());
        choice.setBackground(new Color(0xAFEEEE));

        // Button Domino
        rulesDomino = new JButton("Domino");

        rulesDomino.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rulesDomino();
            }
        });

        c.gridy = 1;
        c.insets = new Insets(50, 0, 0, 0);
        choice.add(rulesDomino, c);

        // Button Carcassonne
        rulesCarcassonne = new JButton("Carcassonne");

        rulesCarcassonne.addMouseListener(new MouseInputAdapter() {
            public void mouseClicked(MouseEvent e) {
                rulesCarcassonne();
            }
        });

        c.gridy = 2;
        c.insets = new Insets(50, 0, 0, 0);
        choice.add(rulesCarcassonne, c);

        add(choice, c);
    }

    // Methods

    /**
     * Sets the action executed by the button {@code home} when clicked.
     * 
     * @param c the action to be executed
     */
    public void setHome(Consumer<MouseEvent> c) {
        home.addMouseListener(new MouseInputAdapter() {
            public void mouseClicked(MouseEvent e) {
                c.accept(e);
            }
        });
    }

    /**
     * Switches to the main panel of rules.
     */
    public void rules() {
        removeAll();
        revalidate();
        repaint();

        // Layout
        setLayout(new GridBagLayout());
        setBackground(new Color(0xAFEEEE));

        // Title
        setBorder(new TitledBorder(new EtchedBorder(), "Rules"));
        ((TitledBorder) getBorder()).setTitleJustification(TitledBorder.CENTER);

        // Buttons
        c.gridx = 0;
        c.gridy = 0;
        add(home, c);

        c.gridx = 0;
        c.gridy = 1;
        add(choice, c);
    }

    /**
     * Switches to the panel of the rules for Domino.
     */
    public void rulesDomino() {
        removeAll();
        revalidate();
        repaint();

        // Layout
        setLayout(new BorderLayout());

        // Title
        setBorder(new TitledBorder(new EtchedBorder(), "Rules : Domino"));
        ((TitledBorder) getBorder()).setTitleJustification(TitledBorder.CENTER);

        // Panel Domino
        add(domino);
    }

    /**
     * Switches to the panel of the rules for Carcassonne.
     */
    public void rulesCarcassonne() {
        removeAll();
        revalidate();
        repaint();

        // Layout
        setLayout(new BorderLayout());

        // Title
        setBorder(new TitledBorder(new EtchedBorder(), "Rules : Carcassonne"));
        ((TitledBorder) getBorder()).setTitleJustification(TitledBorder.CENTER);

        // Panel Carcassonne
        add(carcassonne, BorderLayout.CENTER);
    }

    // Intern Class
    /**
     * Panel representing the rules of a specific game and can access the rules of
     * another one.
     */
    public class RulesGame extends JPanel {
        private JLabel rules;
        private JButton exit;
        private JButton change;
        private JPanel north;
        private JPanel center;

        /**
         * 
         * @param text   the rules of the game
         * @param button the name of the game which we can access the rules
         */
        public RulesGame(String text, String button) {
            setLayout(new BorderLayout());

            // Background
            setBackground(new Color(0xAFEEEE));
            setOpaque(true);

            // North panel
            north = new JPanel(new BorderLayout());
            north.setBackground(new Color(0xAFEEEE));

            // Button to exit
            exit = new JButton("Exit");
            north.add(exit, BorderLayout.WEST);
            exit.setHorizontalAlignment(SwingConstants.LEFT);

            // Switch Button
            change = new JButton(button);
            north.add(change, BorderLayout.EAST);
            change.setHorizontalAlignment(SwingConstants.RIGHT);

            add(north, BorderLayout.NORTH);

            // Center Panel
            center = new JPanel(new BorderLayout());
            center.setBackground(new Color(0xAFEEEE));

            // Rules
            rules = new JLabel(text);

            center.add(rules, BorderLayout.CENTER);

            add(center, BorderLayout.CENTER);
        }

        public void setExit() {
            exit.addMouseListener(new MouseInputAdapter() {
                public void mouseClicked(MouseEvent e) {
                    rules();
                }
            });
        }

        public void setSwitch(String otherGame) {
            change.addMouseListener(new MouseInputAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (otherGame.equals("Domino")) {
                        rulesDomino();
                    } else if (otherGame.equals("Carcassonne")) {
                        rulesCarcassonne();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.add(new Rules());
        frame.setVisible(true);
    }
}