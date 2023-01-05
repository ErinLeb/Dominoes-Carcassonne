package domino.view.gui;

import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import domino.model.BotDomino;
import domino.model.GameDomino;
import domino.model.PlayerDomino;
import shared.view.Settings;
import shared.view.StartMenu;

/**
 * This settings class is the panel that contains the settings for the game
 * Domino
 */
public class SettingsDomino extends Settings {

    private JTextField numberOfDominoesText = new JTextField(3);

    private SettingsDominoModel settingsModel = new SettingsDominoModel();

    /**
     * Creates a new settings panel.
     */
    public SettingsDomino(JFrame frame, StartMenu home) {
        this.frame = frame;
        this.home = home;

        maxPlayers = 6;
        initNumberSettingsSelector();
    }

    /**
     * Initializes the panel that contains the settings for the number of players,
     * bots and dominoes.
     */
    @Override
    protected void initNumberSettingsSelector() {

        setLayout(new GridBagLayout());
        initFilters();

        initTotalNumberOfPlayers();
        initNumberOfBots();
        initNumberOfDominoes();

        initNextButton();

    }

    @Override
    protected void initFilters() {
        super.initFilters();
        initNumberOfDominoesFilter();

    }

    protected void initNumberOfDominoes() {
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

    @Override
    protected void notifyMissingSettings() {
        super.notifyMissingSettings();
        if (!settingsModel.isCorrectNumberOfDominoes() || !numberOfDominoesText.getText().matches("(\\d)+")) {
            JOptionPane.showMessageDialog(SettingsDomino.this,
                    "Please enter a number between 15 and 100 for the number of dominoes");
        }
    }

    @Override
    protected void startGame() {
        GameDomino gameModel = new GameDomino(settingsModel.players, settingsModel.numberOfDominoes);
        GameDominoPanel gameView = new GameDominoPanel(gameModel, frame, home);

        frame.setTitle("Domino");
        frame.setContentPane(gameView);
        frame.revalidate();
        frame.repaint();
    }

    private class SettingsDominoModel extends SettingsModel<PlayerDomino> {
        private int numberOfDominoes;

        private SettingsDominoModel() {
            super();
            numberOfDominoes = -1;
        }

        @Override
        public void updateSettings() {
            super.updateSettings();
            numberOfDominoes = Integer.parseInt(numberOfDominoesText.getText());
        }

        @Override
        public boolean checkSettings() {
            return super.checkSettings() && isCorrectNumberOfDominoes();
        }

        public boolean isCorrectNumberOfDominoes() {
            return numberOfDominoes >= 15 && numberOfDominoes <= 100;
        }

        public int getNumberOfDominoes() {
            return numberOfDominoes;
        }

        @Override
        public void generatePlayers() {
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
    }

    @Override
    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame();

        StartMenu home = new StartMenu(frame);

        SettingsDomino settings = new SettingsDomino(frame, home);

        frame.setPreferredSize(new java.awt.Dimension(950, 950));
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.add(settings);
        frame.pack();
        frame.setVisible(true);
    }

}
