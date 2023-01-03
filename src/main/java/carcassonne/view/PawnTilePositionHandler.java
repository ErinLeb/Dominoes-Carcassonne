package carcassonne.view;

import carcassonne.model.TileCarcassonne.SideSelector;
import utilities.Pair;
import utilities.geometry.Point;

/**
 * This class is used to compute the position of the pawn on the tile.
 */
public class PawnTilePositionHandler {
    private int width;
    private int height;

    private Pair<SideSelector, Integer> pawnLocation;
    private boolean isPath;
    private Point coordinatesPawn;

    public PawnTilePositionHandler(int width, int height, Pair<SideSelector, Integer> pawnLocation, boolean isPath) {
        this.width = width == 0 ? 125 : width;
        this.height = height == 0 ? 125 : height;
        this.isPath = isPath;
        this.pawnLocation = pawnLocation;
    }

    public Point pawnPositionToPoint() {

        if (pawnLocation == null)
            throw new IllegalArgumentException("The pawn position is null");

        switch (pawnLocation.first) {
            case NORTH:
                coordinatesPawn = new Point(width / 2, height / 6);
                break;
            case EAST:
                coordinatesPawn = new Point(5 * width / 6, height / 2);
                break;
            case SOUTH:
                coordinatesPawn = new Point(width / 2, 5 * height / 6);
                break;
            case WEST:
                coordinatesPawn = new Point(width / 6, height / 2);
                break;
            case CENTER:
                return new Point(width / 2, height / 2);
            default:
                coordinatesPawn = new Point(0, 0);
        }

        if (isPath && pawnLocation.second != 1) {
            computePawnPathPosition();
        }

        return coordinatesPawn;
    }

    private void computePawnPathPosition() {

        int pathLength = width / 3;

        int deltaMouvement = (int) (pathLength * 0.7);

        switch (pawnLocation.first) {
            case NORTH:
                if (pawnLocation.second == 0) {

                    coordinatesPawn.x -= deltaMouvement;
                } else {
                    coordinatesPawn.x += deltaMouvement;
                }
                break;

            case EAST:
                if (pawnLocation.second == 0) {

                    coordinatesPawn.y -= deltaMouvement;
                } else {
                    coordinatesPawn.y += deltaMouvement;
                }
                break;

            case SOUTH:
                if (pawnLocation.second == 0) {

                    coordinatesPawn.x += deltaMouvement;
                } else {
                    coordinatesPawn.x -= deltaMouvement;
                }
                break;

            case WEST:
                if (pawnLocation.second == 0) {

                    coordinatesPawn.y += deltaMouvement;
                } else {
                    coordinatesPawn.y -= deltaMouvement;
                }
                break;

            default:
        }
    }

}
