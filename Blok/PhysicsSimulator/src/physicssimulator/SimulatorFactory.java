package physicssimulator;

import interfaces.ISimulator;
import interfaces.ISimulatorFactory;

public class SimulatorFactory implements ISimulatorFactory{
    
    @Override
    public ISimulator createSimulator(String simulatorName) {
        return (simulatorName.equals("Box2D")) ? new Box2D() : null;
    }

    @Override
    public ISimulatorFactory getSimulatorFactory() {
        return this;
    }
}
