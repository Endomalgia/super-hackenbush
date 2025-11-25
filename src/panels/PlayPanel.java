import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class PlayPanel extends JPanel {

  public static final int BUTTON_SIZE = 50;
  public static final String JUA_FONT = "./assets/fonts/Jua/Jua-Regular.ttf";
  public static final String NEXT_IMAGE = "./assets/ui/next.png";
  public static final String PREV_IMAGE = "./assets/ui/previous.png";
  public static final String HELP_IMAGE =  "./assets/ui/question.png";;
  public static final String RETURN_IMAGE = "./assets/ui/backward.png";
  public static final String[] HELP_IMAGES = {
    "./assets/ui/help/1.png",
    "./assets/ui/help/2.png",
    "./assets/ui/help/3.png",
    "./assets/ui/help/4.png"
  };

  public static final String LEVEL_DIRECTORY = "./assets/hps/levels";

  public JButton levelSelectButton;
  public JButton helpButton;
  public JPanel groundPanel;
  public JPanel turnIndicator;
  public JLayeredPane layerPane;
  public ImageOverlay helpWindow;
  public GameBoard gameBoard;
  public JLabel scoreCounter;

  /***
   * Create a new PlayPanel (including all associated child components) for installation on a JLayeredPane
   *
   * @param layerPane   The JLayeredPane to add everything to
   */
  public PlayPanel(JLayeredPane layerPane, int levelId) {
    this.layerPane = layerPane;

    setBounds(0,0,700,500);
    setBackground(Color.WHITE);

    levelSelectButton = new JButton("");
    levelSelectButton.setBounds(0,0,BUTTON_SIZE,BUTTON_SIZE);
    levelSelectButton.setIcon(new ImageIcon(RETURN_IMAGE));
    levelSelectButton.addActionListener((e) -> {
      LevelSelect ls_main = new LevelSelect(layerPane, null);
      removeFromLayer();
      ls_main.addToLayer();
    });

    turnIndicator = new JPanel();
    turnIndicator.setBounds(300,400,100,25);
    turnIndicator.setBackground(new Color(63, 131, 240));
    turnIndicator.setBorder(BorderFactory.createLineBorder(Color.black));

    helpWindow = new ImageOverlay(new ImageIcon(HELP_IMAGES[levelId]), layerPane);
    helpButton = new JButton("");
    helpButton.setBounds(700-BUTTON_SIZE,0,BUTTON_SIZE,BUTTON_SIZE);
    helpButton.setIcon(new ImageIcon(HELP_IMAGE));
    helpButton.addActionListener((e) -> {
      helpWindow.addToLayer();
    });

    scoreCounter = new JLabel("7");
    scoreCounter.setBounds(320,-15,400,100);
    scoreCounter.setVisible(false);

    try {
      Font f_jua = Font.createFont(Font.TRUETYPE_FONT, new File(JUA_FONT));
      scoreCounter.setFont(f_jua.deriveFont(40f));
    } catch (FontFormatException ffe) {
      System.out.println("ERROR: Failed to load font!");
      System.exit(-1);
    } catch (IOException ioe) {
      System.out.println("ERROR: Failed to load font!");
      System.exit(-1);
    }

    groundPanel = new JPanel();
    groundPanel.setOpaque(true);
    groundPanel.setBackground(Color.BLACK);
    groundPanel.setBounds(200,300,300,20);

    gameBoard = generateLevel(levelId);
    gameBoard.setOpaque(false);
    gameBoard.setBounds(100,50,500,250); // Window is 500 x 250
  }

  /***
   * All all components associated with this object to their associated JLayeredPane
   */
  public void addToLayer() {
    layerPane.add(this, JLayeredPane.DEFAULT_LAYER);
    layerPane.add(turnIndicator, JLayeredPane.PALETTE_LAYER);
    layerPane.add(levelSelectButton, JLayeredPane.PALETTE_LAYER);
    layerPane.add(helpButton, JLayeredPane.PALETTE_LAYER);
    layerPane.add(groundPanel, JLayeredPane.PALETTE_LAYER);
    layerPane.add(gameBoard, JLayeredPane.PALETTE_LAYER);
    layerPane.add(scoreCounter, JLayeredPane.PALETTE_LAYER);
  }

  /***
   * Remove all components associated with this object from their associated JLayeredPane
   */
  public void removeFromLayer() {
    layerPane.removeAll();
    layerPane.remove(this);
    layerPane.remove(turnIndicator);
    layerPane.remove(levelSelectButton);
    layerPane.remove(helpButton);
    layerPane.remove(groundPanel);
    layerPane.remove(gameBoard);
    layerPane.remove(scoreCounter);
    layerPane.revalidate();
    layerPane.repaint();
  }

  public GameBoard generateLevel(int index) {
    GameBoard gb_main = new GameBoard(scoreCounter, turnIndicator);

    // ERROR TRACED TO HERE
    ArrayList<HackenbushPicture> pics = HackenbushParser.parseMultipleHPS(LEVEL_DIRECTORY + "/" + index + ".hps");
    gb_main.setPictures(pics);

    /*
    switch(index) { // game board is 500 x 250
      case 0:

        TintWeb2D gndBl = new TintWeb2D(true, 150, 250);
        TintWeb2D stemB = new TintWeb2D(150,100);
        gndBl.addNode(stemB, EdgeTint.BLUE);
        for (int i=100; i<200; i+=10) {
          stemB.addNode(new TintWeb2D(i, 80), EdgeTint.BLUE);
        }

        TintWeb2D gndR = new TintWeb2D(true, 350, 250);
        TintWeb2D stemR = new TintWeb2D(350,100);
        gndR.addNode(stemR, EdgeTint.RED);
        for (int i=300; i<400; i+=10) {
          stemR.addNode(new TintWeb2D(i, 80), EdgeTint.RED);
        }

        Hackenbush picBl = new Hackenbush(new ArrayList<TintWeb2D>(Arrays.asList(gndBl)),
                                         new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND),
                                         new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND),
                                         new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND));
        picBl.buildConnectionLibrary();
        Hackenbush picR = new Hackenbush(picBl);
        picR.getGroundNodes().remove(0);
        picR.getGroundNodes().add(gndR);
        picR.buildConnectionLibrary();
        gb_main.getPictures().add(picBl);
        gb_main.getPictures().add(picR);

        break;
      case 1:

        TintWeb2D gndA = new TintWeb2D(true, 150, 250);
        TintWeb2D stemA2 = new TintWeb2D(150, 150);

        gndA.addNode(stemA2, EdgeTint.RED);

        TintWeb2D stemB1 = new TintWeb2D(250, 150);

        stemA2.addNode(stemB1, EdgeTint.BLUE);

        TintWeb2D gndB = new TintWeb2D(true, 300, 250);

        TintWeb2D stemC1 = new TintWeb2D(350, 200);
        TintWeb2D stemC2 = new TintWeb2D(350, 150);
        TintWeb2D stemC3 = new TintWeb2D(350, 100);

        gndB.addNode(stemC1, EdgeTint.RED);
        stemC1.addNode(stemC2, EdgeTint.BLUE);
        stemC2.addNode(stemC3, EdgeTint.BLUE);

        TintWeb2D gndC = new TintWeb2D(true, 225, 250);

        TintWeb2D stemD1 = new TintWeb2D(225, 200);
        TintWeb2D stemD2 = new TintWeb2D(325, 150);
        TintWeb2D stemD3 = new TintWeb2D(225, 100);

        gndC.addNode(stemD1, EdgeTint.RED);
        stemD1.addNode(stemD2, EdgeTint.BLUE);
        stemD2.addNode(stemD3, EdgeTint.RED);

        Hackenbush picA = new Hackenbush(new ArrayList<TintWeb2D>(Arrays.asList(gndA)),
                                                                 new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND),
                                                                 new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND),
                                                                 new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND));

        picA.buildConnectionLibrary();
        Hackenbush picB = new Hackenbush(picA);
        Hackenbush picC = new Hackenbush(picB);
        picB.getGroundNodes().remove(0);
        picB.getGroundNodes().add(gndB);
        picB.buildConnectionLibrary();
        picC.getGroundNodes().remove(0);
        picC.getGroundNodes().add(gndC);
        picC.buildConnectionLibrary();
        gb_main.getPictures().add(picA);
        gb_main.getPictures().add(picB);
        gb_main.getPictures().add(picC);

        break;
      case 2:

        TintWeb2D gndX = new TintWeb2D(true, 150, 250);
        TintWeb2D stemX1 = new TintWeb2D(true, 150, 200);
        TintWeb2D stemX2 = new TintWeb2D(true, 150, 150);
        gndX.addNode(stemX1, EdgeTint.RED);
        stemX1.addNode(stemX2, EdgeTint.RED);

        TintWeb2D gndY = new TintWeb2D(true, 200, 250);
        TintWeb2D stemY1 = new TintWeb2D(true, 200, 200);
        TintWeb2D stemY2 = new TintWeb2D(true, 200, 150);
        gndY.addNode(stemY1, EdgeTint.BLUE);
        stemY1.addNode(stemY2, EdgeTint.RED);

        TintWeb2D gndZ = new TintWeb2D(true, 250, 250);
        TintWeb2D stemZ1 = new TintWeb2D(true, 250, 200);
        TintWeb2D stemZ2 = new TintWeb2D(true, 250, 150);
        gndZ.addNode(stemZ1, EdgeTint.RED);
        stemZ1.addNode(stemZ2, EdgeTint.BLUE);

        Hackenbush picX = new Hackenbush(new ArrayList<TintWeb2D>(Arrays.asList(gndX)),
                                                                 new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND),
                                                                 new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND),
                                                                 new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND));

        picX.buildConnectionLibrary();
        Hackenbush picY = new Hackenbush(picX);
        Hackenbush picZ = new Hackenbush(picY);
        picY.getGroundNodes().remove(0);
        picY.getGroundNodes().add(gndY);
        picY.buildConnectionLibrary();
        picZ.getGroundNodes().remove(0);
        picZ.getGroundNodes().add(gndZ);
        picZ.buildConnectionLibrary();
        gb_main.getPictures().add(picX);
        gb_main.getPictures().add(picY);
        gb_main.getPictures().add(picZ);

        break;
      case 3:

        Random rng = new Random();

        TintWeb2D gndNode = new TintWeb2D(true, 250, 250);
        TintWeb2D next = new TintWeb2D(rng.nextInt(400), rng.nextInt(250));
        gndNode.addNode(next, EdgeTint.RED);
        for (int i=0; i<10; i++) {
          TintWeb2D nextnext = new TintWeb2D(rng.nextInt(400), rng.nextInt(250));
          next.addNode(nextnext, (rng.nextBoolean()) ? EdgeTint.RED : EdgeTint.BLUE);
          next = nextnext;
        }

        Hackenbush picture = new Hackenbush(new ArrayList<TintWeb2D>(Arrays.asList(gndNode)),
                                                                 new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND),
                                                                 new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND),
                                                                 new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND));
        picture.buildConnectionLibrary();
        gb_main.getPictures().add(picture);

        break;
      default:
        System.out.println("OTHER");
    }
    */
    Fraction score = new Fraction(0,1);
    for (HackenbushPicture h : gb_main.getPictures()) {
      score.add(h.evaluateRedBlue());
    }
    scoreCounter.setText(score.toString());
    return gb_main;
  }


}
