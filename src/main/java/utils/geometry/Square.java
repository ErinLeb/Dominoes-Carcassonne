package utils.geometry;

/**
 * Class for squares.
 */
public class Square implements ClosedShape {

    private Point topCorner;
    private Point bottomCorner;

    public Square(Point topCorner, Point bottomCorner) {
        this.topCorner = topCorner;
        this.bottomCorner = bottomCorner;
    }

    @Override
    public boolean isInside(Point point) {
        return point.x > topCorner.x
                && point.x < bottomCorner.x
                && point.y > topCorner.y
                && point.y < bottomCorner.y;
    }
}
