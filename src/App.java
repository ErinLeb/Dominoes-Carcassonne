import domino.controller.GameDominoController;
import domino.model.GameDomino;
import domino.view.terminal.GameDominoView;

public class App {
    public static void main(String[] args) {
        // Pour lancer le jeu
        GameDomino game = new GameDomino(2, 28);
        GameDominoView view = new GameDominoView(game);

        GameDominoController controller = new GameDominoController(game, view);

        controller.gameLoop();
    }
}
