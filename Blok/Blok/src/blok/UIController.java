package blok;

import gui.GamePanel;
import gui.UIChoosePanel;
import interfaces.ICore;
import interfaces.IPanels;
import interfaces.ISimulator;
import interfaces.IUIController;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

class UIController extends JFrame implements IUIController {
    ICore core;
    GamePanel gamePanel;
    UIChoosePanel chosePanel;
    IPanels panel;

    UIController(ICore core) {
        this.core = core;
    }

    @Override
    public boolean initialize() {
        try {
            Dimension sizeUIChose = new Dimension(400, 300);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            
            UIChoosePanel chosePanel = new UIChoosePanel(this.core);
            chosePanel.setPreferredSize(sizeUIChose);
            chosePanel.setMinimumSize(sizeUIChose);
            chosePanel.setMaximumSize(sizeUIChose);
            chosePanel.setSize(sizeUIChose);
            setContentPane(chosePanel);
            setResizable(false);
            pack();
            
        } catch (InstantiationException ex) {
            Logger.getLogger(UIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    public void GamePanelScreen() {
        Dimension sizeGamePanel = new Dimension(1000, 600);
        GamePanel gamePanel = new GamePanel(this.core);
        gamePanel.setPreferredSize(sizeGamePanel);
        gamePanel.setMinimumSize(sizeGamePanel);
        gamePanel.setMaximumSize(sizeGamePanel);
        gamePanel.setSize(sizeGamePanel);
        setContentPane(gamePanel);
        setResizable(false);
        pack();
        panel = gamePanel;
        
        ISimulator simulator = core.getGameController().getCurrentPhysicsFactory();
        gamePanel.setSimulator(simulator);
        simulator.initialize(core);
    }
    
    @Override
    public void setCurrentPlugins(int UIChoose, String physicsChoose) {
        core.getPluginController().setCurrentPlugins(UIChoose, physicsChoose);
    }

    @Override
    public void repaint() {
        panel.pintar();
    }
    
    @Override
    public Dimension dimensionSize() {
        Dimension size = getSize();
        return size;
    }
    
    @Override
    public void setM_PLAYER(Rectangle rect) {
        GamePanel.setM_PLAYER(rect);
    }
}
