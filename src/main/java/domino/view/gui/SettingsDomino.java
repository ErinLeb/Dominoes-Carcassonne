package domino.view.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import shared.view.Settings;
import utilities.Pair;

/**
 * This settings class is the panel that contains the settings for the game
 * Domino
 */
public class SettingsDomino extends Settings {

    private JTextField numberOfDominoesText = new JTextField(3);

    private Pair<JTextField, Integer>[] playerNameSelectors;

    private SettingsDominoModel settingsModel = new SettingsDominoModel();

    /**
     * Creates a new settings panel.
     */
    public SettingsDomino() {
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

        GridBagConstraints c = new GridBagConstraints();

        initTotalNumberOfPlayers(c);
        initNumberOfBots(c);
        initNumberOfDominoes(c);

        initNextButton(c);

    }

    @Override
    protected void initFilters() {
        super.initFilters();
        initNumberOfDominoesFilter();

    }

    protected void initNumberOfDominoes(GridBagConstraints c) {
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

    private class SettingsDominoModel extends SettingsModel {
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
    }

    @Override
    public SettingsModel getSettingsModel() {
        return settingsModel;
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
