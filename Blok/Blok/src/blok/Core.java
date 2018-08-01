package blok;

import interfaces.ICore;
import interfaces.IGameController;
import interfaces.IPluginController;
import interfaces.IUIController;

public class Core implements ICore {

    private IUIController uiController;
    private IGameController gameController;
    private IPluginController pluginController;
    
    @Override
    public boolean initialize() {
        this.pluginController = new PluginController();
        this.uiController = new UIController();
        this.gameController = new GameController();
        return this.pluginController.initialize(this) && this.uiController.initialize(this) && this.gameController.initialize(this);
    }

    @Override
    public IUIController getUIController() {
        return this.uiController;
    }

    @Override
    public IGameController getGameController() {
        return this.gameController;
    }

    @Override
    public IPluginController getPluginController() {
        return this.pluginController;
    }
}
