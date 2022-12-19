import domino.GameDomino;
import domino.GameDominoController;

public class App {
    public static void main(String[] args) {
        // Pour lancer le jeu
        GameDomino game = new GameDomino(2, 28);

        GameDominoController controller = new GameDominoController(game);

        controller.gameLoop();
    }
}
