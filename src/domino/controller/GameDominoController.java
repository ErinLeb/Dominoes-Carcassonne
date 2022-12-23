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
import utilities.Pair;

/**
 * This class represents the controller of the Domino game, following an MVC
 * model
 */
public class GameDominoController {
    // attributes
    GameDomino model;
    GameDominoView view = new GameDominoView(model);

    Scanner sc = new Scanner(System.in);

    public GameDominoController(GameDomino game, GameDominoView view) {
        this.model = game;
        this.view = view;
    }

    public GameDominoController() {
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

            if (valid) {
                if (nbPlayers < 2 || nbPlayers > 6) {
                    System.out.println("Please choose a number between 2 and 6");
                    valid = false;
                }
            }
        }

        PlayerDomino[] players = new PlayerDomino[nbPlayers];

        System.out.println(
                "You are now going to enter the names of the real players or if the players are virtual, in the order each will play \n");

        for (int i = 1; i <= nbPlayers; i++) {
            while (true) {
                System.out.println("Is the player n°" + i + " is a bot ? (yes/no)");
                String answer = sc.nextLine();
                if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
                    BotDomino bot = new BotDomino();

                    while (!checkName(players, bot)) {
                        bot.changeName();
                    }

                    players[i - 1] = bot;
                    System.out.println("Player n°" + i + " will be " + bot.getName() + ".");
                    break;
                } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
                    System.out.println("What's the name of the player n°" + i + " ?");
                    String name = sc.nextLine();
                    if (name.equals("")) {
                        players[i - 1] = new PlayerDomino();
                    } else {
                        PlayerDomino p = new PlayerDomino(name);

                        while (!checkName(players, p)) {
                            System.out.println("Nom déjà pris, veuillez en choisir un autre.");
                            p.setName(sc.nextLine());
                        }

                        players[i - 1] = p;
                    }
                    break;
                }
            }
        }

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

            if (valid) {
                if (nbTiles < 15 || nbTiles > 100) {
                    System.out.println("Please choose a number between 15 and 100");
                    valid = false;
                }
            }
        }

        model = new GameDomino(players, nbTiles);
        view = new GameDominoView(model);
    }

    /**
     * Checks if the name of {@code p} is already used by one of the Player in
     * {@code players}
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
            } catch (NoPossibleMovementsException e) {
                System.out.println("Bot " + model.getCurrentPlayer().getName() + " passed");
            } catch (Exception e) {
                e.printStackTrace();
            }
            view.printUpdateGameRound();
            return;
        }

        view.printUpdateGameRound();

        String command;

        do {
            System.out.println("Enter a command: ");
            command = sc.nextLine();
        } while (!parseInput(command, model.getCurrentPlayer()));

        if (model.endGame()) {
            view.endGame();
        }
    }

    /**
     * The main loop of the game
     */
    public void gameLoop() {

        while (model.isGameOn()) {
            playRound();

            if (!model.isGameOn()) {

                boolean valid = false;
                while (!valid) {

                    System.out.println("Do you want to play again ? (Yes/No)");

                    String answer = sc.nextLine();
                    if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
                        model.setIsGameOn(true);
                        valid = true;
                    } else if (answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("n")) {
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
    public boolean parseInput(String input, PlayerDomino player) {

        String[] args = input.split(" ");

        try {
            switch (args[0].toLowerCase()) {
                case "pass":
                    List<Pair<Integer, Integer>> p = model.findPossiblePlacements();

                    if (p.isEmpty())
                        return true;

                    throw new TileCanBePlacedException();
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
                    // TODO : if there are still players left, continue the game (attribute boolean
                    // ?)
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
                        }
                        if (args.length > 3)
                            throw new IllegalArgumentException("Invalid number of arguments");

                        model.turn(args[1].toUpperCase().charAt(0) == 'R', Integer.parseInt(args[2]));
                        view.printTileToPlace();

                    }
                    return false;
                case "turnLeft":
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
                case "turnRight":
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
        } catch (Exception e) {
            System.out.println("Invalid command");
            System.out.println(e.getMessage());
            e.printStackTrace(); // TODO: remove this line in the final version
        }

        return false;
    }
}
