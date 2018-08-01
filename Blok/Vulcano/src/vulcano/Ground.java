package vulcano;

import blok.templates.IGround;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Ground implements IGround{
    
    @Override
    public Image loadGround() {
        return new ImageIcon(this.getClass().getResource("resources/ground.png")).getImage();
    }
}
