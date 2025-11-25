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

class TitleScreen extends JPanel {

  public static final String HELP_GRAPHIC = "./assets/ui/help/0.png";
  public static final String NOVEM_FONT = "./assets/fonts/novem-font/Novem-nALvR.ttf";
  public static final String TITLE_TEXT = "<html><font color='#3f83f0'>S</font><font color='#e2574a'>U</font><font color='#5f994b'>P</font><font color='#3f83f0'>E</font><font color='#5f994b'>R</font></html>";
  public static final String SUBTITLE_TEXT = "<html><b>hackenbush</b></html>";
  public static final int HELP_BUTTON_SIZE = 50;
  public static final int TITLE_X_OFFSET = 175;
  public static final int TITLE_Y_OFFSET = 50;

  public static final String BGR_SEQUENCE_PATH = "./assets/img/level-render";
  public static ImageSequence backgroundSequence = null;

  public static final String TITLE_SEQUENCE_PATH = "./assets/img/panoram";
  public static ImageSequence titleSequence = null;
  public int titleFrame = 0;
  public javax.swing.Timer titleTimer;

  public JButton helpButton;
  public JButton playButton;
  public JPanel titleCard;
  public JLabel titleLabel;
  public JLabel titleSubtitle;
  public ImageOverlay helpWindow;
  public JLayeredPane layerPane;

  /***
   * Create a new TitleScreen (including all associated child components) for installation on a JLayeredPane
   *
   * @param layerPane   The JLayeredPane to add everything to
   */
  public TitleScreen(JLayeredPane layerPane) {
    this.layerPane = layerPane;

    // Start loading the background sequence
    backgroundSequence = new ImageSequence(BGR_SEQUENCE_PATH, 20);
    titleSequence = new ImageSequence(TITLE_SEQUENCE_PATH, 21);

    setBounds(0,0,700,500);

    titleTimer = new javax.swing.Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        titleFrame = (titleFrame + 1) % 20;
        repaint();
      }
    });
    titleTimer.setRepeats(true);
    titleTimer.start();

    helpWindow = new ImageOverlay(new ImageIcon(HELP_GRAPHIC),layerPane);

    helpButton = new JButton("?");
    helpButton.setBounds(700 - HELP_BUTTON_SIZE, 0,
                         HELP_BUTTON_SIZE, HELP_BUTTON_SIZE);
    helpButton.addActionListener((e) -> {
      helpWindow.addToLayer();
    });

    playButton = new JButton("Play");
    playButton.setBounds(250, 260, 200, 50);
    playButton.addActionListener((e) -> {
      removeFromLayer();
      LevelSelect ls_main = new LevelSelect(layerPane, backgroundSequence);
      ls_main.addToLayer();
      layerPane.revalidate();
      layerPane.repaint();
    });

    titleCard = new JPanel();
    titleCard.setBounds(150,110,400,130);
    titleCard.setBackground(Color.WHITE);
    titleCard.setBorder(BorderFactory.createLineBorder(Color.black));

    titleLabel = new JLabel(TITLE_TEXT);
    titleLabel.setOpaque(false);
    titleLabel.setBounds(45+TITLE_X_OFFSET,TITLE_Y_OFFSET,400,200);

    titleSubtitle = new JLabel(SUBTITLE_TEXT);
    titleSubtitle.setOpaque(false);
    titleSubtitle.setBounds(TITLE_X_OFFSET,50+TITLE_Y_OFFSET,400,200);

    try {
      Font f_novem = Font.createFont(Font.TRUETYPE_FONT, new File(NOVEM_FONT));
      titleLabel.setFont(f_novem.deriveFont(60f));
      titleSubtitle.setFont(f_novem.deriveFont(40f));
    } catch (FontFormatException ffe) {
      System.out.println("ERROR: Failed to load font!");
      System.exit(-1);
    } catch (IOException ioe) {
      System.out.println("ERROR: Failed to load font!");
      System.exit(-1);
    }
  }

  /***
   * All all components associated with this object to their associated JLayeredPane
   */
  public void addToLayer() {
    layerPane.add(this, JLayeredPane.DEFAULT_LAYER);
    layerPane.add(helpButton, JLayeredPane.PALETTE_LAYER);
    layerPane.add(playButton, JLayeredPane.PALETTE_LAYER);
    layerPane.add(titleCard, JLayeredPane.PALETTE_LAYER);
    layerPane.add(titleLabel, JLayeredPane.POPUP_LAYER);
    layerPane.add(titleSubtitle, JLayeredPane.POPUP_LAYER);
  }

  /***
   * Remove all components associated with this object from their associated JLayeredPane
   */
  public void removeFromLayer() {
    layerPane.remove(this);
    layerPane.remove(helpButton);
    layerPane.remove(playButton);
    layerPane.remove(titleCard);
    layerPane.remove(titleLabel);
    layerPane.remove(titleSubtitle);

    titleTimer.stop();
    titleTimer.setRepeats(false);
  }

  /***
   * Draws the GameBoard (which includes hackenbush pictures) to a Graphics object
   *
   * @param gfx   The Graphics object to draw to
   */
  public void paintComponent(Graphics gfx) {

    AffineTransform mat_def = new AffineTransform(((Graphics2D)gfx).getTransform());
    AffineTransform mat_bgr = new AffineTransform(mat_def);
    mat_bgr.scale(4,4);
    ((Graphics2D)gfx).setTransform(mat_bgr);

    titleSequence.drawFrame((Graphics2D)gfx, 0,0, titleFrame);

  }

}
