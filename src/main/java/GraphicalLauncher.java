import javax.swing.JFrame;
import javax.swing.WindowConstants;

import shared.view.StartMenu;

public class GraphicalLauncher {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(950, 950);

        StartMenu menu = new StartMenu(frame);
        frame.add(menu);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
