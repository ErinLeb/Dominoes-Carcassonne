package carcassonne.model;

import java.util.List;

import carcassonne.model.SideCarcassonne.Type;
import shared.model.Tile;

public class TileCarcassonne extends Tile<SideCarcassonne> {

    enum SideSelector {
        NORTH, EAST, SOUTH, WEST, CENTER
    }

    private int tileId;

    private boolean hasAbbey;

    // This list contains the possible positions of the pawns on the tile. If the
    // tile has an abbey, the last element of the list is the abbey. The rest of the
    // elements are in the following order: NESW(Abbey). If the side has a path, the
    // element of the list is an array of 3 booleans. If the side has a city or a
    // meadow, the element of the list is an array of 1 boolean. The boolean is true
    // if a pawn is placed on that spot and only one pawn per Tile can be placed .
    private List<boolean[]> possiblePawnPositions;

    public TileCarcassonne(int tileId) {
        initSidesWithIntegerId(tileId);
        initPossiblePawnPositions();
    }

    private TileCarcassonne(SideCarcassonne[] tab) {
        if (validSides(tab)) {
            sides = tab;
        } else {
            throw new IllegalArgumentException("The array is not valid");
        }
        initPossiblePawnPositions();
    }

    private void initPossiblePawnPositions() {
        for (SideCarcassonne side : sides) {
            if (side.getType() == SideCarcassonne.Type.PATH) {
                possiblePawnPositions.add(new boolean[] { false, false, false });
            } else {
                possiblePawnPositions.add(new boolean[] { false });
            }
        }

        if (hasAbbey) {
            possiblePawnPositions.add(new boolean[] { false });
        }
    }

    private boolean isPawnPlaced() {
        for (boolean[] tab : possiblePawnPositions) {
            for (boolean b : tab) {
                if (b) {
                    return true;
                }
            }
        }
        return false;
    }

    public void placePawn(SideSelector side) {
        placePawn(side, 0);
    }

    public void placePawn(SideSelector side, int position) {
        // TODO: refactor this method
        if (isPawnPlaced())
            throw new IllegalStateException("A pawn is already placed on this tile");

        if (side == SideSelector.CENTER) {
            if (!hasAbbey)
                throw new IllegalStateException("The tile does not have an abbey");

            possiblePawnPositions.get(sideSelectorToInt(side))[4] = true;
            return;
        }

        if (position != 0 && getSide(sideSelectorToDirection(side)).getType() != Type.PATH) {
            position = 0;
        }

        if (position < 0 || position >= possiblePawnPositions.get(sideSelectorToInt(side)).length)
            throw new IllegalArgumentException("Position must be between 0 and 2");

        possiblePawnPositions.get(sideSelectorToInt(side))[position] = true;
    }

    @Override
    public boolean doesSideMatch(SideCarcassonne side, Direction direction) {
        return sides[directionToInt(direction)].hasSameType(side);
    }

    @Override
    public boolean validSides(SideCarcassonne[] tab) {
        if (tab.length != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (tab[i] == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Tile<SideCarcassonne> copy() {
        return new TileCarcassonne(
                new SideCarcassonne[] { sides[0].copy(), sides[1].copy(), sides[2].copy(), sides[3].copy() });
    }

    public int sideSelectorToInt(SideSelector side) {
        switch (side) {
            case NORTH:
                return 0;
            case EAST:
                return 1;
            case SOUTH:
                return 2;
            case WEST:
                return 3;
            case CENTER:
                return 4;
            default:
                throw new IllegalArgumentException("SideSelector must be NORTH, EAST, SOUTH, WEST or CENTER");
        }
    }

    public SideSelector intToSideSelector(int side) {
        switch (side) {
            case 0:
                return SideSelector.NORTH;
            case 1:
                return SideSelector.EAST;
            case 2:
                return SideSelector.SOUTH;
            case 3:
                return SideSelector.WEST;
            case 4:
                return SideSelector.CENTER;
            default:
                throw new IllegalArgumentException("The int must be between 0 and 4");
        }
    }

    public Direction sideSelectorToDirection(SideSelector side) {
        switch (side) {
            case NORTH:
                return Direction.UP;
            case EAST:
                return Direction.RIGHT;
            case SOUTH:
                return Direction.DOWN;
            case WEST:
                return Direction.LEFT;
            default:
                throw new IllegalArgumentException("SideSelector must be NORTH, EAST, SOUTH or WEST");
        }
    }

    public void initSidesWithIntegerId(int tileId) {
        switch (tileId) {
            case 0:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.MEADOW),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.PATH) };
                break;
            case 1:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.PATH),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.MEADOW) };
                break;
            case 2:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.PATH),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.CITY) };
                break;
            case 3:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.CITY) };
                break;
            case 4:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY) };
                break;
            case 5:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.PATH),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.CITY) };
                break;
            case 6:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.PATH),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.PATH) };
                break;
            case 7:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.MEADOW),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.MEADOW) };
                break;
            case 8:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.PATH),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.PATH) };
                break;
            case 9:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.MEADOW),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.MEADOW) };
                break;
            case 10:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.MEADOW) };
                break;

            case 11:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY) };
                break;
            case 12:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.MEADOW),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.MEADOW) };
                hasAbbey = true;
                break;
            case 13:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.MEADOW),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.MEADOW) };
                hasAbbey = true;
                break;
            case 14:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.MEADOW),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY) };
                break;
            case 15:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY) };
                break;
            case 16:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.PATH),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.PATH) };
                break;
            case 17:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.MEADOW),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.PATH) };
                break;
            case 18:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY) };
                break;
            case 19:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.CITY) };
                break;
            case 20:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.CITY) };
                break;
            case 21:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.PATH),
                        new SideCarcassonne(Type.PATH), new SideCarcassonne(Type.PATH) };
                break;
            case 22:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.CITY), new SideCarcassonne(Type.MEADOW),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY) };
                break;
            case 23:
                sides = new SideCarcassonne[] { new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY),
                        new SideCarcassonne(Type.MEADOW), new SideCarcassonne(Type.CITY) };
                break;
            default:
        }
    }
}
