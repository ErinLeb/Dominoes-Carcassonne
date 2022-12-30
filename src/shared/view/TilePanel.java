package shared.view;

import javax.swing.JPanel;

import shared.model.Tile;

public abstract class TilePanel<T extends Tile> extends JPanel {

    public abstract void updateModel(T model);

}
