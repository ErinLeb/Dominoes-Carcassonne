package domino.view.terminal;

import java.util.List;

import domino.model.BotDomino;
import domino.model.GameDomino;
import domino.model.PlayerDomino;
import domino.model.TileDomino;

public class GameDominoView {

    GameDomino model;

    public GameDominoView(GameDomino model) {
        this.model = model;
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

        String voidLine = " ".repeat(TileDomino.LENGTH_OF_LINE);

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

        String spaceString = " ".repeat((TileDomino.LENGTH_OF_LINE - 1) / 2);

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

    public void surrender(PlayerDomino p) {
        System.out.println("Player " + p.getName() + " has surrendered");
    }

    /**
     * Print the winner(s).
     */
    private void printWinners() {
        List<PlayerDomino> winners = model.getWinners();

        if (winners == null || winners.size() == 0 || winners.get(0) == null) {
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
        PlayerDomino[] ranking = model.getRanking();
        System.out.println("This is the ranking : ");
        for (PlayerDomino p : ranking) {
            System.out.println(p.getName() + " : " + p.getScore() + " points");
        }
    }
}
