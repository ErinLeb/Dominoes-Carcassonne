package domino.controller;

import java.util.List;
import java.util.Scanner;

import domino.model.BotDomino;
import domino.model.GameDomino;
import domino.model.PlayerDomino;
import domino.view.terminal.GameDominoView;
import exceptions.NoPossibleMovementsException;
import exceptions.TileCanBePlacedException;
import interfaces.Placeable;
import interfaces.Placeable.Direction;
import shared.model.Player;
import utilities.Pair;

/**
 * This class represents the controller of the Domino game, following an MVC
 * model
 */
public class GameDominoController {
    // Attributes
    GameDomino model;
    GameDominoView view;

    Scanner sc = new Scanner(System.in);

    // Constructor
    public GameDominoController(GameDomino game, GameDominoView view) {
        this.model = game;
        this.view = view;
    }

    public GameDominoController() {
        GameDominoView.printRules();
        initGame();
    }

    // Methods

    /**
     * Initializes the model and the view according to what the user enters as
     * parameters.
     */
    public void initGame() {
        // Number of players
        System.out.println("How many players do you want to play ?");
        int nbPlayers = 2; // default

        boolean valid = false;
        while (!valid) {
            try {
                valid = true;
                nbPlayers = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                valid = false;
                System.out.println("Invalid command, you must enter a number");
            }

            if (valid && (nbPlayers < 2 || nbPlayers > 6)) {
                System.out.println("Please choose a number between 2 and 6");
                valid = false;
            }
        }

        PlayerDomino[] players = new PlayerDomino[nbPlayers];

        // Type and names of the players
        System.out.println(
                "You are now going to enter the names of the real players or if the players are virtual, in the order each will play. \n");

        // True if there is at least a real player
        boolean printCommands = getPlayerNames(nbPlayers, players);

        // Number of Tiles
        System.out.println("Now please choose the number of tiles you want in the deck (between 15 and 100)");
        int nbTiles = 28; // default

        valid = false;
        while (!valid) {
            try {
                valid = true;
                nbTiles = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                valid = false;
                System.out.println("Invalid command, you must enter a number");
            }

            if (valid && (nbTiles < 15 || nbTiles > 100)) {
                System.out.println("Please choose a number between 15 and 100");
                valid = false;
            }
        }

        // print rules if there is at least a player who needs it
        if (printCommands) {
            GameDominoView.printCommands();
        }

        model = new GameDomino(players, nbTiles);
        view = new GameDominoView(model);
    }

    /**
     * This method is used to get the names of the players. It updates the array of
     * players. Returns {@code true} if at least one player is real and needs to
     * read the commands.
     * 
     * @param nbPlayers the number of players
     * @param players   the array of players
     * @return {@code true} if at least one player is real
     */
    private boolean getPlayerNames(int nbPlayers, PlayerDomino[] players) {
        boolean printCommands = false;

        for (int i = 1; i <= nbPlayers; i++) {
            boolean isNameValid = false;
            while (!isNameValid) {
                System.out.println("Is the player n°" + i + " is a bot ? (yes/no)");
                String playAgain = sc.nextLine();

                if (playAgain.equalsIgnoreCase("yes") || playAgain.equalsIgnoreCase("y")) {
                    BotDomino bot = new BotDomino();

                    while (!checkName(players, bot)) {
                        bot.changeName();
                    }

                    players[i - 1] = bot;
                    System.out.println("Player n°" + i + " will be " + bot.getName() + ".");
                    isNameValid = true;

                } else if (playAgain.equalsIgnoreCase("no") || playAgain.equalsIgnoreCase("n")) {
                    printCommands = true;

                    System.out.println("What's the name of the player n°" + i + " ?");

                    String name = sc.nextLine();

                    if (name.equals("")) {
                        players[i - 1] = new PlayerDomino();

                    } else {
                        PlayerDomino p = new PlayerDomino(name);

                        while (!checkName(players, p)) {
                            System.out.println("Name already taken, please choose another name.");
                            p.setName(sc.nextLine());
                        }

                        players[i - 1] = p;
                    }
                    isNameValid = true;
                }
            }
        }
        return printCommands;
    }

