package utilities.geometry;

/**
 * Class for triangles in a 2D plane.
 */
public class Triangle implements ClosedShape {

    private Point point1;
    private Point point2;
    private Point point3;

    public Triangle(Point point1, Point point2, Point point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    /**
     * Calculates the area of the triangle formed by the three given points.
     * 
     * @param firstPoint  the first point.
     * @param secondPoint the second point.
     * @param thirdPoint  the third point.
     * 
     * @return the area of the triangle.
     */
    private double area(Point firstPoint, Point secondPoint, Point thirdPoint) {
        return Math.abs((firstPoint.x * (secondPoint.y - thirdPoint.y)
                + secondPoint.x * (thirdPoint.y - firstPoint.y) +
                thirdPoint.x * (firstPoint.y - secondPoint.y)) / 2.0);
    }

    @Override
    public boolean isInside(Point point) {

        // To check if a point is inside a triangle, we can check if the sum of the
        // areas of the three triangles formed by the point and the three vertices of
        // the triangle is equal to the area of the triangle.

        // Calculate area of the triangle
        double area = area(point1, point2, point3);

        // Areas of the three triangles formed by the point and the three vertices of
        // the triangle
        double area1 = area(point, point2, point3);
        double area2 = area(point1, point, point3);
        double area3 = area(point1, point2, point);

        // Check if the sum of the areas is equal to the area of the triangle
        return (area == area1 + area2 + area3);
    }
}
