import domino.controller.GameDominoController;

public class App {
    public static void main(String[] args) {
        // Pour lancer le jeu
        GameDominoController controller = new GameDominoController();

        controller.gameLoop();
    }
}
