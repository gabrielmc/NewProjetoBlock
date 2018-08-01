package blok;

import interfaces.ICore;
import interfaces.IGameController;
import blok.templates.IUIFactory;
import interfaces.ISimulator;
import interfaces.ISimulatorFactory;
import physicssimulator.SimulatorFactory;

public class GameController implements IGameController {
    
    private ICore core;
    private IUIFactory currentUI;
    private ISimulator currentPhysics;
    private ISimulatorFactory simulatorFactory;
    private State m_state = State.INITIAL;

    @Override
    public boolean initialize(ICore core) {
        this.core = core;
        simulatorFactory = (ISimulatorFactory) new SimulatorFactory();
        return true;
    }
    
    @Override
    public void setState(State state) {
        m_state = state;
        switch(m_state) {
            case INITIAL:
                getCurrentPhysicsFactory().init();
                getCurrentPhysicsFactory().stop();
                break;
            case RUNNING:
                getCurrentPhysicsFactory().start();
                break;
        }
        core.getUIController().repaint();
    }
    
    @Override
    public IUIFactory getCurrentUIFactory() {
        return currentUI;
    }
    
    @Override
    public void setCurrentUIFactory(IUIFactory factory) {
        currentUI = factory;
    }
    
    @Override
    public ISimulator getCurrentPhysicsFactory() {
        return currentPhysics;
    }
    
    @Override
    public void setCurrentPhysicsFactory(String simulatorName) {
        currentPhysics = simulatorFactory.createSimulator(simulatorName);
    }
    
    @Override
    public State getState() {
        return m_state;
    }
}
