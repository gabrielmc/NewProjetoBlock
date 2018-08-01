package space;

import blok.templates.IPlayer;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

public class Player implements IPlayer{

    @Override
    public Image loadPlayer() {
        String url = "resources/player" + Math.abs((new Random()).nextInt()%9) + ".png";
        return new ImageIcon(this.getClass().getResource(url)).getImage();
    }
}
