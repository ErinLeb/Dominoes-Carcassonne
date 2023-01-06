package domino.model;

import java.util.List;

import exceptions.NoPossibleMovementsException;
import exceptions.TileNotFoundException;
import exceptions.UnableToTurnException;
import utils.Pair;

public class BotDomino extends PlayerDomino {

    public BotDomino() {
        super(botNames[(int) (Math.random() * botNames.length)]);
    }

    public void changeName() {
        name = botNames[(int) (Math.random() * botNames.length)];
    }

    public void play(GameDomino model)
            throws TileNotFoundException, UnableToTurnException, NoPossibleMovementsException {

        List<Pair<Integer, Integer>> possibleMoves = model.findPossiblePlacements();

        if (possibleMoves.isEmpty())
            throw new NoPossibleMovementsException();

        TileDomino tileToPlace = model.getTileToPlace().copy();

        Pair<Integer, Integer> position = new Pair<>(-5, -5);
        int points = 0;
        int nbTurns = 0;

        for (Pair<Integer, Integer> move : possibleMoves) {
            for (int i = 0; i < 4; i++) {
                int pointsToPlace = model.pointsIfPlaced(move.first, move.second, tileToPlace);

                if (pointsToPlace >= points) {
                    points = pointsToPlace;
                    position = move;
                    nbTurns = i;
                }

                try {
                    tileToPlace.turnRight(1);
                } catch (UnableToTurnException e) {
                    //
                }
            }
        }

        model.turn(true, nbTurns);

        model.place(position.first, position.second, this);
    }
}