import java.util.*;
import java.awt.*;

enum HackenbushTint {
  GREEN,
  RED,
  BLUE,
  REDBLUE
}

class Hackenbush {

  /*//*/// VARIABLES

  private static final Map<EdgeTint,Color> tintMap = Map.of(
    EdgeTint.GREEN, new Color(95, 153, 75),   // #5f994b
    EdgeTint.RED,   new Color(226, 87, 74),   // #e2574a
    EdgeTint.BLUE,  new Color(63, 131, 240)); // #3f83f0

  private ArrayList<TintWeb2D> groundNodes;
  private ArrayList<TintWeb2D> loopArchive;
  private ArrayList<TintWeb2D> connectionLibrary;
  private ArrayList<EdgeTint> tintLibrary;
  private int longestLine;

  private BasicStroke lineStroke;
  private BasicStroke dotStroke;
  private BasicStroke innerDotStroke;
  private Color dotColor;
  private Color innerDotColor;

  /*//*/// CONSTRUCTORS

  /***
   * Constructor for a new Hackenbush picture
   *
   * @param groundNodes     ground nodes present in the picture
   * @param lineStroke      BasicStroke used for drawing the picture lines
   * @param dotStroke       BasicStroke used for drawing the outer edges of the dots connecting the lines
   * @param innerDotStroke  BasicStroke used for drawing the inner portion of the dots connecting the lines
   */
  public Hackenbush(ArrayList<TintWeb2D> groundNodes, BasicStroke lineStroke, BasicStroke dotStroke, BasicStroke innerDotStroke) {
    setGroundNodes(groundNodes);
    setLoopArchive(new ArrayList<TintWeb2D>());
    setConnectionLibrary(new ArrayList<TintWeb2D>());
    setTintLibrary(new ArrayList<EdgeTint>());
    setLineStroke(lineStroke);
    setDotStroke(dotStroke);
    setInnerDotStroke(innerDotStroke);
    setInnerDotColor(Color.WHITE);
    setDotColor(Color.BLACK);
  }

  /***
   * Copy constructor for a new Hackenbush picture. Intentionally a shallo
   *
   * @param h  The hackenbush picture to copy from.
   */
  public Hackenbush(Hackenbush h) {
    this(new ArrayList<TintWeb2D>(h.getGroundNodes()),
         h.getLineStroke(),
         h.getDotStroke(),
         h.getInnerDotStroke());
  }

  /*//*/// METHODS

  /***
   * Update the manually generated connection library to account for the removal of an edge
   *
   * @param origin           The origin node
   * @param insertion        The node the origin node leads into
   */
  public void buildConnectionLibrary() {
    ArrayList<TintWeb2D> clib = new ArrayList<TintWeb2D>();
    ArrayList<EdgeTint> tlib = new ArrayList<EdgeTint>();
    for (TintWeb2D node : getGroundNodes()) {
      forwardTrace(node, clib, tlib, new ArrayList<TintWeb2D>());
    }
    //pruneDuplicates(clib, tlib);
    setConnectionLibrary(clib);
    setTintLibrary(tlib);
    calculateLongestLine();
    updateLoopArchive();
  }
  private void forwardTrace(TintWeb2D node, ArrayList<TintWeb2D> clib, ArrayList<EdgeTint> tlib, ArrayList<TintWeb2D> loopNodes) {
    if (node == null) return;
    ArrayList<TintWeb> nodes = node.getNodes();
    for (TintWeb next : nodes) {
      EdgeTint tint = node.getEdgeTints().get(nodes.indexOf((TintWeb2D)next));
      int dup = locateDuplate(node, (TintWeb2D)next, clib);
      if (dup != -1) continue;
      tlib.add(tint);
      clib.add(node);
      clib.add((TintWeb2D)next);
      System.out.println("TRACING IN TO " + next + " LOOP? " + node.getLoopNodes().size() +"|"+tint);
      if (next.isLooped()) {
        if (loopNodes.contains((TintWeb2D)next)) return;
        loopNodes.add((TintWeb2D)next);
      }
      forwardTrace((TintWeb2D)next, clib, tlib, loopNodes);
    }
  }
  private int locateDuplate(TintWeb2D a, TintWeb2D b, ArrayList<TintWeb2D> library) {
    for (int i=0; i<library.size(); i++) {
      if (a.equals(library.get(i))) {
        if (b.equals(library.get(i ^ 1))) return (i >> 1)*2;
      }
    }
    return -1;
  }

