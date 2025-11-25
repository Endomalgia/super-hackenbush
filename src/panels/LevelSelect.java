import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.KeyboardFocusManager;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;

class LevelSelect extends JPanel {

  public JLayeredPane layerPane;

  ///////////////////////////////

  public static final String BGR_SEQUENCE_PATH = "./assets/img/level-render";
  public static final String CONWAY_UP_SEQUENCE_PATH = "./assets/img/conway/conway-up";
  public static final String CONWAY_DOWN_SEQUENCE_PATH = "./assets/img/conway/conway-down";
  public static final String CONWAY_LEFT_SEQUENCE_PATH = "./assets/img/conway/conway-left";
  public static final String CONWAY_RIGHT_SEQUENCE_PATH = "./assets/img/conway/conway-right";
  public int WALK_SPEED = 2;

  public static ImageSequence backgroundSequence = null;
  public int backgroundFrame;
  public javax.swing.Timer backgroundTimer;

  public static ImageSequence conwaySequenceUp = null;
  public static ImageSequence conwaySequenceDown = null;
  public static ImageSequence conwaySequenceLeft = null;
  public static ImageSequence conwaySequenceRight = null;
  public static int conwayDirection = 0; // 0 1 2 3
  public static int conwayFrame = 0;

  public static int hardcodedFrameDelayMax = 1;
  public static int hardcodedFrameDelay = 0;

  public static int cameraX = -174;
  public static int cameraY = -399;

  /***
   * Create a new LevelSelect (including all associated child components) for installation on a JLayeredPane
   *
   * @param layerPane   The JLayeredPane to add everything to
   */
  public LevelSelect(JLayeredPane layerPane, ImageSequence backgroundSequence) {
    this.layerPane = layerPane;
    setBounds(0,0,layerPane.getWidth(), layerPane.getHeight());

    if (this.backgroundSequence == null) {
      this.backgroundSequence = backgroundSequence;
    }
    backgroundFrame = 0;

    if (conwaySequenceUp == null) {
      conwaySequenceUp = new ImageSequence(CONWAY_UP_SEQUENCE_PATH, 8);
      conwaySequenceDown = new ImageSequence(CONWAY_DOWN_SEQUENCE_PATH,  8);
      conwaySequenceLeft = new ImageSequence(CONWAY_LEFT_SEQUENCE_PATH, 8);
      conwaySequenceRight = new ImageSequence(CONWAY_RIGHT_SEQUENCE_PATH, 8);
    }

    backgroundTimer = new javax.swing.Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        backgroundFrame = (backgroundFrame + 1) % 20;
        repaint();
      }
    });
    backgroundTimer.setRepeats(true);
    backgroundTimer.start();

    Boolean hardCodedDelay = true;
    layerPane.getParent().requestFocus();
    PlayPanel pp_main = null;
    layerPane.getParent().addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
          case KeyEvent.VK_SHIFT:
            WALK_SPEED = 6;
            hardcodedFrameDelayMax = 3;
            break;
          case KeyEvent.VK_UP:
            conwayDirection = 0;
            cameraY += WALK_SPEED;
            break;
          case KeyEvent.VK_DOWN:
            conwayDirection = 2;
            cameraY -= WALK_SPEED;
            break;
          case KeyEvent.VK_LEFT:
            conwayDirection = 1;
            cameraX += WALK_SPEED;
            break;
          case KeyEvent.VK_RIGHT:
            conwayDirection = 3;
            cameraX -= WALK_SPEED;
            break;
          case KeyEvent.VK_1:
            removeFromLayer();
            new PlayPanel(layerPane, 1).addToLayer();
            break;
          case KeyEvent.VK_2:
            removeFromLayer();
            new PlayPanel(layerPane, 2).addToLayer();
            break;
          case KeyEvent.VK_3:
            removeFromLayer();
            new PlayPanel(layerPane, 3).addToLayer();
            break;
          case KeyEvent.VK_4:
            removeFromLayer();
            new PlayPanel(layerPane, 4).addToLayer();
            break;
        }
        if (hardcodedFrameDelay == 0) {
          conwayFrame = (conwayFrame + 1) % 8;
        }
        hardcodedFrameDelay = (hardcodedFrameDelay+1) % hardcodedFrameDelayMax;
        repaint();
      }
      public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
          case KeyEvent.VK_SHIFT:
            hardcodedFrameDelayMax = 1;
            WALK_SPEED = 3;
            break;
          case KeyEvent.VK_UP:
            conwayDirection = 0;
            conwayFrame = 6;
            break;
          case KeyEvent.VK_DOWN:
            conwayDirection = 2;
            conwayFrame = 3;
            break;
          case KeyEvent.VK_LEFT:
            conwayDirection = 1;
            conwayFrame = 2;
            break;
          case KeyEvent.VK_RIGHT:
            conwayDirection = 3;
            conwayFrame = 2;
            break;
        }
        repaint();
      }
      public void keyTyped(KeyEvent e) {

      }
    });
  }

  /***
   * Draws the GameBoard (which includes hackenbush pictures) to a Graphics object
   *
   * @param gfx   The Graphics object to draw to
   */
  public void paintComponent(Graphics gfx) {

    AffineTransform mat_def = new AffineTransform(((Graphics2D)gfx).getTransform());

    AffineTransform mat_bgr = new AffineTransform(mat_def);
    mat_bgr.translate(cameraX, cameraY);
    mat_bgr.scale(0.5,0.5);
    ((Graphics2D)gfx).setTransform(mat_bgr);

    backgroundSequence.drawFrame((Graphics2D)gfx, -200,-200, backgroundFrame);

    int imWidth = 200;
    AffineTransform mat_conw = new AffineTransform(mat_def);
    mat_conw.translate(350 - imWidth/2,250 - imWidth/2);
    mat_conw.scale(0.05,0.05);
    ((Graphics2D)gfx).setTransform(mat_conw);

    switch(conwayDirection) {
    case 0:
      conwaySequenceUp.drawFrame((Graphics2D)gfx, 0,0, conwayFrame);
      break;
    case 1:
      conwaySequenceLeft.drawFrame((Graphics2D)gfx, 0,0, conwayFrame);
      break;
    case 2:
      conwaySequenceDown.drawFrame((Graphics2D)gfx, 0,0, conwayFrame);
      break;
    case 3:
      conwaySequenceRight.drawFrame((Graphics2D)gfx, 0,0, conwayFrame);
      break;
    }

  }

  /***
   * All all components associated with this object to their associated JLayeredPane
   */
  public void addToLayer() {
    layerPane.add(this, JLayeredPane.DEFAULT_LAYER);
  }

  /***
   * Remove all components associated with this object from their associated JLayeredPane
   */
  public void removeFromLayer() {
    layerPane.remove(this);

    backgroundTimer.stop();
    backgroundTimer.setRepeats(false);
  }

}
