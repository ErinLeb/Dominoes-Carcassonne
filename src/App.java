import domino.controller.GameDominoController;
import domino.model.BotDomino;
import domino.model.GameDomino;
import domino.model.PlayerDomino;
import domino.view.terminal.GameDominoView;

public class App {
    public static void main(String[] args) {
        // Pour lancer le jeu
        // GameDomino game = new GameDomino(new PlayerDomino[] { new
        // PlayerDomino("Erin"), new BotDomino() }, 28);
        // GameDominoView view = new GameDominoView(game);

        // GameDominoController controller = new GameDominoController(game, view);
        GameDominoController controller = new GameDominoController();

        controller.gameLoop();
    }
}