  /***
   * Update the loop node archive, which is required for quick ground tracing.
   *
   * @param origin           The origin node
   * @param insertion        The node the origin node leads into
   */
  public void  updateLoopArchive() {
    ArrayList<TintWeb2D> loopArch = getLoopArchive();
    int len = loopArch.size();
    for (int i=0; i<len; i++) {
      if (!loopArch.get(i).isLooped()) {
        loopArch.remove(i);
      }
    }
  }

  /***
   * Update the manually generated connection library to account for the removal of an edge
   *
   * @param origin           The origin node
   * @param insertion        The node the origin node leads into
   */
  public void updateConnectionLibrary(TintWeb2D origin, int index) {
    ArrayList<TintWeb2D> clib = getConnectionLibrary();
    for (int i=0; i<clib.size()-1; i+=2) {
      TintWeb2D first = clib.get(i);
      TintWeb2D last = clib.get(i+1);
      Boolean isFirst = first.equals(origin) && last.equals(first.getNode(index));
      Boolean isLast = last.equals(origin) && first.equals(last.getNode(index));
      if (isFirst || isLast) { // Xor would also be fine but im paranoid
        clib.remove(i);
        return;
      }
    }
    updateLoopArchive();
  }

  /***
   * Update the internal listing of ground nodes,
   */
  public void updateGroundNodes() {
    ArrayList<TintWeb2D> clib = getConnectionLibrary();
    if (clib.size() == 0) buildConnectionLibrary();
    ArrayList<TintWeb2D> gndNew = new ArrayList<TintWeb2D>();
    for (TintWeb2D node : clib) {
      if (node.isGroundNode() && !gndNew.contains(node)) {
        gndNew.add(node);
      }
    }
    setGroundNodes(gndNew);
  }

  /***
   * Draws a hackenbush tree on a Graphics2D object.
   *
   * @param gfx          The graphics object to draw to
   */
  public void draw(Graphics2D gfx) {
    ArrayList<TintWeb2D> vlib = getConnectionLibrary();
    ArrayList<EdgeTint> tlib = getTintLibrary();

    for (int i=0; i<vlib.size()-1; i+=2) {
      Vector<Integer> p1 = vlib.get(i).getPosition();
      Vector<Integer> p2 = vlib.get(i+1).getPosition();

      // Draw the line with the appropriate color
      gfx.setStroke(getLineStroke());
      gfx.setColor(tintMap.get(tlib.get(i / 2)));
      gfx.drawLine(p1.get(0),p1.get(1),p2.get(0),p2.get(1));

      // Draw the endpoints of the line
      gfx.setStroke(getDotStroke());
      gfx.setColor(getDotColor());
      gfx.drawLine(p1.get(0),p1.get(1),p1.get(0),p1.get(1));
      gfx.drawLine(p2.get(0),p2.get(1),p2.get(0),p2.get(1));

      // Draw the inner portion of each line
      gfx.setStroke(getInnerDotStroke());
      gfx.setColor(getInnerDotColor());
      gfx.drawLine(p1.get(0),p1.get(1),p1.get(0),p1.get(1));
      gfx.drawLine(p2.get(0),p2.get(1),p2.get(0),p2.get(1));
    }
  }

