package carcassonne.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import carcassonne.model.PlayerCarcassonne;
import carcassonne.model.SideCarcassonne.Type;
import carcassonne.model.TileCarcassonne;
import carcassonne.model.TileCarcassonne.SideSelector;
import interfaces.Placeable.Direction;
import shared.view.TilePanel;
import utils.Pair;
import utils.geometry.Point;
import utils.geometry.Square;
import utils.geometry.Triangle;

public class TileCarcassonnePanel extends TilePanel<TileCarcassonne> {

    private TileCarcassonne tileModel;

    private PlayerCarcassonne player;

    private int pathLength;

    private boolean canPawnBePlaced;

    public TileCarcassonnePanel(TileCarcassonne tileModel) {
        this.tileModel = tileModel;

        if (tileModel != null) {
            this.player = tileModel.getPlayer();
        }

        if (tileModel == null) {
            initNull();
        } else {
            init();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(125, 125);
    }

    public void setCanPawnBePlaced(boolean canPawnBePlaced) {
        this.canPawnBePlaced = canPawnBePlaced;
    }

    private void init() {
        setLayout(null);

        addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        placePawn(e.getX(), e.getY());
                    }
                });

        drawPawn();
    }

    private void initNull() {
        setLayout(new GridLayout(1, 0));

        JPanel square = new JPanel();
        square.setBackground(Color.WHITE);
        square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(square);
    }

    private void placePawn(int x, int y) {
        pathLength = getWidth() / 3;
        Point click = new Point(x, y);

        if (!canPawnBePlaced || tileModel.isPawnPlaced() || isOutOfBounds(click))
            return;

        if (tileModel.hasAbbey()) {
            handleAbbey(click);
            if (tileModel.isPawnPlaced()) {
                return;
            }
        }
        for (Direction direction : Direction.values()) {
            handleSide(direction, click);
            if (tileModel.isPawnPlaced()) {
                return;

            }
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
            drawPawn();
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

        BoundingBoxHandler boundingBoxHandler = new BoundingBoxHandler(getWidth(), getHeight(), pathLength, direction);

        // Generates the bounding box of the side
        Triangle sideBoundingBox = boundingBoxHandler.sideBoundingBoxFromDirection();

        if (!sideBoundingBox.isInside(click))
            return;

        // Generates the bounding box of the left part of the path (left of the path
        // looking from the center of the tile)
        Triangle lefBoundingBox = boundingBoxHandler.leftBoundingBoxFromDirection();

        if (tileModel.getSide(direction).getType() != Type.PATH || lefBoundingBox.isInside(click)) {
            tileModel.placePawn(selectedSide, 0);
            drawPawn();
            return;
        }

        // Generates the bounding box of the right part of the path (right of the path
        // looking from the center of the tile)
        Triangle rightBoundingBox = boundingBoxHandler.rightBoundingBoxFromDirection();

        if (rightBoundingBox.isInside(click)) {
            tileModel.placePawn(selectedSide, 2);
            drawPawn();
            return;
        }

        // The tile is then in the path
        tileModel.placePawn(selectedSide, 1);
        drawPawn();
    }

    public void drawPawn() {

        if (tileModel == null || player == null || !tileModel.isPawnPlaced())
            return;

        removeAll();

        JPanel pawn = new JPanel();
        pawn.setBackground(player.getPawnColor());

        Pair<SideSelector, Integer> pawnLocation = tileModel.getPawnPosition();
        boolean isPath;

        if (pawnLocation.first == SideSelector.CENTER) {
            isPath = false;
        } else {
            isPath = tileModel.getSide(TileCarcassonne.sideSelectorToDirection(pawnLocation.first))
                    .getType() == Type.PATH;
        }

        PawnTilePositionHandler pawnTilePositionHandler = new PawnTilePositionHandler(getWidth(), getHeight(),
                pawnLocation, isPath);

        Point centerPawn = pawnTilePositionHandler.pawnPositionToPoint();
        pawn.setBounds(centerPawn.x - 5, centerPawn.y - 5, 10, 10);

        add(pawn);
        revalidate();
        repaint();
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

        if (tileModel == null)
            return;

        String pathToImage = "src/main/resources/tiles_carcassonne/" + tileModel.getId() + ".png";
        Image scaledImage;

        try {
            BufferedImage image = ImageIO.read(new File(pathToImage));
            scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        // Rotates the image
        drawRotatedImage(scaledImage, g2d);
    }

    private void drawRotatedImage(Image scaledImage, Graphics2D g2d) {
        AffineTransform rotationTransformation = AffineTransform.getRotateInstance(rotationsToAngle(),
                getWidth() / 2., getHeight() / 2.);

        Rectangle2D bounds = rotationTransformation
                .createTransformedShape(new Rectangle2D.Double(0, 0, getWidth(), getHeight()))
                .getBounds2D();

        g2d.transform(
                AffineTransform.getScaleInstance(getWidth() / bounds.getWidth(), getHeight() / bounds.getHeight()));

        g2d.drawImage(scaledImage, rotationTransformation, null);
    }

    private double rotationsToAngle() {
        return tileModel.getNbOfRotations() * Math.PI / 2.;
    }
}
