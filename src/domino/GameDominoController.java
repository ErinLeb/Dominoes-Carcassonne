package domino;

import java.util.Scanner;

/**
 * This class represents the controller of the Domino game, following an MVC
 * model
 */
public class GameDominoController {
    // attributes
    GameDomino game;
    Scanner sc = new Scanner(System.in);

    public GameDominoController(GameDomino game) {
        this.game = game;
    }

    /**
     * Plays a single round of the game (just a player's turn)
     */
    public void playRound() {
        System.out.println("Round " + game.getNbRounds() + " :");

        game.updateGameRound();

        String command;

        do {
            System.out.println("Enter a command: ");
            command = sc.nextLine();
        } while (!game.playRound(command));
    }

    /**
     * The main loop of the game
     */
    public void gameLoop() {

        while (game.isGameOn()) {
            playRound();
        }

    }
}
