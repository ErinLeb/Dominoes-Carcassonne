package carcassonne.view;

import shared.view.Settings;

/**
 * This settings class is the panel that contains the settings for the game
 * Carcassonne.
 */
public class SettingsCarcassonne extends Settings {

    SettingsModel settingsModel = new SettingsModel();

    /**
     * Creates a new settings panel.
     */
    public SettingsCarcassonne() {
        maxPlayers = 4;
        initNumberSettingsSelector();
    }

    @Override
    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    public static void main(String[] args) {
        SettingsCarcassonne settings = new SettingsCarcassonne();
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setPreferredSize(new java.awt.Dimension(900, 900));
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.add(settings);
        frame.pack();
        frame.setVisible(true);
    }

}
