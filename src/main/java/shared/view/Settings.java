package shared.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import carcassonne.model.BotCarcassonne;
import domino.model.BotDomino;
import domino.model.PlayerDomino;
import shared.model.Player;
import utils.Pair;
import utils.StringOperations;

public abstract class Settings extends JPanel {

    protected JTextField numberOfPlayersText = new JTextField(1);
    protected JTextField numberOfBotsText = new JTextField(1);

    protected Pair<JTextField, Integer>[] playerNameSelectors;

    public abstract SettingsModel getSettingsModel();

    protected GridBagConstraints c = new GridBagConstraints();

    protected int maxPlayers;

    /**
     * Initializes the panel that contains the settings for the number of players,
     * bots and dominoes.
     */
    protected void initNumberSettingsSelector() {

        setLayout(new GridBagLayout());
        initFilters();

        initTotalNumberOfPlayers();
        initNumberOfBots();

        initNextButton();

    }

    /**
     * Initializes the filters for the text fields.
     */
    protected void initFilters() {

        initTotalNumberOfPlayersFilter();

        initNumberOfBotsFilter();

    }

    /**
     * Initializes the filter for the number of dominoes text field. Only allows
     * digits between 2 and 6.
     */
    protected void initTotalNumberOfPlayersFilter() {
        // Create document filter to only allow digits between 2 and maxPlayers
        DocumentFilter filterNbOfPlayers = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                // Validate input and only allow digits between 2 and 6
                if (string.matches("[2-" + maxPlayers + "]") && numberOfPlayersText.getText().length() < 1) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    JOptionPane.showMessageDialog(Settings.this, "Please enter a number between 2 and " + maxPlayers);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                // Validate input and only allow digits between 2 and 6
                if (text.matches("[2-" + maxPlayers + "]") && numberOfPlayersText.getText().length() < 1) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    JOptionPane.showMessageDialog(Settings.this, "Please enter a number between 2 and " + maxPlayers);
                }
            }
        };

        ((AbstractDocument) numberOfPlayersText.getDocument()).setDocumentFilter(filterNbOfPlayers);
    }

    /**
     * Initializes the filter for the number of bots text field. The filter only
     * allows digits between 0 and the total number of players.
     */
    private void initNumberOfBotsFilter() {
        // Create document filter to only allow digits between 0 and the total number of
        // players
        DocumentFilter filterNbOfBots = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                // Validate input and only allow digits between 0 and the total number of
                // players
                if (string.matches("[0-6]") && numberOfBotsText.getText().length() < 1 && isValidNumber(string)) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    JOptionPane.showMessageDialog(Settings.this,
                            "Please enter a number between 0 and the total number of players");
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                // Validate input and only allow digits between 0 and the total number of
                // players
                if (text.matches("[0-6]") && numberOfBotsText.getText().length() < 1 && isValidNumber(text)) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    JOptionPane.showMessageDialog(Settings.this,
                            "Please enter a number between 0 and the total number of players");
                }
            }

            private boolean isValidNumber(String text) {
                int number = Integer.parseInt(text);

                if (numberOfPlayersText.getText().isEmpty())
                    return number <= 6;

                return number <= Integer.parseInt(numberOfPlayersText.getText());
            }
        };

        ((AbstractDocument) numberOfBotsText.getDocument()).setDocumentFilter(filterNbOfBots);
    }

    protected void initTotalNumberOfPlayers() {
        numberOfPlayersText.setText("2");

        c.gridx = 0;
        c.gridy = 0;

        JLabel numberOfPlayersLabel = new JLabel("Total number of players: ");
        setLabelFontSize(numberOfPlayersLabel);
        numberOfPlayersLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(numberOfPlayersLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        setTextFieldFontSize(numberOfPlayersText);
        add(numberOfPlayersText, c);
    }

    protected void initNumberOfBots() {
        numberOfBotsText.setText("0");

        c.gridx = 0;
        c.gridy = 1;
        JLabel numberOfBotsLabel = new JLabel("Number of bots: ");
        setLabelFontSize(numberOfBotsLabel);
        numberOfBotsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(numberOfBotsLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        setTextFieldFontSize(numberOfBotsText);
        add(numberOfBotsText, c);
    }

    protected void initNextButton() {
        c.gridx = 0;
        c.gridy = 3;
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> nextSettingsPanel());
        setButtonFontSize(nextButton);
        add(nextButton, c);
    }

    protected void setComponentFontSize(JComponent component, float size) {
        component.setFont(component.getFont().deriveFont(size));
    }

    protected void setLabelFontSize(JLabel label) {
        setComponentFontSize(label, 26);
    }

    protected void setTextFieldFontSize(JTextField textZone) {
        setComponentFontSize(textZone, 22);
    }

    protected void setButtonFontSize(JButton button) {
        setComponentFontSize(button, 22);
    }

    /**
     * Updates the settings and initializes the panel that contains the settings
     * for the players.
     */
    protected void nextSettingsPanel() {

        getSettingsModel().updateSettings();
        if (getSettingsModel().checkSettings()) {
            getSettingsModel().generatePlayers();
            getSettingsModel().shufflePlayers();
            initPlayersSettingsSelector();
        } else {
            notifyMissingSettings();
        }

    }

    protected void notifyMissingSettings() {
        if (!getSettingsModel().isCorrectNumberOfPlayers() || !numberOfPlayersText.getText().matches("(\\d)+")) {
            JOptionPane.showMessageDialog(Settings.this,
                    "Please enter a number between 2 and 6 for the number of players");
        } else if (!getSettingsModel().isCorrectNumberOfBots() || !numberOfBotsText.getText().matches("(\\d)+")) {
            JOptionPane.showMessageDialog(Settings.this,
                    "Please enter a number between 0 and the total number of players for the number of bots");
        }
    }

    /**
     * Initializes the panel that contains the settings for the players.
     */
    @SuppressWarnings("unchecked")
    private void initPlayersSettingsSelector() {
        setLayout(new GridBagLayout());

        removeAll();
        revalidate();
        repaint();

        playerNameSelectors = new Pair[getSettingsModel().totalNumberOfPlayers - getSettingsModel().numberOfBots];

        int counter = 0;
        for (int i = 0; i < getSettingsModel().players.length; i++) {
            if (getSettingsModel().players[i] instanceof BotDomino
                    || getSettingsModel().players[i] instanceof BotCarcassonne) {
                continue;
            }

            initPlayerTextZone(counter, i);

            counter++;
        }

        initStartGameButton(counter);

    }

    private void initPlayerTextZone(int counter, int i) {

        c.gridx = 0;
        c.gridy = counter;
        JLabel playerNameLabel = new JLabel("Player " + (i + 1) + " name: ");
        setLabelFontSize(playerNameLabel);
        add(playerNameLabel, c);

        c.gridx = 1;
        c.gridy = counter;
        JTextField playerName = new JTextField(20);
        setTextFieldFontSize(playerName);
        add(playerName, c);

        playerNameSelectors[counter] = new Pair<>(playerName, i + 1);
    }

    private void initStartGameButton(int counter) {
        c.gridx = 0;
        c.gridy = counter + 1;
        JButton startGameButton = new JButton("Start game");
        startGameButton.addActionListener(e -> startGame());
        setButtonFontSize(startGameButton);
        add(startGameButton, c);
    }

    /**
     * Starts the game.
     */
    protected void startGame() {

        Set<String> names = new HashSet<>();

        for (Pair<JTextField, Integer> pair : playerNameSelectors) {
            String name = pair.first.getText();
            if (name.isEmpty() || StringOperations.isBlank(name)) {
                JOptionPane.showMessageDialog(this, "Please enter a name for player " + pair.second);
                return;
            }

            if (names.contains(name)) {
                JOptionPane.showMessageDialog(this, "Please enter a unique name for player " + pair.second);
                return;
            }

            names.add(name);
            getSettingsModel().players[pair.second - 1].setName(name);
        }

        System.out.println("Start game");// TODO remove this

        // TODO: start game
    }

    protected class SettingsModel {

        public int totalNumberOfPlayers;
        public int numberOfBots;

        public Player[] players;

        /**
         * Creates a new settings model.
         */
        public SettingsModel() {
            totalNumberOfPlayers = -1;
            numberOfBots = -1;
        }

        /**
         * Updates the settings.
         */
        public void updateSettings() {
            if (numberOfPlayersText.getText().matches("(\\d)+")) {
                totalNumberOfPlayers = Integer.parseInt(numberOfPlayersText.getText());
            }
            if (numberOfBotsText.getText().matches("(\\d)+")) {
                numberOfBots = Integer.parseInt(numberOfBotsText.getText());
            }

        }

        /**
         * Checks if the settings are valid.
         * 
         * @return {@code true} if the settings are valid, {@code false} otherwise
         */
        public boolean checkSettings() {
            return isCorrectNumberOfPlayers() && isCorrectNumberOfBots();
        }

        public boolean isCorrectNumberOfPlayers() {
            return totalNumberOfPlayers >= 2 && totalNumberOfPlayers <= 6;
        }

        public boolean isCorrectNumberOfBots() {
            return numberOfBots >= 0 && numberOfBots <= totalNumberOfPlayers;
        }

        /**
         * Generates the players.
         */
        public void generatePlayers() {
            players = new Player[totalNumberOfPlayers];

            Set<String> botNames = new HashSet<>();

            for (int i = 0; i < totalNumberOfPlayers; i++) {
                if (i < numberOfBots) {
                    // Takes care of bot naming an duplicates
                    players[i] = new BotDomino();
                    while (botNames.contains(players[i].getName())) {
                        ((BotDomino) players[i]).changeName();
                    }
                    botNames.add(players[i].getName());
                } else {
                    players[i] = new PlayerDomino();
                }
            }
        }

        /**
         * Shuffles the players.
         */
        public void shufflePlayers() {
            Random rand = new Random();

            for (int i = 0; i < totalNumberOfPlayers; i++) {
                int j = rand.nextInt(totalNumberOfPlayers);

                Player temp = players[i];
                players[i] = players[j];
                players[j] = temp;
            }
        }

    }

}