    /**
     * Checks if the name of {@code p} is already used by one of the Player in
     * {@code players}
     * 
     * @param players the array of players
     * @param p       the player to check
     */
    private boolean checkName(PlayerDomino[] players, PlayerDomino p) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getName().equalsIgnoreCase(p.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Plays a single round of the game (just a player's turn)
     */
    public void playRound() {
        model.updateGameRound();

        if (model.getCurrentPlayer() instanceof BotDomino) {

            try {
                ((BotDomino) model.getCurrentPlayer()).play(model);
                view.printUpdateGameRound();
            } catch (NoPossibleMovementsException e) {
                view.printUpdateGameRound();
                System.out.println("Bot " + model.getCurrentPlayer().getName() + " passed");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            view.printUpdateGameRound();

            String command;
            do {
                System.out.println("Enter a command: ");
                command = sc.nextLine();
            } while (!parseInput(command, model.getCurrentPlayer()));
        }

    }

    /**
     * The main loop of the game
     */
    public void gameLoop() {

        while (model.isGameOn()) {
            playRound();

            // if its the end of the game, we print the winners and the ranking
            if (model.endGame()) {
                view.endGame();
            }

            if (!model.isGameOn()) {

                boolean valid = false;
                // We ask if the players want to play again until the playAgain is yes/y or no/n
                while (!valid) {

                    System.out.println("Do you want to play again ? (Yes/No)");
                    String playAgain = sc.nextLine();

                    if (playAgain.equalsIgnoreCase("yes") || playAgain.equalsIgnoreCase("y")) {
                        // The game is going to start again

                        while (true) {
                            System.out.println("Do you want to keep the same parameters ? (Yes/No)");
                            String keepParameters = sc.nextLine();

                            // We keep the same parameters
                            if (keepParameters.equalsIgnoreCase("yes") || keepParameters.equalsIgnoreCase("y")) {

                                while (true) {
                                    System.out.println("Do you want to reset your current score to 0 ? (Yes/No)");
                                    String resetScore = sc.nextLine();

                                    // We restart the game
                                    if (resetScore.equalsIgnoreCase("yes") || resetScore.equalsIgnoreCase("y")) {
                                        model.initGame(true);
                                        break;
                                    }

                                    // We keep the same scores and restart the game
                                    if (resetScore.equalsIgnoreCase("no") || resetScore.equalsIgnoreCase("n")) {
                                        model.initGame(false);
                                        break;
                                    }
                                }
                                break;
                            }

                            // We initialize the game with new parameters
                            if (keepParameters.equalsIgnoreCase("no") || keepParameters.equalsIgnoreCase("n")) {
                                initGame();
                                break;
                            }
                        }
                        valid = true;

                    } else if (playAgain.equalsIgnoreCase("no") || playAgain.equalsIgnoreCase("n")) {
                        // Real end of the game
                        valid = true;

                    } else {
                        System.out.println("Invalid command");
                    }
                }
            }
        }
    }

    /**
     * Parses the command given by the player and executes it.
     * 
     * @param input  The command given by the player
     * @param player The player who gave the command
     * @return {@code true} if the command was executed and the turn should be
     *         finished, {@code false} otherwise
     */
    public boolean parseInput(String input, Player player) {
        String[] args = input.split(" ");

        try {
            switch (args[0].toLowerCase()) {
                case "pass":
                    List<Pair<Integer, Integer>> p = model.findPossiblePlacements();

                    if (p.isEmpty()) {
                        return true;
                    } else {
                        throw new TileCanBePlacedException();
                    }
                case "p":
                case "print":
                    if (args.length == 1) {
                        view.printBoard();
                    } else if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("b") || args[1].equalsIgnoreCase("board")) {

                            view.printBoard();
                        } else {
                            view.printTileToPlace();
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid number of arguments");
                    }
                    return false;
                case "printtile":
                    view.printTileToPlace();
                    return false;
                case "printboard":
                    view.printBoard();
                    return false;
                case "surrender":
                    model.surrender(player);
                    view.surrender(player);
                    return true;
                case "mv":
                case "move":
                    if (args.length < 2 || args.length > 3)
                        throw new IllegalArgumentException("Invalid number of arguments");

                    if (args[1].matches("^\\d+")) {
                        model.move(Integer.parseInt(args[1]));
                    } else {
                        Direction direction = Placeable.stringToDirection(args[1].toUpperCase());
                        model.move(direction, args.length == 3 ? Integer.parseInt(args[2]) : 1);
                    }

                    view.printBoard();

                    return false;
                case "tr":
                case "turn":
                    if (args.length == 1) {
                        model.turn(true, 1);
                        view.printTileToPlace();

                    } else {
                        if (args.length == 2) {
                            model.turn(true, Integer.parseInt(args[1]));
                            view.printTileToPlace();
                            return false;
                        }
                        if (args.length > 3)
                            throw new IllegalArgumentException("Invalid number of arguments");

                        model.turn(args[1].toLowerCase().charAt(0) == 'r', Integer.parseInt(args[2]));
                        view.printTileToPlace();
                    }
                    return false;
                case "turnleft":
                    if (args.length == 1) {
                        model.turn(false, 1);
                        view.printTileToPlace();

                    } else {
                        if (args.length != 2)
                            throw new IllegalArgumentException("Invalid number of arguments");

                        model.turn(false, Integer.parseInt(args[1]));
                        view.printTileToPlace();

                    }
                    return false;
                case "turnright":
                    if (args.length == 1) {
                        model.turn(true, 1);
                        view.printTileToPlace();

                    } else {
                        if (args.length != 2)
                            throw new IllegalArgumentException("Invalid number of arguments");

                        model.turn(true, Integer.parseInt(args[1]));
                        view.printTileToPlace();

                    }
                    return false;
                case "pl":
                case "place":
                    if (args.length != 3)
                        throw new IllegalArgumentException("Invalid number of arguments");
                    model.place(args[1], args[2], player);
                    return true;
                case "q":
                case "quit":
                    model.quit();
                    return true;
                case "h":
                case "help":
                    GameDominoView.printCommands();
                    return false;
                default:
                    System.out.println("Invalid command");
                    return false;
            }
        } catch (TileCanBePlacedException e) {
            System.out.print(e.getMessage());

            while (true) {
                System.out.println(" Are you sure you still want to pass ? (Yes/No)");
                String pass = sc.nextLine();

                if (pass.equalsIgnoreCase("yes") || pass.equalsIgnoreCase("y")) {
                    return true;
                }
                if (pass.equalsIgnoreCase("no") || pass.equalsIgnoreCase("n")) {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid command");
            System.out.println(e.getMessage());
            e.printStackTrace(); // TODO: remove this line in the final version
        }

        return false;
    }
}
