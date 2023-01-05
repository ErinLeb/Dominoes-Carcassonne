package domino.view.terminal;

import java.util.List;

import domino.model.BotDomino;
import domino.model.GameDomino;
import domino.model.PlayerDomino;
import domino.model.TileDomino;
import shared.model.Player;
import utils.StringOperations;

public class GameDominoView {
    // Attributes
    GameDomino model;

    // Constructor
    public GameDominoView(GameDomino model) {
        this.model = model;
    }

    // Methods

    public static void printRules() {
        String rules = "\n";
        rules += "########################################################*RULES*#######################################################\n";

        rules += "In this version of Dominoes, the dominoes are squares and have three figures on each of their sides.\n";
        rules += "\nHow to win ?\n";
        rules += "The winner is the player who has the highest score when there is no tile to place anymore. Another way to win is to be the only player to last in the game if the others surrendered.\n";
        rules += "\nHow to score points ?\n";
        rules += "You score when you place successfully a tile on the board. The amount of points you gain is the sum of the numbers in contact with those of the neighboring tiles.\n";
        rules += "\nThe rules are easy :\n";
        rules += "There are between 2 and 6 players. You can also play against AIs by choosing so at the beginning.\n";
        rules += "At the beginning, you need to choose the number of tiles you want (one placed on the board to start and the others in the deck).\n";
        rules += "You can stop the game once it has begun using the command 'quit'.\n";
        rules += "A player can decide to quit the game. If after this decision there is only one player left in the game, this one wins by default, else, the game continues with the players still in the game.\n";

        rules += "########################################################*RULES*#######################################################\n";
        System.out.println(rules);
    }

    public static void printCommands() {
        String commands = "\n";
        commands += "#####################################################*COMMANDS*##########################################################\n";

        commands += "[ID] = the id of a tile on the board\n";
        commands += "[Direction] = up/down/right/left\n\n";
        commands += "pass - if you can't place the tile, it is discarded and the next player plays \n";
        commands += "p/print/print b/ print board/printboard - print the board \n";
        commands += "p/print [string]/printtile - print tile to place\n";
        commands += "surrender - abandon the game\n";
        commands += "mv/move [Direction] [number of tile]  - move the board according to the direction and number of tiles (1 by default)\n";
        commands += "tr/turn/turn r [number] - turn the tile to place clockwise [number] times (1 by default)\n";
        commands += "turnRight [number] - turn the tile to place clockwise [number] times\n";
        commands += "turnLeft [number] - turn the tile to place anticlockwise [number] times\n";
        commands += "pl/place [line] [column] - place the tile to place in the position (line, column) if possible\n";
        commands += "pl/place [ID] [Direction] - place the tile to place in the position [Direction] relative to the tile with the id [ID]. (e.g : place 4 u -> above the tile #4)\n";
        commands += "q/quit - quit the game\n";
        commands += "h/help - print the commands\n";

        commands += "#####################################################*COMMANDS*##########################################################\n";
        System.out.println(commands);
    }

    /**
     * Prints on the screen the tile to be placed
     */
    public void printTileToPlace() {
        if (model.getTileToPlace() == null)
            return;

        System.out.println("Current tile: ");
        model.getTileToPlace().printTile();
        System.out.println("-----------------------------------------------");
    }

    /**
     * Prints the relative board.
     */
    public void printBoard() {
        System.out.println("-------------------- Board --------------------");

        // Get the sub array to print
        List<List<TileDomino>> relativeBoard = model.getCurrentSubArray();

        // Print the column numbers
        System.out.print("  ");

        String spaceString = StringOperations.repeat(" ", (TileDomino.LENGTH_OF_LINE - 1) / 2);

        for (int i = 1; i <= relativeBoard.get(0).size(); i++) {
            System.out.print(spaceString + i + spaceString + " ");
        }

        System.out.println();

        // Print all the lines
        for (int i = 0; i < relativeBoard.size(); i++) {
            String[] lines = getStringLine(relativeBoard, i);

            for (String line : lines)
                System.out.println(line);
        }

        System.out.println("-----------------------------------------------");
    }

