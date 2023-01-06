package carcassonne.view;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import carcassonne.model.BotCarcassonne;
import carcassonne.model.GameCarcassonne;
import carcassonne.model.PlayerCarcassonne;
import shared.view.Settings;
import shared.view.StartMenu;

/**
 * This settings class is the panel that contains the settings for the game
 * Carcassonne.
 */
public class SettingsCarcassonne extends Settings {

    SettingsCarcassonneModel settingsModel = new SettingsCarcassonneModel();

    /**
     * Creates a new settings panel.
     */
    public SettingsCarcassonne(JFrame frame, StartMenu home) {
        this.frame = frame;
        this.home = home;

        maxPlayers = 4;
        initNumberSettingsSelector();
    }

    @Override
    protected void startGame() {
        GameCarcassonne gameModel = new GameCarcassonne(settingsModel.players);
        frame.setContentPane((new GameCarcassonnePanel(gameModel, frame, home)));
        frame.revalidate();
    }

    @Override
    public SettingsCarcassonneModel getSettingsModel() {
        return settingsModel;
    }

    private class SettingsCarcassonneModel extends SettingsModel<PlayerCarcassonne> {
        public SettingsCarcassonneModel() {
            super();
        }

        @Override
        public void generatePlayers() {
            players = new PlayerCarcassonne[totalNumberOfPlayers];

            Set<String> botNames = new HashSet<>();

            for (int i = 0; i < totalNumberOfPlayers; i++) {
                if (i < numberOfBots) {
                    // Takes care of bot naming an duplicates
                    players[i] = new BotCarcassonne(null);
                    while (botNames.contains(players[i].getName())) {
                        ((BotCarcassonne) players[i]).changeName();
                    }
                    botNames.add(players[i].getName());
                } else {
                    players[i] = new PlayerCarcassonne(null);
                }
            }
        }
    }
}