package domino.view.gui;

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

import domino.model.BotDomino;
import domino.model.PlayerDomino;
import utilities.Pair;

/**
 * This settings class is the panel that contains the settings for the game
 * Domino
 */
public class SettingsDomino extends JPanel {

    private JTextField numberOfPlayersText = new JTextField(1);
    private JTextField numberOfBotsText = new JTextField(1);
    private JTextField numberOfDominoesText = new JTextField(3);

    private Pair<JTextField, Integer>[] playerNameSelectors;

    private SettingsModel settingsModel = new SettingsModel();

    /**
     * Creates a new settings panel.
     */
    public SettingsDomino() {
        initNumberSettingsSelector();
    }

    /**
     * Initializes the panel that contains the settings for the number of players,
     * bots and dominoes.
     */
    private void initNumberSettingsSelector() {

        setLayout(new GridBagLayout());
        initFilters();

        GridBagConstraints c = new GridBagConstraints();

        initTotalNumberOfPlayers(c);
        initNumberOfBots(c);
        initNumberOfDominoes(c);

        initNextButton(c);

    }

    private void initNextButton(GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 3;
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> nextSettingsPanel());
        setButtonFontSize(nextButton);
        add(nextButton, c);
    }

    private void initNumberOfDominoes(GridBagConstraints c) {
        numberOfDominoesText.setText("28");

        c.gridx = 0;
        c.gridy = 2;
        JLabel numberOfDominoesLabel = new JLabel("Number of dominoes: ");
        setLabelFontSize(numberOfDominoesLabel);
        numberOfDominoesLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(numberOfDominoesLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        setTextFieldFontSize(numberOfDominoesText);
        add(numberOfDominoesText, c);
    }

    private void initNumberOfBots(GridBagConstraints c) {
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

    private void initTotalNumberOfPlayers(GridBagConstraints c) {
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

    private void setComponentFontSize(JComponent component, float size) {
        component.setFont(component.getFont().deriveFont(size));
    }

    private void setLabelFontSize(JLabel label) {
        setComponentFontSize(label, 26);
    }

    private void setTextFieldFontSize(JTextField textZone) {
        setComponentFontSize(textZone, 22);
    }

    private void setButtonFontSize(JButton button) {
        setComponentFontSize(button, 22);
    }

    /**
     * Initializes the filters for the text fields.
     */
    private void initFilters() {

        initTotalNumberOfPlayersFilter();

        initNumberOfBotsFilter();

        initNumberOfDominoesFilter();

    }

    /**
     * Initializes the filter for the number of dominoes text field. Only allows
     * digits between 2 and 6.
     */
    private void initTotalNumberOfPlayersFilter() {
        // Create document filter to only allow digits between 2 and 6
        DocumentFilter filterNbOfPlayers = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                // Validate input and only allow digits between 2 and 6
                if (string.matches("[2-6]") && numberOfPlayersText.getText().length() < 1) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    JOptionPane.showMessageDialog(SettingsDomino.this, "Please enter a number between 2 and 6");
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                // Validate input and only allow digits between 2 and 6
                if (text.matches("[2-6]") && numberOfPlayersText.getText().length() < 1) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    JOptionPane.showMessageDialog(SettingsDomino.this, "Please enter a number between 2 and 6");
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
                    JOptionPane.showMessageDialog(SettingsDomino.this,
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
                    JOptionPane.showMessageDialog(SettingsDomino.this,
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

    /**
     * Initializes the filter for the total number of players. The filter only
     * allows numbers.
     */
    private void initNumberOfDominoesFilter() {
        // Only allow numbers
        DocumentFilter filterNbOfDominoes = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string.matches("(\\d)+")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    JOptionPane.showMessageDialog(SettingsDomino.this, "Please enter a number");
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text.matches("(\\d)+")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    JOptionPane.showMessageDialog(SettingsDomino.this, "Please enter a number");
                }
            }
        };

        ((AbstractDocument) numberOfDominoesText.getDocument()).setDocumentFilter(filterNbOfDominoes);
    }

    /**
     * Updates the settings and initializes the panel that contains the settings
     * for the players.
     */
    private void nextSettingsPanel() {

        settingsModel.updateSettings();
        if (settingsModel.checkSettings()) {
            settingsModel.generatePlayers();
            settingsModel.shufflePlayers();
            initPlayersSettingsSelector();
        } else {
            notifyMissingSettings();
        }

    }

    private void notifyMissingSettings() {
        if (!settingsModel.isCorrectNumberOfPlayers() || !numberOfPlayersText.getText().matches("(\\d)+")) {
            JOptionPane.showMessageDialog(SettingsDomino.this,
                    "Please enter a number between 2 and 6 for the number of players");
        } else if (!settingsModel.isCorrectNumberOfBots() || !numberOfBotsText.getText().matches("(\\d)+")) {
            JOptionPane.showMessageDialog(SettingsDomino.this,
                    "Please enter a number between 0 and the total number of players for the number of bots");
        } else if (!settingsModel.isCorrectNumberOfDominoes() || !numberOfDominoesText.getText().matches("(\\d)+")) {
            JOptionPane.showMessageDialog(SettingsDomino.this,
                    "Please enter a number between 15 and 100 for the number of dominoes");
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

        GridBagConstraints c = new GridBagConstraints();
        playerNameSelectors = new Pair[settingsModel.players.length];

        int counter = 0;
        for (int i = 0; i < settingsModel.players.length; i++) {
            if (settingsModel.players[i] instanceof BotDomino)
                continue;

            initPlayerTextZone(c, counter, i);

            counter++;
        }

        initStartGameButton(c, counter);

    }

    private void initPlayerTextZone(GridBagConstraints c, int counter, int i) {
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

    private void initStartGameButton(GridBagConstraints c, int counter) {
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
    private void startGame() {

        Set<String> names = new HashSet<>();

        for (Pair<JTextField, Integer> pair : playerNameSelectors) {
            String name = pair.first.getText();
            if (name.isEmpty() || name.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter a name for player " + pair.second);
                return;
            }

            if (names.contains(name)) {
                JOptionPane.showMessageDialog(this, "Please enter a unique name for player " + pair.second);
                return;
            }

            names.add(name);
            settingsModel.players[pair.second - 1].setName(name);
        }

        // TODO: start game
    }

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    private class SettingsModel {

        private int totalNumberOfPlayers;
        private int numberOfBots;

        private PlayerDomino[] players;

        private int numberOfDominoes;

        /**
         * Creates a new settings model.
         */
        private SettingsModel() {
            totalNumberOfPlayers = -1;
            numberOfBots = -1;
            numberOfDominoes = -1;
        }

        /**
         * Updates the settings.
         */
        private void updateSettings() {
            if (numberOfPlayersText.getText().matches("(\\d)+")) {
                totalNumberOfPlayers = Integer.parseInt(numberOfPlayersText.getText());
            }
            if (numberOfBotsText.getText().matches("(\\d)+")) {
                numberOfBots = Integer.parseInt(numberOfBotsText.getText());
            }
            if (numberOfDominoesText.getText().matches("(\\d)+")) {
                numberOfDominoes = Integer.parseInt(numberOfDominoesText.getText());
            }
        }

        /**
         * Checks if the settings are valid.
         * 
         * @return {@code true} if the settings are valid, {@code false} otherwise
         */
        private boolean checkSettings() {
            return isCorrectNumberOfDominoes() && isCorrectNumberOfPlayers() && isCorrectNumberOfBots();
        }

        private boolean isCorrectNumberOfDominoes() {
            return numberOfDominoes >= 15 && numberOfDominoes <= 100;
        }

        private boolean isCorrectNumberOfPlayers() {
            return totalNumberOfPlayers >= 2 && totalNumberOfPlayers <= 6;
        }

        private boolean isCorrectNumberOfBots() {
            return numberOfBots >= 0 && numberOfBots <= totalNumberOfPlayers;
        }

        /**
         * Generates the players.
         */
        private void generatePlayers() {
            players = new PlayerDomino[totalNumberOfPlayers];

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
        private void shufflePlayers() {
            Random rand = new Random();

            for (int i = 0; i < totalNumberOfPlayers; i++) {
                int j = rand.nextInt(totalNumberOfPlayers);

                PlayerDomino temp = players[i];
                players[i] = players[j];
                players[j] = temp;
            }
        }

        public int getTotalNumberOfPlayers() {
            return totalNumberOfPlayers;
        }

        public int getNumberOfBots() {
            return numberOfBots;
        }

        public PlayerDomino[] getPlayers() {
            return players;
        }

        public int getNumberOfDominoes() {
            return numberOfDominoes;
        }

    }

    public static void main(String[] args) {
        SettingsDomino settings = new SettingsDomino();
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setPreferredSize(new java.awt.Dimension(900, 900));
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.add(settings);
        frame.pack();
        frame.setVisible(true);
    }

}