    /**
     * Helper function for {@link #printBoard()}. Returns an array of {@code String}
     * which represents a line of the relative board.
     * 
     * @param relativeBoard The relative board
     * @param y             Y coordinate of the line
     * @return An array of {@code String} which represents a line of the relative
     *         board
     */
    private String[] getStringLine(List<List<TileDomino>> relativeBoard, int y) {
        String[] lines = new String[TileDomino.COLUMNS_LENGTH];

        String voidLine = StringOperations.repeat(" ", TileDomino.LENGTH_OF_LINE);

        // Get the line number
        for (int j = 0; j < lines.length; j++)
            lines[j] = j == TileDomino.COLUMNS_LENGTH / 2 ? Integer.toString(y + 1) + " " : "  ";

        // Get the information of the lines
        for (int i = 0; i < relativeBoard.get(y).size(); i++) {
            // If the tile is null we add the corresponding spaces
            if (relativeBoard.get(y).get(i) == null) {
                for (int j = 0; j < lines.length; j++)
                    lines[j] += voidLine;

            } else {
                String[] stringRepresentation = relativeBoard.get(y).get(i).getStringRepresentation();
                for (int j = 0; j < lines.length; j++)
                    lines[j] += stringRepresentation[j];
            }
        }

        return lines;
    }

    /**
     * Prints the updated information of the round.
     */
    public void printUpdateGameRound() {

        System.out.println("===================================== Round " + model.getNbRounds()
                + " ===========================================");
        System.out.println();

        if (model.getCurrentPlayer() instanceof BotDomino) {
            System.out.println("Bot " + model.getCurrentPlayer().getName() + " is playing");
            System.out.println("Score: " + model.getCurrentPlayer().getScore());
            System.out.println();
            return;
        }

        System.out.println("Player " + model.getCurrentPlayer().getName() + " is playing");
        System.out.println("Score: " + model.getCurrentPlayer().getScore());
        System.out.println();

        printTileToPlace();

        printBoard();
    }

    public void surrender(Player p) {
        System.out.println("Player " + p.getName() + " has surrendered");
    }

    /**
     * Print the winner(s).
     */
    private void printWinners() {
        List<Player> winners = model.getWinners();

        if (winners == null || winners.isEmpty() || winners.get(0) == null) {
            System.out.println("There is no winner !");
        }

        String message = "";
        for (int i = 0; i < winners.size(); i++) {
            if (i == winners.size() - 1) {
                message += winners.get(i).getName() + " ";
            } else if (i == winners.size() - 2) {
                message += winners.get(i).getName() + " and ";
            } else {
                message += winners.get(i).getName() + ", ";
            }
        }

        if (winners.size() > 1) {
            System.out.println("Equality :");
        }
        System.out.println(message + "won !");
    }

    /**
     * Print the winner(s) and the ranking of the players.
     */
    public void endGame() {
        System.out
                .println("===================================== End of the Game =====================================");
        // print the winners
        printWinners();

        // print the ranking
        Player[] ranking = model.getRanking();
        System.out.println("This is the ranking : ");
        for (Player p : ranking) {
            System.out.print(p.getName() + " : " + p.getScore() + " points");

            // print which player surrendered
            if (p.isInGame()) {
                System.out.println();
            } else {
                System.out.println(" (surrendered)");
            }
        }
    }

    public static void main(String[] args) {
        // printRules();
        // printCommands();

        PlayerDomino p1 = new PlayerDomino("Erin");
        PlayerDomino p2 = new PlayerDomino("Yago");
        PlayerDomino p3 = new PlayerDomino("Surrendered");

        PlayerDomino[] players = { p1, p2, p3 };
        GameDomino game = new GameDomino(players, 4);
        GameDominoView view = new GameDominoView(game);

        p1.incrementScore(10);
        p2.incrementScore(10);
        p3.incrementScore(12);
        game.surrender(p3);

        view.endGame();
    }
}
