package blok;

import interfaces.ICore;
import interfaces.IGameController;
import interfaces.IPluginController;
import interfaces.IUIController;

public class Core implements ICore {

    private IUIController uiController;
    private IGameController gameController;
    private IPluginController pluginController;
    private static ICore instance;
    
    @Override
    public boolean initialize() {
        return this.pluginController.initialize() && this.uiController.initialize() && this.gameController.initialize();
    }
    
    private Core(){
        this.pluginController = new PluginController(this);
        this.uiController = new UIController(this);
        this.gameController = new GameController(this);
    } 
    
    public static synchronized ICore getInstance(){
        return (instance == null) ? instance = new Core() : instance;
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
