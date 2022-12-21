package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import interfaces.Placeable.Direction;

/**
 * This class represents a generic expandable 2D array.
 */
public class Expandable2DArray<T> {

    private ArrayList<ArrayList<T>> array; // The array

    // Constructors

    public Expandable2DArray() {
        array = new ArrayList<>();
    }

    public Expandable2DArray(T firstElement) {
        array = new ArrayList<>();
        array.add(new ArrayList<>());
        array.get(0).add(firstElement);
    }

    // Getters
    public int getHeight() {
        return array.get(0).size();
    }

    public int getWidth() {
        return array.size();
    }

    public T get(int x, int y) {
        if (x < 0 || y < 0 || x > array.size() || y > array.get(0).size())
            throw new IndexOutOfBoundsException();

        return array.get(x).get(y);
    }

    public T get(Pair<Integer, Integer> pair) {
        return get(pair.first, pair.second);
    }

    /**
     * Returns the immediate non-null neighbors of the element at the given
     * position.
     * 
     * @param x The x coordinate of the element.
     * @param y The y coordinate of the element.
     * @return The immediate non-null neighbors of the element at the given
     */
    public List<Pair<T, Direction>> getNeighbors(int x, int y) {
        List<Pair<T, Direction>> neighbors = new ArrayList<>();

        if (!isInsideExpandableBounds(x, y))
            throw new IndexOutOfBoundsException();

        if (!isOutOfBounds(x - 1, y) && get(x - 1, y) != null) {
            neighbors.add(new Pair<>(get(x - 1, y), Direction.UP));
        }

        if (!isOutOfBounds(x + 1, y) && get(x + 1, y) != null) {
            neighbors.add(new Pair<>(get(x + 1, y), Direction.DOWN));
        }

        if (!isOutOfBounds(x, y - 1) && get(x, y - 1) != null) {
            neighbors.add(new Pair<>(get(x, y - 1), Direction.LEFT));
        }

        if (!isOutOfBounds(x, y + 1) && get(x, y + 1) != null) {
            neighbors.add(new Pair<>(get(x, y + 1), Direction.RIGHT));
        }

        return neighbors;
    }

    // Setter
    public void set(int x, int y, T value) {
        if (x < 0 || y < 0 || x > array.size() || y > array.get(0).size())
            throw new IndexOutOfBoundsException();

        array.get(x).set(y, value);
    }

    // Methods
    /**
     * Expands the size of the array in the given direction.
     * 
     * @param direction The direction in which the array should be expanded.
     */
    private void expand(Direction direction) {
        switch (direction) {
            case UP:
                ArrayList<T> newLine = new ArrayList<>();
                array.get(0).forEach(l -> newLine.add(null));
                array.add(0, newLine);
                break;

            case DOWN:
                newLine = new ArrayList<>();
                array.get(0).forEach(l -> newLine.add(null));
                array.add(newLine);
                break;

            case LEFT:
                array.forEach(l -> l.add(0, null));
                break;

            case RIGHT:
                array.forEach(l -> l.add(null));
                break;
        }
    }

    /**
     * Adds a new element to the array.
     * 
     * @param x     The x coordinate of the new element.
     * @param y     The y coordinate of the new element.
     * @param value The value of the new element.
     */
    public void add(int x, int y, T value) {

        if (array.isEmpty()) {
            array.add(new ArrayList<>());
            array.get(0).add(value);
            return;
        }

        if (x < -1 || y < -1 || x > array.size() || y > array.get(0).size())
            throw new IndexOutOfBoundsException();

        if (x == array.size()) {
            expand(Direction.DOWN);
        }

        if (y == array.get(0).size()) {
            expand(Direction.RIGHT);
        }

        if (x == -1) {
            expand(Direction.UP);
            x = 0;
        }

        if (y == -1) {
            expand(Direction.LEFT);
            y = 0;
        }

        if (array.get(x).get(y) != null)
            throw new IllegalArgumentException("The cell is already occupied");

        array.get(x).set(y, value);
    }

