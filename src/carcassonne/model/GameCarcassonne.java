package carcassonne.model;

import shared.model.Game;
import utilities.Expandable2DArray;
import utilities.Pair;

public class GameCarcassonne extends Game<SideCarcassonne, TileCarcassonne> {

    // Constructor

    /**
     * Creates a game of Carcassonne with {@code nbPlayers} players
     */
    public GameCarcassonne(int nbPlayers) {
        // Players
        players = new PlayerCarcassonne[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            players[i] = new PlayerCarcassonne();
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

}
