package gui;

import interfaces.ICore;
import interfaces.IGameController;
import interfaces.IPanels;
import interfaces.ISimulator;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener, KeyEventPostProcessor, IPanels {
    
    private ICore core;
    private BufferedImage playerImage;
    private Image displayPlayerImage;
    private ISimulator m_simulator;
    private static Rectangle m_player;
    private String m_playerImage;

    public GamePanel(ICore core) {
        this.core = core;
        initComponents();
        setFocusable(true);
        addMouseListener(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this);
        displayPlayerImage = core.getGameController().getCurrentUIFactory().createPlayer().loadPlayer();
        playerImage = new BufferedImage(
            displayPlayerImage.getWidth(this),
            displayPlayerImage.getHeight(this),
            BufferedImage.TRANSLUCENT);
        playerImage.createGraphics().drawImage(displayPlayerImage, 0, 0, this);
        playWav("sounds/background.wav", -1);
    }

    final void playWav(final String wavFile, final int times) {
        (new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                System.out.println(AudioSystem.getMixerInfo()[1].getName());
                Clip clip = AudioSystem.getClip();
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(wavFile));
                clip.open(ais);
                clip.loop(times);
            } catch (MalformedURLException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }})).start();
    }
    
    public void setSimulator(ISimulator simulator) {
        m_simulator = simulator;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        m_simulator.mouseReleased(e);
    }
    
    public boolean postProcessKeyEvent(KeyEvent ke) {
        if (ke.getID() == KeyEvent.KEY_RELEASED) {
            switch(core.getGameController().getState()) {
                case INITIAL:
                    core.getGameController().setState(IGameController.State.RUNNING);
                    break;
                case YOUWON:
                case YOULOST:
                    core.getGameController().setState(IGameController.State.INITIAL);
                    break;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        Dimension size = getSize();
        g2d.drawImage(core.getGameController().getCurrentUIFactory().createBackground().loadBackground(), 0, 0, null);
        g2d.drawImage(core.getGameController().getCurrentUIFactory().createGround().loadGround(), size.width/2-450, size.height/2-10+260, null);

        for (Rectangle rect : m_simulator.getM_BODYRECT()) {
            if (rect != m_player) {
                // Block
                Image displayImage = core.getGameController().getCurrentUIFactory().createBlocks().loadBlocks();
                BufferedImage bi = new BufferedImage(
                                 displayImage.getWidth(this),
                                 displayImage.getHeight(this),
                                 BufferedImage.TRANSLUCENT);
                bi.createGraphics().drawImage(displayImage, 0, 0, this);
                try {
                    TexturePaint texturePaint = new TexturePaint(bi, rect);
                    g2d.setPaint(texturePaint);
                } catch (Exception ex) {
                    Logger.getLogger(ISimulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                // Player
                
                try {
                    TexturePaint texturePaint = new TexturePaint(playerImage, rect);
                    g2d.setPaint(texturePaint);
                } catch (Exception ex) {
                    Logger.getLogger(ISimulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            g2d.fill(rect);
        }

        int x;
        FontMetrics fm = null;
        if (core.getGameController().getState() != IGameController.State.RUNNING)
        {
            g2d.setPaint(Color.black);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(size.width/2-250, size.height/2-200-50, 500, 100);
            g2d.setPaint(new Color(0xDF, 0xC1, 0x01));
            g2d.fillRect(size.width/2-250, size.height/2-200-50, 500, 100);

            g2d.setPaint(Color.black);
            g2d.setFont(new Font("Times", Font.BOLD, 18));
            fm = g2d.getFontMetrics();
        }
        if (core.getGameController().getState() == IGameController.State.INITIAL)
        {
            x = (int) fm.stringWidth("Remove all the blocks but do not")/2;
            g2d.drawString("Remove all the blocks but do not", size.width/2-x, size.height/2-200-10-5);

            x = fm.stringWidth("let this guy hit the ground, okay ?")/2;
            g2d.drawString("let this guy hit the ground, okay ?", size.width/2-x, size.height/2-200+10-5);
        }
        if (core.getGameController().getState() == IGameController.State.YOUWON)
        {
            x = (int) fm.stringWidth("Congratulations ! You won !")/2;
            g2d.drawString("Congratulations ! You won !", size.width/2-x, size.height/2-200);
        }
        if (core.getGameController().getState() == IGameController.State.YOULOST)
        {
            x = (int) fm.stringWidth("I'm sorry ! You lost !")/2;
            g2d.drawString("I'm sorry ! You lost !", size.width/2-x, size.height/2-200);
        }
        if (core.getGameController().getState() != IGameController.State.RUNNING)
        {
            g2d.setFont(new Font("Times", Font.BOLD, 10));
            fm = g2d.getFontMetrics();
            x = fm.stringWidth("Press any key to start")/2;
            g2d.drawString("Press any key to start", size.width/2-x, size.height/2-200+30);
        }
    }
    
    public static void setM_PLAYER(Rectangle rect) {
        m_player = rect;
    }
    
    public Rectangle getM_PLAYER() {
        return m_player;
    }
    
    public void pintar() {
        repaint();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    
}
