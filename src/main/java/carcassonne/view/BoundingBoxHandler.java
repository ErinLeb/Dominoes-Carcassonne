package carcassonne.view;

import interfaces.Placeable.Direction;
import utils.geometry.Point;
import utils.geometry.Triangle;

/**
 * The BoundingBoxHandler class is used to generate the bounding boxes of the
 * tiles.
 */
public class BoundingBoxHandler {

    private int width;
    private int height;
    private int pathLength;
    private Direction direction;

    public BoundingBoxHandler(int width, int height, int pathLength, Direction direction) {
        this.width = width;
        this.height = height;
        this.pathLength = pathLength;
        this.direction = direction;
    }

    /**
     * Generates the bounding box of the side.
     * 
     * @return The bounding box of the side
     */
    public Triangle sideBoundingBoxFromDirection() {
        switch (direction) {
            case UP:
                return new Triangle(new Point(0, 0), new Point(width / 2, height / 2),
                        new Point(width, 0));
            case RIGHT:
                return new Triangle(new Point(width, 0), new Point(width / 2, height / 2),
                        new Point(width, height));
            case DOWN:
                return new Triangle(new Point(0, height), new Point(width / 2, height / 2),
                        new Point(width, height));
            case LEFT:
                return new Triangle(new Point(0, 0), new Point(width / 2, height / 2),
                        new Point(0, height));
            default:
                return new Triangle(new Point(0, 0), new Point(0, 0), new Point(0, 0));
        }
    }

    /**
     * Generates the bounding box of the left part of the path (LEFT of the path
     * looking from the center of the tile)
     * 
     * @return The bounding box of the right part of the path
     */
    public Triangle leftBoundingBoxFromDirection() {
        switch (direction) {
            case UP:
                return new Triangle(new Point(0, 0), new Point(pathLength, 0), new Point(pathLength, pathLength));
            case RIGHT:
                return new Triangle(new Point(width, 0), new Point(width, pathLength),
                        new Point(2 * pathLength, pathLength));
            case DOWN:
                return new Triangle(new Point(width, height), new Point(2 * pathLength, 2 * pathLength),
                        new Point(2 * pathLength, height));
            case LEFT:
                return new Triangle(new Point(0, height), new Point(0, 2 * pathLength),
                        new Point(pathLength, 2 * pathLength));
            default:
                return new Triangle(new Point(0, 0), new Point(0, 0), new Point(0, 0));
        }
    }

    /**
     * Generates the bounding box of the right part of the path (right of the path
     * looking from the center of the tile)
     * 
     * @return The bounding box of the right part of the path
     */
    public Triangle rightBoundingBoxFromDirection() {
        switch (direction) {
            case UP:
                return new Triangle(new Point(2 * pathLength, 0), new Point(width, 0),
                        new Point(2 * pathLength, pathLength));
            case RIGHT:
                return new Triangle(new Point(width, 2 * pathLength), new Point(width, height),
                        new Point(2 * pathLength, 2 * pathLength));
            case DOWN:
                return new Triangle(new Point(pathLength, 2 * pathLength), new Point(pathLength, height),
                        new Point(0, height));
            case LEFT:
                return new Triangle(new Point(0, 0), new Point(0, pathLength), new Point(pathLength, pathLength));
            default:
                return new Triangle(new Point(0, 0), new Point(0, 0), new Point(0, 0));
        }
    }

}
