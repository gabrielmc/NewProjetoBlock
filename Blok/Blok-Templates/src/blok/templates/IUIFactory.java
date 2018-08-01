package blok.templates;

public interface IUIFactory{
    public IBackground createBackground();
    public IGround createGround();
    public IBlocks createBlocks();
    public IPlayer createPlayer();
}