  /***
   * Checks the distance between a position and the current hackenbush picture.
   * Returns the index of the collided line or -1 if no collision occured.
   *
   * @param position          The position to check
   * @param threshold         The threshold squared distance before a line is considered clicked
   * @return                  The index of the collided line or -1 if no collision occured
   */
  public Integer collide(Vector<Integer> position, Integer threshold) {
    int sqrtThreshold = (int)Math.sqrt(threshold);
    int xp = position.get(0);
    int yp = position.get(1);
    int distClosest = Integer.MAX_VALUE;
    int actualClosest = distClosest;
    int closest = -1;
    int longest = getLongestLine();
    ArrayList<TintWeb2D> vlib = getConnectionLibrary();
    for (int i=0; i<vlib.size()-1; i+=2) {
      Vector<Integer> p1 = vlib.get(i).getPosition();
      int y1 = p1.get(1);
      if (Math.abs(y1 - yp) <= longest && Math.abs(p1.get(0) - xp) <= longest) {
        Vector<Integer> p2 = vlib.get(i+1).getPosition();
        int y2 = p2.get(1);
        // NOTE: The way I did this removes the threshold from top and bottom of the line at the end points :P....

        // Calculate the distance to the line of interest, if the point is also within x1 and x2, its good!
        int a = squaredDistanceToLine(xp,yp, p1.get(0),y1, p2.get(0),y2);
        if (a<actualClosest) actualClosest=a;
        if (y1 < y2) sqrtThreshold = -sqrtThreshold;
        if (a <= threshold) {
          if (a < distClosest) {
            distClosest = a;
            closest = i;
          }
        }
      }
    }
    if (distClosest > threshold) {
      closest = -1; // Better to just check once down here
    }
    return closest;
  }
  private int squaredDistanceToLine(int x, int y, int x1, int y1, int x2, int y2) {
    int a = x - x1;
    int b = y - y1;
    int c = x2 - x1;
    int d = y2 - y1;
    int e = -d;
    int f = c;
    int dot = a*e + b*f;
    int len_sq = e*e + f*f;
    return dot * dot / len_sq;
  }
  private int distanceToPoint(int x, int y, int xp, int yp) {
    int a = x-xp;
    int b = y-yp;
    return (int)Math.sqrt(a*a + b*b);
  }

  /***
   * Calculate the internal length of the longest line in the hackenbush picture
   */
  public void calculateLongestLine() {
    int longest = 0;
    ArrayList<TintWeb2D> clib = getConnectionLibrary();
    for (int i=0; i<clib.size()-1; i+=2) {
      Vector<Integer> p1 = clib.get(i).getPosition();
      Vector<Integer> p2 = clib.get(i+1).getPosition();
      int next = distanceToPoint(p1.get(0),p1.get(1), p2.get(0),p2.get(1));
      if (next > longest) {
        longest = next;
      }
    }
    setLongestLine(longest);
  }

  /***
   * Determine the WinningWays value of the current hackenbush picture
   */
  public double evaluate() {
    HackenbushTint color = calculateTint();
    switch(color) {
      case BLUE:
        return (double)(getConnectionLibrary().size()) / 2.0;
      case RED:
        return -(double)(getConnectionLibrary().size()) / 2.0;
      case GREEN:
        // NOT IMPLEMENTED
        break;
      case REDBLUE:
        return evaluateRedBlue(this);
      default:
        return 0;
    }
    return 0.0;
  }
  private double evaluateRedBlue(Hackenbush redBlue) {
    int loops = redBlue.getLoopArchive().size();

    System.out.println("EVALUATING PICTURE");

    // Check if the game is a dyadic fraction
    ArrayList<TintWeb2D> gnd = getGroundNodes();
    ArrayList<TintWeb> firstNodes = gnd.get(0).getNodes();
    if (gnd.size() == 1) {
      int fnLen = firstNodes.size();
      if (fnLen == 0) return 0.0;
      if (fnLen == 1) {
        double multiplier = 0.5;
        TintWeb firstNode = firstNodes.get(0);
        EdgeTint col = gnd.get(0).getEdgeTints().get(0);
        double potentialValue = (col == EdgeTint.BLUE) ? 1.0 : -1.0;
        while (true) {
          firstNodes = firstNode.getNodes();
          fnLen = firstNodes.size();
          if (fnLen != 1) break;
          col = firstNode.getEdgeTints().get(0);
          potentialValue += (col == EdgeTint.BLUE) ? multiplier : -multiplier;
          System.out.println("ADDING " +((col == EdgeTint.BLUE) ? multiplier : -multiplier)+" TO THE EVAL");
          multiplier /= 2.0;
          firstNode = firstNodes.get(0);
        }
        if (fnLen == 0) return potentialValue;
      }
    }

    return 0.0;
    //ArrayList<EdgeTint> tlib = getTintLibrary();
  //  if (loops == 0)
  }
  private HackenbushTint calculateTint() {
    Boolean hasRed = false;
    for (TintWeb2D node : getConnectionLibrary()) {
      if (node.getEdgeTints().contains(EdgeTint.RED)) hasRed = true;
      if (hasRed && node.getEdgeTints().contains(EdgeTint.BLUE)) return HackenbushTint.REDBLUE;
      if (node.getEdgeTints().contains(EdgeTint.GREEN)) return HackenbushTint.GREEN;
    }
    return (hasRed) ? HackenbushTint.RED : HackenbushTint.BLUE;
  }

