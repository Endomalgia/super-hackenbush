import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.DecimalFormat;
import javax.swing.border.EmptyBorder;

class GameBoard extends JPanel {

  /*//*/// VARIABLES

  private ArrayList<HackenbushPicture> pictures;
  //private FallingLeaf animatedLeaf;
  private Boolean isPressable;

  private javax.swing.Timer redTimer;

  public JLabel scoreCounter;
  public JPanel turnIndicator;
  public JPanel win = null;

  public int blunderMoves = 0;
  public int bestMoves = 0;

  private MouseAdapter mouseListener = new MouseAdapter() {
    public void mousePressed(MouseEvent e) {
      if (!getPressable()) return;

      System.out.print("MOUSE EVENT AT "+e.getX()+" "+e.getY());
      HackenbushPicture parentPicture = null;
      HackenbushLibrary hlib = null;
      int collideIndex = -1;
      ArrayList<HackenbushPicture> pics = getPictures();

      for (HackenbushPicture h : pics) {
        Vector<Integer> hpos = h.getPosition();
        collideIndex = h.collide(e.getX() - hpos.get(0), e.getY() - hpos.get(1), 16);
        System.out.println(" : "+collideIndex);
        if (collideIndex != -1) {
          // If the edge is not blue, continue
          hlib = h.getHackenbushLibrary();
          ArrayList<EdgeTint> tlib = hlib.getTintLibrary();
          if (tlib.get(collideIndex / 2) != EdgeTint.BLUE) continue;

          // Set parent picture and break out of loop
          parentPicture = h;
          break;
        };
      }
      if (parentPicture == null) return;

      // Dont worry about the spelling
      HackenbushMove bestMove = HackenbushPicture.getBestBlueMoveAccross(pics);


      // Remove the connection and add out the child nodes
      ArrayList<HackenbushNode> clib = hlib.getConnectionLibrary();
      ArrayList<HackenbushPicture> childPictures = parentPicture.removeDisorderedConnection(clib.get(collideIndex), clib.get(collideIndex+1));
      pics.remove(parentPicture);
      pics.addAll(childPictures);

      // HOW IS THE SLECTOR PASSING PICTURES TO THIS?

      pics = getPictures();
      for (int i=0; i<pics.size(); i++) {
        if (pics.get(i).getGroundNodes().size() == 0) {
          pics.remove(i);
          i--;
        }
      }

      Fraction out = new Fraction(0,1);
      for (HackenbushPicture p : childPictures) {
        p.calculateLongestLine();
        out.add(p.evaluateRedBlue());
      }

      System.out.println(out + "vs ideal" + bestMove.getValue());
      if (out.compareTo(bestMove.getValue()) > 0) {
        bestMoves++;
      } else {
        blunderMoves++;
      }

      turnIndicator.setBackground(new Color(226, 87, 74));

      invalidate();
      repaint();
      setPressable(false);

      redTimer.start();

    }
  };

  /*//*/// CONSTRUCTORS

