package iceberg;

import blok.templates.IBackground;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Background implements IBackground{
    
    @Override
    public Image loadBackground() {
        return new ImageIcon(this.getClass().getResource("resources/background.jpg")).getImage();
    }
}