  /*//*/// GET &* SET

  /** Get all of the ground nodes associated with this picture @return Array of ground nodes*/
  public ArrayList<TintWeb2D> getGroundNodes() {return groundNodes;}
  /** Get all of the loop nodes associated with this picture @return Array of loop nodes*/
  public ArrayList<TintWeb2D> getLoopArchive() {return loopArchive;}
  /** Get array containing every connection in the picture by way of pairs of TintWeb2D. @return Array of TintWeb2D connections*/
  public ArrayList<TintWeb2D> getConnectionLibrary() {return connectionLibrary;}
  /** Get array of all tints in this picture, indicies correspond to connection library @return Array of edge tints*/
  public ArrayList<EdgeTint> getTintLibrary() {return tintLibrary;}
  /** Get the size of the longest line in the picture @return The longest line*/
  public Integer getLongestLine() {return longestLine;}
  /** Get the BasicStroke used for drawing the picture edges @return BasicStroke used for edges*/
  public BasicStroke getLineStroke() {return lineStroke;}
  /** Get the BasicStroke used for drawing the outline of the dots in the picture @return BasicStroke used for dots*/
  public BasicStroke getDotStroke() {return dotStroke;}
  /** Get the BasicStroke used for drawing the inside of the dots in the picture @return BasicStroke used for the inner dots*/
  public BasicStroke getInnerDotStroke() {return innerDotStroke;}
  /** Get the color of the dots/dot outlines used in the picture @return Color of the dots*/
  public Color getDotColor() {return dotColor;}
  /** Get the color of the inner dots used in the picture @return Color of the inner dots*/
  public Color getInnerDotColor() {return innerDotColor;}

  /** Set the stroke associated with lines in the picture @param stroke BasicStroke used for edges*/
  public void setLineStroke(BasicStroke stroke) {this.lineStroke = stroke;}
  /** Set the stroke associated with dots in the picture @param stroke BasicStroke used for dots*/
  public void setDotStroke(BasicStroke stroke) {this.dotStroke = stroke;}
  /** Set the stroke associated with inner dots in the picture @param stroke BasicStroke used for the inner dots*/
  public void setInnerDotStroke(BasicStroke stroke) {this.innerDotStroke = stroke;}
  /** Set the outline color of dots in the picture @param color Outline color*/
  public void setDotColor(Color color) {this.dotColor = color;}
  /** Set the inside color of dots in the picture @param color Inside color*/
  public void setInnerDotColor(Color color) {this.innerDotColor = color;}

  public void setGroundNodes(ArrayList<TintWeb2D> groundNodes) {this.groundNodes = groundNodes;}
  private void setLoopArchive(ArrayList<TintWeb2D> loopArchive) {this.loopArchive = loopArchive;}
  private void setConnectionLibrary(ArrayList<TintWeb2D> connectionLibrary) {this.connectionLibrary = connectionLibrary;}
  private void setTintLibrary(ArrayList<EdgeTint> tintLibrary) {this.tintLibrary = tintLibrary;}
  private void setLongestLine(Integer length) {this.longestLine = length;}
}
