package space;

import blok.templates.*;

public class Space extends IPlugin implements IUIFactory{
    
    private IBackground background;
    private IGround ground;
    private IBlocks block;
    private IPlayer player;
    
    public Space(){
        this.background = new Background();
        this.ground = new Ground();
        this.block  = new Blocks();
        this.player = new Player();
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
