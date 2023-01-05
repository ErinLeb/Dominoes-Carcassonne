package shared.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import carcassonne.model.TileCarcassonne;
import carcassonne.view.SettingsCarcassonne;
import carcassonne.view.TileCarcassonnePanel;
import domino.model.TileDomino;
import domino.view.gui.SettingsDomino;
import domino.view.gui.TileDominoPanel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * The StartMenu class is the panel that contains the start menu.
 */
public class StartMenu extends JPanel {
    private JLabel welcome;
    private JLabel dominoTitle;
    private JLabel carcassonneTitle;

    private Rules rules;

    private JButton rulesButton;
    private TileDominoPanel domino;
    private TileCarcassonnePanel carcassonne;

    public StartMenu(JFrame frame) {
        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Background
        setBackground(new Color(0xAFEEEE));

        // Border
        setBorder(new TitledBorder(new EtchedBorder(), "Home"));
        ((TitledBorder) getBorder()).setTitleJustification(TitledBorder.CENTER);

        // Message
        welcome = new JLabel("Welcome ! Which game do you want to play ?");
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 40, 0);
        add(welcome, c);

        // Rules
        rules = new Rules();
        rules.setHome(e -> {
            frame.setContentPane(this);
            frame.revalidate();
        });

        // Buttons
        // Rules Button
        rulesButton = new JButton("Rules");
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(rules);
                frame.revalidate();
            }
        });
        c.gridx = 1;
        c.gridy = 1;
        add(rulesButton, c);

        // Domino Button
        domino = new TileDominoPanel(new TileDomino());
        domino.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setContentPane(new SettingsDomino(frame, StartMenu.this));
                frame.revalidate();
            }
        });
        c.gridx = 0;
        c.gridy = 2;
        add(domino, c);

        // Carcassonne Buttton
        carcassonne = new TileCarcassonnePanel(new TileCarcassonne(15));
        carcassonne.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setContentPane(new SettingsCarcassonne(frame, StartMenu.this));
                frame.revalidate();
            }
        });
        c.gridx = 2;
        c.gridy = 2;
        add(carcassonne, c);

        // Titles of the games
        dominoTitle = new JLabel("Domino");
        c.gridx = 0;
        c.gridy = 3;
        add(dominoTitle, c);

        carcassonneTitle = new JLabel("Carcassonne");
        c.gridx = 2;
        c.gridy = 3;
        add(carcassonneTitle, c);
    }

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 950);

        StartMenu menu = new StartMenu(frame);
        frame.add(menu);
        frame.setVisible(true);
    }

}
