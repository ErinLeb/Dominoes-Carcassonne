package domino.controller;

import java.util.List;
import java.util.Scanner;

import domino.model.GameDomino;
import domino.view.terminal.GameDominoView;
import exceptions.TileCanBePlacedException;
import interfaces.Placeable;
import interfaces.Placeable.Direction;
import shared.model.Player;
import utils.Pair;

public class GameDominoCommandParser {

    Scanner sc;

    GameDomino model;
    GameDominoView view;

    public GameDominoCommandParser(Scanner sc, GameDomino model, GameDominoView view) {
        this.sc = sc;
        this.model = model;
        this.view = view;
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

            return handlePassWithPossiblePlacement();

        } catch (Exception e) {
            System.out.println("Invalid command");
            System.out.println(e.getMessage());
        }

        return false;
    }

    private boolean handlePassWithPossiblePlacement() {

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

    }

}
