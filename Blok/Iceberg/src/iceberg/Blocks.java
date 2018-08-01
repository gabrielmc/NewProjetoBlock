package iceberg;

import blok.templates.IBlocks;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Blocks implements IBlocks{
    
    @Override
    public Image loadBlocks() {
        return new ImageIcon(this.getClass().getResource("resources/brick.png")).getImage();
    }
}
