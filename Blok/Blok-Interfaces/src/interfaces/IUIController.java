package interfaces;

import java.awt.Dimension;
import java.awt.Rectangle;

public interface IUIController {
    public boolean initialize(ICore core);
    public void GamePanelScreen();
    public void setCurrentPlugins(int UIChoose, String physicsChoose);
    public void repaint();
    public Dimension dimensionSize();
    public void setM_PLAYER(Rectangle rect);            
}
