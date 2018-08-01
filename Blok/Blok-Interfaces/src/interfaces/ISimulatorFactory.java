package interfaces;

public interface ISimulatorFactory {
     public ISimulator createSimulator(String simulatorName);
     public ISimulatorFactory getSimulatorFactory();
}
