package carcassonne.model;

import java.awt.Color;
import java.util.List;

import carcassonne.model.TileCarcassonne.SideSelector;
import exceptions.NoPossibleMovementsException;
import exceptions.TileNotFoundException;
import exceptions.UnableToTurnException;
import utilities.Pair;

public class BotCarcassonne extends PlayerCarcassonne {

    public BotCarcassonne(Color colorPawn) {
        super(botNames[(int) (Math.random() * botNames.length)], colorPawn);
    }

    public void changeName() {
        name = botNames[(int) (Math.random() * botNames.length)];
    }

    public void play(GameCarcassonne model)
            throws TileNotFoundException, UnableToTurnException, NoPossibleMovementsException {

        List<Pair<Integer, Integer>> possibleMoves = model.findPossiblePlacements();

        if (possibleMoves.isEmpty())
            throw new NoPossibleMovementsException();

        Pair<Integer, Integer> chosenPosition = possibleMoves.get((int) (Math.random() * possibleMoves.size()));

        TileCarcassonne tileToPlace = model.getTileToPlace().copy();

        placeTile(model, chosenPosition, tileToPlace);

        placePawn(model);

    }

    private void placeTile(GameCarcassonne model, Pair<Integer, Integer> chosenPosition, TileCarcassonne tileToPlace)
            throws UnableToTurnException, TileNotFoundException {
        int nbTurns = 0;

        for (int i = 0; i < 4; i++) {
            if (tileToPlace.canBePlaced(model.getNeighborsFromPosition(chosenPosition.first, chosenPosition.second))) {
                break;
            }
            tileToPlace.turnRight(1);
            nbTurns++;
        }

        model.turn(true, nbTurns);

        model.place(chosenPosition.first, chosenPosition.second, this);
    }

    private void placePawn(GameCarcassonne model) {

        TileCarcassonne tile = model.getTileToPlace();

        if (remainingPawns == 0 || Math.random() < 0.5)
            return;

        Pair<SideSelector, Integer> possiblePositions = tile.getRandomPlacingPosition();

        tile.placePawn(possiblePositions.first, possiblePositions.second);
    }
}