  /***
   * Constructor for a new GameBoard.
   *
   * @param scoreCounter  JLabel that shows the score count associated with this board
   */
  public GameBoard(JLabel scoreCounter, JPanel turnIndicator) {
    super();
    this.scoreCounter = scoreCounter;
    this.turnIndicator = turnIndicator;
    addMouseListener(mouseListener);
    setPictures(new ArrayList<HackenbushPicture>());
    //setAnimatedLeaf(null);
    setPressable(true);
    invalidate();
    repaint();

    redTimer = new javax.swing.Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ArrayList<HackenbushPicture> pics = getPictures();

        Integer nRedMoves = 0;
        Integer nBlueMoves = 0;
        for (HackenbushPicture p : pics) {
          ArrayList<EdgeTint> tlib = p.getHackenbushLibrary().getTintLibrary();
          for (EdgeTint t : tlib) {
            if (t == EdgeTint.RED) nRedMoves++;
            if (t == EdgeTint.BLUE) nBlueMoves++;
          }
        }

        if (nRedMoves >= 1) {
          HackenbushMove redMove = HackenbushPicture.getBestRedMoveAccross(getPictures());
          if (redMove == null) {
            System.out.println("MOVE IS NULL!!!!");
          }
          ArrayList<HackenbushPicture> childPictures = redMove.getSource().removeDisorderedConnection(redMove.getOrigin(), redMove.getInsert());
          pics.remove(redMove.getSource());
          pics.addAll(childPictures);
        }
        nRedMoves--;

        Fraction out = new Fraction(0,1);
        for (HackenbushPicture p : pics) {
          p.calculateLongestLine();
          out.add(p.evaluateRedBlue());
        }
        System.out.println("VALUE = " + out);

        out.reduce();
        scoreCounter.setText((out.getDenominator() == 1) ? out.getNumerator() + "" : out.toString());

        turnIndicator.setBackground(new Color(63, 131, 240));

        if (nRedMoves <= 0) {
          turnIndicator.setBackground(new Color(102, 102, 102));

          win = new JPanel();
          win.setBounds(100,50,500,400);
          win.setBackground(Color.WHITE);
          win.setLayout(new BoxLayout(win, BoxLayout.PAGE_AXIS));
          win.setBorder(new EmptyBorder(100,0,0,0));

          JLabel title = new JLabel((nBlueMoves > 0) ? "YOU WIN!" : "GAME OVER");
          title.setFont(scoreCounter.getFont());

          title.setAlignmentX(Component.CENTER_ALIGNMENT);

          JLabel ideal = new JLabel("Ideal Moves : " + bestMoves);
          ideal.setFont(title.getFont().deriveFont(20f));

          ideal.setAlignmentX(Component.CENTER_ALIGNMENT);

          JLabel blunder = new JLabel("Blunders    : " + blunderMoves);
          blunder.setFont(ideal.getFont());

          blunder.setAlignmentX(Component.CENTER_ALIGNMENT);

          invalidate();
          repaint();

          javax.swing.Timer tempTimer = new javax.swing.Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              win.add(new JLabel(""));
              win.add(title);
              win.add(ideal);
              win.add(blunder);

              scoreCounter.getParent().add(win, JLayeredPane.POPUP_LAYER);
              invalidate();
              repaint();
            }
          });
          tempTimer.setRepeats(false);
          tempTimer.start();

          return;
        }

        invalidate();
        repaint();
        setPressable(true);
      }
    });
    redTimer.setRepeats(false);
  }

  /*//*/// METHODS

  /***
   * Draws the GameBoard (which includes hackenbush pictures) to a Graphics object
   *
   * @param gfx   The Graphics object to draw to
   */
  public void paintComponent(Graphics gfx) {
    super.paintComponent(gfx);

    for (HackenbushPicture h : getPictures()) {
      h.draw((Graphics2D)gfx);
    }

    /*
    FallingLeaf animLeaf = getAnimatedLeaf();
    System.out.println("DRAWING");
    if (animLeaf != null && animLeaf.isActive()) {
      System.out.println("DRAWING");
      animLeaf.draw((Graphics2D)gfx);
    }
    */
  }

  /***
   * Animate falling hackenbush leaves when called appropriately
   */
   /*
  public void animate() {
    FallingLeaf animLeaf = getAnimatedLeaf();
    animLeaf.setActive(true);
    float tf = 1.0f / animLeaf.getFallingLeafOpacityDelta();
    while (animLeaf.getTime() <= tf) {

      animLeaf.setTime(animLeaf.getTime() + 0.5f);

      System.out.println("Animating");
      invalidate();
      repaint();

      try {Thread.sleep(10);
      } catch (Exception e) {
        System.out.println("Error");
      }
    }
    animLeaf.setActive(false);
    animLeaf.setTime(0.0f);
  }
  */

  /*//*/// GET &* SET

  /** Check if the gameboard is currently able to be interacted with (is it your turn). @return boolean representing the result*/
  public Boolean getPressable() {return this.isPressable;}
  /** Get an array containing all of the hackenbush pictures on the board @return ArrayList of Hackenbush pictures*/
  public ArrayList<HackenbushPicture> getPictures() {return this.pictures;}
  /** Get the current leaf. @return current animated FallingLeaf*/
  //public FallingLeaf getAnimatedLeaf() {return this.animatedLeaf;}

  /** Set if the gameboard is currently able to be interacted with (is it your turn). @param isPressable Wheather it is or is not your turn*/
  public void setPressable(Boolean isPressable) {this.isPressable = isPressable;}
  /** Set the array of pictures on the board. @param pictures new array of pictures*/
  public void setPictures(ArrayList<HackenbushPicture> pictures) {this.pictures = pictures;}
  /** Set the current animated FallingLeaf. @param leaf The leaf to use*/
  //public void setAnimatedLeaf(FallingLeaf leaf) {this.animatedLeaf = leaf;}

}
