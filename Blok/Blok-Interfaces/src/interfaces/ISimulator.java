package interfaces;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Collection;

public interface ISimulator {
    public boolean initialize(ICore core);
    public void init();
    public void start();
    public void stop();
    public void run();
    public void mouseReleased(MouseEvent e);
    public Collection<Rectangle> getM_BODYRECT();
}
