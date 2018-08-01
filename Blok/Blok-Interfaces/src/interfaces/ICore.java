package interfaces;

public interface ICore {
    public boolean initialize();
    public IUIController getUIController();
    public IGameController getGameController();
    public IPluginController getPluginController();
}
