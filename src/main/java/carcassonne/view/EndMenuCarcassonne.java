package carcassonne.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

import shared.view.EndMenu;
import shared.view.StartMenu;

/**
 * The EndMenuCarcassonne class is the panel that contains the end menu for
 * Carcassonne.
 */
public class EndMenuCarcassonne extends EndMenu {
    // Attributes
    private JLabel message;

    protected static final Color LIGHT_RED = new Color(0xde1738);

    public EndMenuCarcassonne(StartMenu homeMenu, JFrame frame) {
        super(homeMenu, frame);

        // Message
        message = new JLabel("The game is finished !");
        c.gridx = 1;
        c.gridy = 0;
        add(message, c);
    }
}
