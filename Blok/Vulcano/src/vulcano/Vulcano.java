package vulcano;

import blok.templates.*;

public class Vulcano extends IPlugin implements IUIFactory{
    
    private IBackground background;
    private IGround ground;
    private IBlocks block;
    private IPlayer player;
    private static Vulcano instance;
    
    public Vulcano(){
        this.background = new Background();
        this.ground = new Ground();
        this.block  = new Blocks();
        this.player = new Player();
    }
    
    public static Vulcano getInstance(){
        return (instance == null) ? instance = new Vulcano() : instance;
    }
    
    @Override
    public IBackground createBackground() {
        return this.background;
    }

    @Override
    public IGround createGround() {
        return this.ground;
    }

    @Override
    public IBlocks createBlocks() {
        return this.block;
    }
    
    @Override
    public IPlayer createPlayer() {
        return this.player;
    }
}