    /**
     * Returns a sub-array of this array. This sub-array is centered on the point
     * {@code x,y} and is a rectangle of size {@code size}. If the size is bigger
     * than the array it will return the maximum possible sub-array.
     * 
     * @param x    X coordinate of the center
     * @param y    Y coordinate of the center
     * @param size The size of the sub-array. Must be odd and positive in order to
     *             have a center.
     * @return The sub-array
     */
    public List<List<T>> getSubArray(int x, int y, int size) {

        if (x < 0 || y < 0 || x > array.size() || y > array.get(0).size())
            throw new IndexOutOfBoundsException();
        if (size < 0)
            throw new IllegalArgumentException("Size must be positive");
        if (size % 2 == 0)
            throw new IllegalArgumentException("Size must be odd");

        int xStart = x - size / 2;
        int yStart = y - size / 2;

        if (xStart < 0) {
            xStart = 0;
        }

        if (yStart < 0) {
            yStart = 0;
        }

        int xEnd = x + size / 2;
        int yEnd = y + size / 2;

        if (xEnd > array.size()) {
            xEnd = array.size();
        }

        if (yEnd > array.get(0).size()) {
            yEnd = array.get(0).size();
        }

        List<List<T>> subArray = new ArrayList<>();

        for (int i = xStart; i < xEnd; i++) {
            subArray.add(new ArrayList<>());
            for (int j = yStart; j < yEnd; j++) {
                subArray.get(i - xStart).add(array.get(i).get(j));
            }
        }

        return subArray;
    }

    // Useful for testing purposes (maybe in the future ...)

    /**
     * Applies a function to a sub-array, centered on the point {@code x,y} and is a
     * rectangle of size {@code size} {@link #getSubArray(int, int, int)}.
     * 
     * @param x    X coordinate of the center
     * @param y    Y coordinate of the center
     * @param size The size of the sub-array
     * @param func The function to apply
     */
    public void applyFunctionSubArray(int x, int y, int size, Consumer<T> func) {
        getSubArray(x, y, size).forEach(l -> l.forEach(func));
    }

    /**
     * Applies {@code func} to all the elements of the array.
     * 
     * @param func The function to apply
     */
    public void forEach(Consumer<T> func) {
        array.forEach(l -> l.forEach(func));
    }

    /**
     * Finds the first element of the array that satisfies the predicate
     * {@code pred}.
     * 
     * @param predicate The predicate to test
     * @return The first element that satisfies the predicate
     */
    public T find(Predicate<T> predicate) {
        for (List<T> l : array) {
            for (T t : l) {
                if (predicate.test(t))
                    return t;
            }
        }
        return null;
    }

    /**
     * Finds the first element of the array that satisfies the predicate. It returns
     * its index.
     * 
     * @param predicate The predicate to test
     * @return The index of the first element that satisfies the predicate
     */
    public Pair<Integer, Integer> findIndex(Predicate<T> predicate) {
        for (int i = 0; i < array.size(); i++) {
            for (int j = 0; j < array.get(0).size(); j++) {
                if (predicate.test(array.get(i).get(j)))
                    return new Pair<>(i, j);
            }
        }
        return null;
    }

    /**
     * Returns true if the index is out of bounds.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @return True if the index is out of bounds
     */
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= array.size() || y >= array.get(0).size();
    }

    /**
     * Returns true if the index is out of bounds.
     * 
     * @param index The coordinates
     * @return True if the index is out of bounds
     */
    public boolean isOutOfBounds(Pair<Integer, Integer> index) {
        return isOutOfBounds(index.first, index.second);
    }

    /**
     * Returns {@code true} if the index is inside of the array or it could be
     * inside if the array was expanded.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @return True if the index is inside of the array or it could be inside if the
     *         array was expanded
     */
    public boolean isInsideExpandableBounds(int x, int y) {
        return x >= -1 && y >= -1 && x <= array.size() && y <= array.get(0).size();
    }

    /**
     * Returns {@code true} if the {@code index} is inside of the array or it could
     * be inside if the array was expanded.
     * 
     * @param index The index
     * @return True if the index is inside of the array or it could be inside if the
     *         array was expanded
     */
    public boolean isInsideExpandableBounds(Pair<Integer, Integer> index) {
        return isInsideExpandableBounds(index.first, index.second);
    }

    /**
     * Iterates over the array and applies the action to each index.
     * 
     * @param action The action to apply
     */
    public void iteri(Consumer<Pair<Integer, Integer>> action) {
        for (int i = 0; i < array.size(); i++) {
            for (int j = 0; j < array.get(i).size(); j++) {
                action.accept(new Pair<>(i, j));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        array.forEach(line -> {
            line.forEach(element -> {
                sb.append(element == null ? "null" : element.toString());
                sb.append(" ");
            });
            sb.append("\n");
        });

        return sb.toString();
    }
}
