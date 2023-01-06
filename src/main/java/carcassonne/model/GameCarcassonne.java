package carcassonne.model;

import java.awt.Color;

import shared.model.Game;
import utils.Expandable2DArray;
import utils.Pair;

public class GameCarcassonne extends Game<SideCarcassonne, TileCarcassonne> {

    // Constructor

    /**
     * Creates a game of Carcassonne with {@code nbPlayers} players
     */
    public GameCarcassonne(int nbPlayers) {
        // Colors for pawns
        Color[] colorPawns = initColors(nbPlayers);

        for (int i = 0; i < nbPlayers; i++) {
            players[i] = new PlayerCarcassonne(colorPawns[i]);
        }
        currentPlayer = 0;

        // Deck
        deckSize = 72;
        deck = new DeckCarcassonne();

        currentTile = deck.draw();
        currentPosition = new Pair<>(0, 0);

        board = new Expandable2DArray<>(currentTile);
    }

    public GameCarcassonne(PlayerCarcassonne[] players) {
        // Colors for pawns
        Color[] colorPawns = initColors(players.length);

        for (int i = 0; i < players.length; i++) {
            players[i].setColor(colorPawns[i]);
        }
        // Players
        this.players = players;
        currentPlayer = 0;

        // Deck
        deckSize = 72;
        deck = new DeckCarcassonne();

        currentTile = deck.draw();
        currentPosition = new Pair<>(0, 0);

        board = new Expandable2DArray<>(currentTile);
    }

    // Methods

    private Color[] initColors(int nbPlayers) {
        Color[] pawns = new Color[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            switch (i) {
                case 0:
                    pawns[i] = Color.RED;
                    break;
                case 1:
                    pawns[i] = Color.BLUE;
                    break;
                case 2:
                    pawns[i] = Color.GREEN;
                    break;
                case 3:
                    pawns[i] = Color.YELLOW;
                    break;
                default:
                    break;
            }
        }
        return pawns;
    }

    @Override
    public void initGame(boolean resetScore) {
        // resetScore if score implemented

        // nbRounds
        nbRounds = 0;

        // isInGame
        for (int i = 0; i < players.length; i++) {
            players[i].setInGame(true);
        }

        // deck
        deck = new DeckCarcassonne();

        // currentPlayer
        currentPlayer = 0;

        // currentTile & currentPosition
        currentTile = deck.draw();
        currentPosition = new Pair<>(0, 0);

        // board
        board = new Expandable2DArray<>(currentTile);

        // isGameOn
        isGameOn = true;
    }

    @Override
    public void updateGameRound() {
        super.updateGameRound();
        tileToPlace.setPlayer(((PlayerCarcassonne) getCurrentPlayer()));
    }

}
