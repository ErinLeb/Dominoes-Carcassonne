package domino.model;

import java.util.List;

import exceptions.NoPossibleMovementsException;
import exceptions.TileNotFoundException;
import exceptions.UnableToTurnException;
import utilities.Pair;

public class BotDomino extends PlayerDomino {

    protected static final String[] names = { "Aiwendil", "Alatar", "Amroth", "Anarion", "Ancalagon", "Annatar",
            "Aragorn", "Arathorn", "Arien", "Arvedui", "Asfaloth", "Aule", "Beleg", "Beorn", "Beregond", "Beruthiel",
            "Bilbo", "Bill", "Boromir", "Carcaroth", "Celeborn", "Celebrimbor", "Cirdan", "Curumo", "Deagol",
            "Denethor", "Dior", "Durin", "Earendil", "Ecthelion", "Elanor", "Elbereth", "Elendil", "Elladan", "Elrohir",
            "Elrond", "Elros", "Elwe", "Elwing", "Engwe", "Eol", "Eomer", "Eonwe", "Eorl", "Eowyn", "Este", "Fangorn",
            "Faramir", "Feanaro", "Feanor", "Filgolfin", "Finarfin", "Finguilas", "Finrod", "Frodo", "Galadriel",
            "Gandalf", "Gil-Galad", "Gimli", "Glaurung", "Gloin", "Glorfindel", "Goldberry", "Gollum", "Gothmog",
            "Grima", "Gwaihir", "Hama", "Helm", "Huan", "Hurin", "Idril", "Ilmare", "Indis", "Irmo", "Isildur",
            "Khamul", "Legolas", "Lorien", "Luthien", "Maedhros", "Mairon", "Mandos", "Manwe", "Melian", "Melkor",
            "Meriadoc", "Min", "Morgoth", "Nessa", "Nienna", "Niennor", "Ohtar", "Olorin", "Orodreth", "Orome", "Osse",
            "Pallando", "Peregrin", "Quickbeam", "Radagast", "Rose", "Salmar", "Sam", "Saruman", "Sauron", "Scatha",
            "Shadowfax", "Shelob", "Smaug", "Smeagol", "Tar-Minyatur", "Tar-Palatir", "Thengel", "Theoden", "Thingol",
            "Thorin", "Thorondor", "Thrain", "Tilion", "Tom Bombadil", "Treebeard", "Tulkas", "Tuor", "Turgon", "Turin",
            "Uinen", "Ulmo", "Ungoliant", "Vaire", "Vana", "Varda", "Yavanna" };

    public BotDomino() {
        super(names[(int) (Math.random() * names.length)]);
    }

    public void changeName() {
        name = names[(int) (Math.random() * names.length)];
    }

    public void play(GameDomino model)
            throws TileNotFoundException, UnableToTurnException, NoPossibleMovementsException {

        List<Pair<Integer, Integer>> possibleMoves = model.findPossiblePlacements();

        TileDomino tileToPlace = model.getTileToPlace().copy();

        Pair<Integer, Integer> position = new Pair<>(-5, -5);
        int points = 0;
        int nbTurns = 0;

        if (possibleMoves.isEmpty())
            throw new NoPossibleMovementsException();

        for (var move : possibleMoves) {
            for (int i = 0; i < 4; i++) {
                int pointsToPlace = model.pointsIfPlaced(move.first, move.second, tileToPlace);

                if (pointsToPlace > points) {
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