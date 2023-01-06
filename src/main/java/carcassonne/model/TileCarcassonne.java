package carcassonne.model;

import java.util.ArrayList;
import java.util.List;

import interfaces.Placeable;
import carcassonne.model.SideCarcassonne.Type;
import exceptions.UnableToTurnException;
import shared.model.Tile;
import utils.Pair;

public class TileCarcassonne extends Tile<SideCarcassonne> {

    public enum SideSelector {
        NORTH, EAST, SOUTH, WEST, CENTER
    }

    private int nbOfRotations = 0;

    private boolean hasAbbey;

    private PlayerCarcassonne player;

    // This list contains the possible positions of the pawns on the tile. If the
    // tile has an abbey, the last element of the list is the abbey. The rest of the
    // elements are in the following order: NESW(Abbey). If the side has a path, the
    // element of the list is an array of 3 booleans. If the side has a city or a
    // meadow, the element of the list is an array of 1 boolean. The boolean is true
    // if a pawn is placed on that spot and only one pawn per Tile can be placed .
    private List<boolean[]> possiblePawnPositions;

    public TileCarcassonne(int tileId) {
        id = tileId;
        initSidesWithIntegerId(tileId);
        initPossiblePawnPositions();
    }

    private TileCarcassonne(SideCarcassonne[] tab, PlayerCarcassonne player, boolean hasAbbey) {
        this.player = player;
        this.hasAbbey = hasAbbey;

        if (validSides(tab)) {
            sides = tab;
        } else {
            throw new IllegalArgumentException("The array is not valid");
        }
        initPossiblePawnPositions();
    }

    public int getNbOfRotations() {
        return nbOfRotations;
    }

    public PlayerCarcassonne getPlayer() {
        return player;
    }

    public boolean hasAbbey() {
        return hasAbbey;
    }

    public Pair<SideSelector, Integer> getPawnPosition() {
        for (int i = 0; i < sides.length; i++) {
            boolean[] side = possiblePawnPositions.get(i);
            for (int j = 0; j < side.length; j++) {
                if (side[j])
                    return new Pair<>(SideSelector.values()[i], j);
            }
        }
        if (possiblePawnPositions.size() == sides.length + 1 && possiblePawnPositions.get(sides.length)[0])
            return new Pair<>(SideSelector.CENTER, 0);

        return null;
    }

    public Pair<SideSelector, Integer> getRandomPlacingPosition() {
        if (isPawnPlaced()) {
            throw new IllegalStateException("Pawn is already placed");
        }

        int side = (int) (Math.random() * possiblePawnPositions.size());

        int position;

        if (possiblePawnPositions.get(side).length == 0) {
            position = 0;
        } else {
            position = (int) (Math.random() * possiblePawnPositions.get(side).length);
        }

        return new Pair<>(SideSelector.values()[side], position);
    }

    public void setPlayer(PlayerCarcassonne player) {
        this.player = player;
    }

    private void initPossiblePawnPositions() {
        possiblePawnPositions = new ArrayList<>();
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

    public boolean isPawnPlaced() {
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
        if (isPawnPlaced())
            throw new IllegalStateException("A pawn is already placed on this tile");

        if (side == SideSelector.CENTER) {
            handleAbbeyPlacement(side);
        } else {
            handleSidePlacement(side, position);
        }
    }

    private void handleAbbeyPlacement(SideSelector side) {
        if (!hasAbbey)
            throw new IllegalStateException("The tile does not have an abbey");

        possiblePawnPositions.get(sideSelectorToInt(side))[0] = true;
        player.decreaseRemainingPawns();
    }

    private void handleSidePlacement(SideSelector side, int position) {
        if (position != 0 && getSide(sideSelectorToDirection(side)).getType() != Type.PATH) {
            position = 0;
        }

        if (position < 0 || position >= possiblePawnPositions.get(sideSelectorToInt(side)).length)
            throw new IllegalArgumentException("Position must be between 0 and 2");

        possiblePawnPositions.get(sideSelectorToInt(side))[position] = true;
        player.decreaseRemainingPawns();

    }

    @Override
    public boolean doesSideMatch(SideCarcassonne side, Direction direction) {
        return sides[Placeable.directionToInt(direction)].hasSameType(side);
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
    public TileCarcassonne copy() {
        return new TileCarcassonne(
                new SideCarcassonne[] { sides[0].copy(), sides[1].copy(), sides[2].copy(), sides[3].copy() },
                player, hasAbbey);
    }

    @Override
    public void turnRight(int n) throws UnableToTurnException {
        if (isPlaced)
            throw new UnableToTurnException();

        n = n % sides.length;

        for (int i = 0; i < n; i++) {
            SideCarcassonne last = sides[sides.length - 1];
            boolean[] lastPawnPosition = possiblePawnPositions.get(sides.length - 1);

            for (int j = sides.length - 1; j > 0; j--) {
                sides[j] = sides[j - 1];
                possiblePawnPositions.set(j, possiblePawnPositions.get(j - 1));
            }

            sides[0] = last;
            possiblePawnPositions.set(0, lastPawnPosition);
        }

        nbOfRotations += n;
        nbOfRotations = (nbOfRotations + sides.length) % sides.length;
    }

    @Override
    public void turnLeft(int n) throws UnableToTurnException {
        if (isPlaced) {
            throw new UnableToTurnException();
        }

        n = n % sides.length;

        for (int i = 0; i < n; i++) {
            int j;
            SideCarcassonne first = sides[0];
            boolean[] firstPawnPosition = possiblePawnPositions.get(0);

            for (j = 0; j < sides.length - 1; j++) {
                sides[j] = sides[j + 1];
                possiblePawnPositions.set(j, possiblePawnPositions.get(j + 1));
            }

            sides[j] = first;
            possiblePawnPositions.set(j, firstPawnPosition);
        }
        nbOfRotations -= n;
        nbOfRotations = (nbOfRotations + sides.length) % sides.length;
    }

    public static int sideSelectorToInt(SideSelector side) {
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

    public static SideSelector intToSideSelector(int side) {
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

    public static Direction sideSelectorToDirection(SideSelector side) {
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

    public static SideSelector directionToSideSelector(Direction direction) {
        switch (direction) {
            case UP:
                return SideSelector.NORTH;
            case RIGHT:
                return SideSelector.EAST;
            case DOWN:
                return SideSelector.SOUTH;
            case LEFT:
                return SideSelector.WEST;
            default:
                throw new IllegalArgumentException("Direction must be UP, RIGHT, DOWN or LEFT");
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

    public String toString() {
        String s = "";
        for (int i = 0; i < 4; i++) {
            s += (sides[i]).toString();
        }
        return s;
    }
}
