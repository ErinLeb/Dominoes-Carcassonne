package utils.geometry;

/**
 * Interface for closed shapes (shapes that can be closed by a line).
 */
public interface ClosedShape {

    /**
     * Checks if the given point is inside the shape.
     * 
     * @param point
     *              the point to check.
     * @return {@code true} if the point is inside the shape, {@code false}
     *         otherwise.
     */
    boolean isInside(Point point);

}
