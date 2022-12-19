package interfaces;

import java.util.List;

import elements.Side;
import utilities.Pair;

/**
 * This interface represents a placeable object. The object has a side of the
 * type {@code T}.
 */
public interface Placeable<T extends Side> {
    /**
     * Represents the direction of the side on the tile
     */
    enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    /**
     * Returns the side of the object in the direction {@code direction}.
     * 
     * @param direction the direction of the side to get
     * @return the side of the object in the direction {@code direction}
     */
    public T getSide(Direction direction);

    /**
     * Sets the side of the object in the direction {@code direction} to the side.
     * 
     * @param side      the side to set
     * @param direction the direction of the side to set
     */
    public void setSide(T side, Direction direction);

    /**
     * Returns the opposite direction of the direction {@code direction}.
     * 
     * @param direction the direction to get the opposite
     * @return the opposite direction of the direction {@code direction}
     */
    public static Direction getOpposite(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case RIGHT:
                return Direction.LEFT;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            default:
                return null;
        }
    }

    /**
     * Converts a {@code Direction} to an {@code int}.
     * <p>
     * The conversion is as follows:
     * <ul>
     * <li>{@code Direction.UP} is converted to {@code 0}</li>
     * <li>{@code Direction.RIGHT} is converted to {@code 1}</li>
     * <li>{@code Direction.DOWN} is converted to {@code 2}</li>
     * <li>{@code Direction.LEFT} is converted to {@code 3}</li>
     * </ul>
     * </p>
     * 
     * @param direction {@code Direction to convert}
     * @return the converted {@code int}
     */
    public default int directionToInt(Direction direction) {
        switch (direction) {
            case UP:
                return 0;
            case RIGHT:
                return 1;
            case DOWN:
                return 2;
            case LEFT:
                return 3;
            default:
                return -1;
        }
    }

    /**
     * Converts a {@code int} to a {@code Direction}.
     * <p>
     * The conversion is as follows:
     * 
     * <ul>
     * <li>{@code 0} is converted to {@code Direction.UP}</li>
     * <li>{@code 1} is converted to {@code Direction.RIGHT}</li>
     * <li>{@code 2} is converted to {@code Direction.DOWN}</li>
     * <li>{@code 3} is converted to {@code Direction.LEFT}</li>
     * </ul>
     * </p>
     * 
     * @param direction {@code Direction} to convert
     * @return the converted {@code int}
     */
    public default Direction intToDirection(int i) {
        switch (i) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.DOWN;
            case 3:
                return Direction.LEFT;
            default:
                return null;
        }
    }

    /**
     * Converts a {@code String} to a {@code Direction}.
     * <p>
     * The conversion is as follows:
     * 
     * <ul>
     * <li>{@code "UP", "U"} is converted to {@code Direction.UP}</li>
     * <li>{@code "RIGHT", "R"} is converted to {@code Direction.RIGHT}</li>
     * <li>{@code "DOWN", "D"} is converted to {@code Direction.DOWN}</li>
     * <li>{@code "LEFT", "L"} is converted to {@code Direction.LEFT}</li>
     * </ul>
     * </p>
     * 
     * @param direction {@code Direction} to convert
     * @return the converted {@code int}
     */
    public static Direction stringToDirection(String s) {
        switch (s.toUpperCase()) {
            case "UP":
            case "U":
                return Direction.UP;

            case "RIGHT":
            case "R":
                return Direction.RIGHT;

            case "DOWN":
            case "D":
                return Direction.DOWN;

            case "LEFT":
            case "L":
                return Direction.LEFT;

            default:
                return null;
        }
    }

    /**
     * Change the coordinates of one towards the {@code direction}. E.g : if we have
     * {@code coordinates} = {1,2} and {@code direction} = Down, it changes
     * coordinates to {1,3}
     * 
     * @param coordinates the coordinates of the point to update
     * @param direction   the direction towards we move
     */
    public static void updateCoordinatesFromDirection(Pair<Integer, Integer> coordinates, Direction direction) {
        switch (direction) {
            case UP:
                coordinates.first = coordinates.first - 1;
                break;
            case RIGHT:
                coordinates.second = coordinates.second + 1;
                break;
            case DOWN:

                coordinates.first = coordinates.first + 1;
                break;
            case LEFT:
                coordinates.second = coordinates.second - 1;
                break;
            default:
                break;
        }
    }

    /**
     * Returns true if the side in the direction {@code direction} has the same
     * figures as {@code side}.
     * 
     * @param direction The direction of the comparison
     * @param side      Side to compare
     * @return
     */
    public boolean doesSideMatch(T side, Direction direction);

    /**
     * Returns true if the tile can be placed next to each tile of {@code tiles}.
     * 
     * @param tiles The tiles to check, with their respective directions
     * @return True if the tile can be placed
     */
    public default boolean canBePlaced(List<Pair<Placeable<T>, Direction>> tiles) {
        for (Pair<Placeable<T>, Direction> tile : tiles)
            if (!doesSideMatch(tile.first.getSide(getOpposite(tile.second)), tile.second))
                return false;

        return true;
    }
}
