package iceberg;

import blok.templates.*;

public class Iceberg extends IPlugin implements IUIFactory{
    
    private IBackground background;
    private IGround ground;
    private IBlocks block;
    private IPlayer player;
    private static Iceberg instance;
    
    public Iceberg(){
        this.background = new Background();
        this.ground = new Ground();
        this.block  = new Blocks();
        this.player = new Player();
    }
    
    public static Iceberg getInstance(){
        return (instance == null) ? instance = new Iceberg() : instance;
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
