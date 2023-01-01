package carcassonne.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import carcassonne.model.SideCarcassonne.Type;
import carcassonne.model.TileCarcassonne;
import carcassonne.model.TileCarcassonne.SideSelector;
import interfaces.Placeable.Direction;
import shared.view.TilePanel;
import utilities.geometry.Point;
import utilities.geometry.Square;
import utilities.geometry.Triangle;

public class TileCarcassonnePanel extends TilePanel<TileCarcassonne> {

    private TileCarcassonne tileModel;

    private Color pawnColor;

    private int pathLength;

    private boolean canPawnBePlaced;

    public TileCarcassonnePanel(TileCarcassonne tileModel, Color pawnColor) {
        this.tileModel = tileModel;
        this.pawnColor = pawnColor;

        init();
    }

    public void setCanPawnBePlaced(boolean canPawnBePlaced) {
        this.canPawnBePlaced = canPawnBePlaced;
    }

    private void init() {
        setPreferredSize(new Dimension(125, 125));
        setLayout(null);

        addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        placePawn(e.getX(), e.getY());
                    }
                });
    }

    private void placePawn(int x, int y) {
        pathLength = getWidth() / 3;
        Point click = new Point(x, y);

        if (!canPawnBePlaced || tileModel.isPawnPlaced() || isOutOfBounds(click))
            return;

        if (tileModel.hasAbbey()) {
            handleAbbey(click);
            if (tileModel.isPawnPlaced())
                return;
        }
        for (Direction direction : Direction.values()) {
            handleSide(direction, click);
            if (tileModel.isPawnPlaced())
                return;
        }
    }

    private boolean isOutOfBounds(Point click) {
        return click.x < 0 || click.y < 0 || click.x > this.getWidth() || click.y > this.getHeight();
    }

    private void handleAbbey(Point click) {
        int abbeySize = computeAbbeySize();

        Square abbeyBoundingBox = new Square(new Point(abbeySize / 2, abbeySize / 2),
                new Point((3 * abbeySize) / 2, (3 * abbeySize) / 2));

        if (abbeyBoundingBox.isInside(click)) {
            tileModel.placePawn(SideSelector.CENTER, 0);
            drawPawn(click);
        }
    }

    private int computeAbbeySize() {
        return (int) (getWidth() * 0.5);
    }

    private void handleSide(Direction direction, Point click) {
        SideSelector selectedSide = TileCarcassonne.directionToSideSelector(direction);

        // A bounding box is an area that represents the zone we want to check. We have
        // to check if the click is inside the bounding box. If it is, then we can place
        // a pawn on that zone.

        // Generates the bounding box of the side
        Triangle sideBoundingBox = sideBoundingBoxFromDirection(direction);

        if (!sideBoundingBox.isInside(click))
            return;

        // Generates the bounding box of the left part of the path (left of the path
        // looking from the center of the tile)
        Triangle lefBoundingBox = leftBoundingBoxFromDirection(direction);

        if (tileModel.getSide(direction).getType() != Type.PATH || lefBoundingBox.isInside(click)) {
            tileModel.placePawn(selectedSide, 0);
            drawPawn(click);
            return;
        }

        // Generates the bounding box of the right part of the path (right of the path
        // looking from the center of the tile)
        Triangle rightBoundingBox = rightBoundingBoxFromDirection(direction);

        if (rightBoundingBox.isInside(click)) {
            tileModel.placePawn(selectedSide, 2);
            drawPawn(click);
            return;
        }

        // The tile is then in the path
        tileModel.placePawn(selectedSide, 1);
        drawPawn(click);
    }

    private void drawPawn(Point center) {
        JPanel pawn = new JPanel();
        pawn.setBackground(pawnColor);
        pawn.setBounds(center.x - 5, center.y - 5, 10, 10);
        add(pawn);
        revalidate();
        repaint();
    }

    /**
     * Generates the bounding box of the side.
     * 
     * @param direction The direction of the side
     * @return The bounding box of the side
     */
    private Triangle sideBoundingBoxFromDirection(Direction direction) {
        switch (direction) {
            case UP:
                return new Triangle(new Point(0, 0), new Point(getWidth() / 2, getHeight() / 2),
                        new Point(getWidth(), 0));
            case RIGHT:
                return new Triangle(new Point(getWidth(), 0), new Point(getWidth() / 2, getHeight() / 2),
                        new Point(getWidth(), getHeight()));
            case DOWN:
                return new Triangle(new Point(0, getHeight()), new Point(getWidth() / 2, getHeight() / 2),
                        new Point(getWidth(), getHeight()));
            case LEFT:
                return new Triangle(new Point(0, 0), new Point(getWidth() / 2, getHeight() / 2),
                        new Point(0, getHeight()));
            default:
                return new Triangle(new Point(0, 0), new Point(0, 0), new Point(0, 0));
        }
    }

    /**
     * Generates the bounding box of the left part of the path (LEFT of the path
     * looking from the center of the tile)
     * 
     * @param direction The direction of the side
     * @return The bounding box of the right part of the path
     */
    private Triangle leftBoundingBoxFromDirection(Direction direction) {
        switch (direction) {
            case UP:
                return new Triangle(new Point(0, 0), new Point(pathLength, 0), new Point(pathLength, pathLength));
            case RIGHT:
                return new Triangle(new Point(getWidth(), 0), new Point(getWidth(), pathLength),
                        new Point(2 * pathLength, pathLength));
            case DOWN:
                return new Triangle(new Point(getWidth(), getHeight()), new Point(2 * pathLength, 2 * pathLength),
                        new Point(2 * pathLength, getHeight()));
            case LEFT:
                return new Triangle(new Point(0, getHeight()), new Point(0, 2 * pathLength),
                        new Point(pathLength, 2 * pathLength));
            default:
                return new Triangle(new Point(0, 0), new Point(0, 0), new Point(0, 0));
        }
    }

    /**
     * Generates the bounding box of the right part of the path (right of the path
     * looking from the center of the tile)
     * 
     * @param direction The direction of the side
     * @return The bounding box of the right part of the path
     */
    private Triangle rightBoundingBoxFromDirection(Direction direction) {
        switch (direction) {
            case UP:
                return new Triangle(new Point(2 * pathLength, 0), new Point(getWidth(), 0),
                        new Point(2 * pathLength, pathLength));
            case RIGHT:
                return new Triangle(new Point(getWidth(), 2 * pathLength), new Point(getWidth(), getHeight()),
                        new Point(2 * pathLength, 2 * pathLength));
            case DOWN:
                return new Triangle(new Point(pathLength, 2 * pathLength), new Point(2 * pathLength, getHeight()),
                        new Point(0, getHeight()));
            case LEFT:
                return new Triangle(new Point(0, 0), new Point(0, pathLength), new Point(pathLength, pathLength));
            default:
                return new Triangle(new Point(0, 0), new Point(0, 0), new Point(0, 0));
        }
    }

    @Override
    public void updateModel(TileCarcassonne model) {
        this.tileModel = model;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        String pathToImage = "src/main/resources/tiles_carcassonne/" + tileModel.getId() + ".png";
        ImageIcon icon = new ImageIcon(pathToImage);
        Image image = icon.getImage();

        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        TileCarcassonnePanel tile = new TileCarcassonnePanel(new TileCarcassonne(12), Color.RED);
        tile.setCanPawnBePlaced(true);
        frame.add(tile);
        frame.pack();
        frame.setVisible(true);
    }

}
