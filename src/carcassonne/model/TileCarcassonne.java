package carcassonne.model;

import shared.model.Tile;

public class TileCarcassonne extends Tile<SideCarcassonne> {

    public TileCarcassonne(SideCarcassonne[] tab) {
        // TODO
    }

    @Override
    public boolean doesSideMatch(SideCarcassonne side, Direction direction) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean validSides(SideCarcassonne[] tab) {
        // TODO Auto-generated method stub

        return false;
    }

    @Override
    public Tile<SideCarcassonne> copy() {
        // TODO Auto-generated method stub
        return new TileCarcassonne(this.sides);
    }

}
