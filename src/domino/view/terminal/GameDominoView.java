package domino.view.terminal;

import java.util.List;

import domino.model.GameDomino;
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

        System.out.println(model.getCurrentPlayer());

        printTileToPlace();

        printBoard();
    }

}
