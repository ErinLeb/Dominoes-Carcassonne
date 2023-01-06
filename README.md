# Game of Dominoes and Carcassonne

Welcome to the Game of Dominoes and Carcassonne! This project is a Java application that combines two classic tabletop games into one convenient package. The GUI allows you to easily play both games with friends or against AI opponents.

## Features

This project includes the following features:

- Two classic tabletop games in one convenient package : Dominoes and Carcassonne.
- A GUI interface for easy play, as well as a terminal version for a more traditional gaming experience of the game Dominoes.
- Built-in game rules for both Dominoes and Carcassonne, accessible from the main menu.
- AI opponents
- Customizable game settings, including the number of players (2-6 for Dominoes, 2-4 for Carcassonne), the number of tiles (15-100 for Dominoes), and the number of bots (0-number of players).  
- The possibility to quit the game any time.  
- A final ranking of players after each game of Dominoes.  
- The possibility to start a new game once you finished one.  
- The possibility to play again with the same parameters in the terminal version (including keeping your scores for a return match !)  

## Installation

From the repository `/poo3jeux`, run the command `./play.sh` to compile and launch the application.
To run directly the terminal version of the game, run the command `./play.sh terminal`.

## How to Play

### Dominoes

To play a game of dominoes using the GUI, follow these steps:

1. Click on the domino displayed on the main menu.
2. Select the number of players (2-6), the number of bots (0-number of players), the number of tiles (15-100) and the names of the human players.
3. Click "Start Game" to begin (if using the GUI).
4. On your turn, place the given domino on the board according to the game rules. If you cannot place it, you can pass your turn. You can also surrender.
5. The game ends when there are no more tiles to draw or only one player left.
6. Among the players still in the game, the winner is the one with the highest score .

To play a game of dominoes using the terminal, follow these steps:

1. Run the command `./play.sh terminal`.
2. Follow the instructions on the screen.

### Carcassonne

To play a game of Carcassonne, follow these steps:

1. Click the Carcassonne tile on the main menu.
2. Select the number of players (2-4), the number of bots (0-number of players) and the names of the human players.
3. Click "Start Game" to begin.
4. On your turn, place one of your followers on the tile if you wish and then place the tile on the board according to the game rules. If you cannot place it, you can pass your turn. You can also surrender any time.
5. The game ends when the draw pile is empty or if only one player remains in the game.

## Folder Structure

The folder structure of this project is as follows:

- `src`: Contains the source code for the project.
  - `main`
    - `java`
      - `domino`: Contains the source code for the game Dominoes.
        - `controller`: Contains the source code for the controllers used by the game Dominoes.
        - `model`: Contains the source code for the models used by the game Dominoes.
        - `view`: Contains the source code for the views used by the game Dominoes.
          - `terminal`: Contains the source code for the terminal views used by the game Dominoes.
          - `gui`: Contains the source code for the GUI views used by the game Dominoes.
      - `carcassonne`: Contains the source code for the game Carcassonne.
        - `model`: Contains the source code for the models used by the game Carcassonne.
        - `view`: Contains the source code for the views used by the game Carcassonne.
      - `interfaces`: Contains the source code for the interfaces used by both games.
      - `exceptions`: Contains the source code for the exceptions used by both games.
      - `shared`: Contains the source code for the shared classes used by both games.
        - `model`: Contains the source code for the shared models used by both games.
        - `gui`: Contains the source code for the shared GUI views used by both games.
      - `utils`: Contains the source code for the utility classes used by both games.
    - `resources`: Contains the resources used by the project.

## Authors

This project was created by Erin Le Boulc'H and Yago Iglesias Vázquez.
