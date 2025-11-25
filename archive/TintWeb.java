import java.util.*;

/*
  A more generic tintweb implementation would use a generic instead of this enum
  but this is easier to read in my opinion so im keeping it this way.
*/

/*
enum EdgeTint {
  GREEN,
  RED,
  BLUE
}
*/

// TintWeb
class TintWeb {

  /*//*/// VARIABLES

  private ArrayList<TintWeb> nodes;       // Array of all nodes leading from this node
  private ArrayList<EdgeTint> edgeTints;  // Tint of the corresponding outgoing node
  private ArrayList<TintWeb> loopNodes;   // Number of non-previous nodes leading into this node
  private TintWeb previous;               // Immediately
  private Boolean isGroundNode;           // Is this node on the ground (sourceless)?
  private Boolean isLooped;               // Does this node have non-previous incoming nodes

  /*//*/// CONSTRUCTORS

  /***
   * Constructor for a new TintWeb
   *
   * @param isGroundNode   is this node going to be a ground node
   */
  public TintWeb(Boolean isGroundNode) {
    setNodes(new ArrayList<TintWeb>());
    setEdgeTints(new ArrayList<EdgeTint>());
    setLoopNodes(new ArrayList<TintWeb>());
    setPrevious(null);
    setLooped(false);
    setGroundNode(isGroundNode);
  }

  /***
   * No arg Constructor for a new TintWeb. Sets isGroundNode to false;
   */
  public TintWeb() {
    this(false);
  }

  /***
   * Copy constructor for a new TintWeb
   *
   * @param node   Node to copy from
   */
  public TintWeb(TintWeb node) {
    this(node.isGroundNode()); // Why is this here?
    setPrevious(node.getPrevious());
    setNodes(node.getNodes());
    setLoopNodes(node.getLoopNodes());
    setLooped(node.isLooped());
  }

  /*//*/// METHODS

  /***
   * Add a node after the current node with an edge tint between them.
   *
   * @param node      The node to be added
   * @param edgeTint  EdgeTint value of the edge between the two nodes
   */
  public void addNode(TintWeb node, EdgeTint edgeTint) {
    node.setPrevious(this);
    getNodes().add(node);
    getEdgeTints().add(edgeTint);
  }

  /***
   * Get the node connected to the current node by its index. Returns null if not present.
   *
   * @param index     The index of the child node
   * @return          The child node
   */
  public TintWeb getNode(int index) {
    ArrayList<TintWeb> nodes = getNodes();
    if (nodes.size() <= index) return null;
    return nodes.get(index);
  }

  /***
   * Get the loop node connected to the current node by its index. Returns null if not present.
   *
   * @param index     The index of the node merging into the loop node
   * @return          The node merging into the current loop node
   */
  public TintWeb getLoopNode(int index) {
    ArrayList<TintWeb> loopNodes = getLoopNodes();
    if (loopNodes.size() <= index) return null;
    return loopNodes.get(index);
  }

  /***
   * Get the number of connected nodes;
   *
   * @return          The number of outgoing (nonloop) nodes connected to the current node
   */
  public int getNumberNodes() {
    return getNodes().size();
  }

  /***
   * Create a loop between the current node and node with an edge tint between them
   * and returns the orphaned branch if not connected to anything
   *
   * @param node      The node to be connected to
   * @param edgeTint  EdgeTint value of the edge between the two nodes
   */
  public void linkNode(TintWeb node, EdgeTint edgeTint) {
    getNodes().add(node);
    getEdgeTints().add(edgeTint);
    node.getLoopNodes().add(this);
    node.setLooped(true);
  }

  /***
   * Delete a connection between two nodes and return either the orphaned node or
   * the ground nodes at the root of the isolated web.
   *
   * @param index          The index associated with the connection between the nodes
   * @return               Array containing either all ground nodes acessable by the orphaned node
   */
  public ArrayList<TintWeb> quickRemoveEdge(int index) {
    ArrayList<TintWeb> nodes = getNodes();
    System.out.println("hllo?");
    if (nodes.size() <= index) return null;
    // Remove the orphaned nodes connection
    TintWeb orphan = nodes.get(index);
    nodes.remove(index);
    getEdgeTints().remove(index);

    System.out.println("CURRENT CAPPED END IS "+ this);
    System.out.println("CURRENT ORPHAN IS "+ orphan);
    System.out.println("IS LOOOPED : " + isLooped());

    // If the orphan end is a loop node, remove it and continue, nothing is going to fall off here
    if (orphan.isLooped()) {
      System.out.println("Removing orphan loop....");
      ArrayList<TintWeb> loopNodes = orphan.getLoopNodes();
      int ti = loopNodes.indexOf(this);
      System.out.println("TI = " + ti);
      if (ti != -1) {
        loopNodes.remove(ti);
        if (loopNodes.size() == 0) orphan.setLooped(false);
        return null;
      }

      if (orphan.getPrevious().equals(this)) {
        TintWeb loop = loopNodes.remove(0);
        orphan.setPrevious(loop);
        if (loopNodes.size() == 0) orphan.setLooped(false);
        return null;

      }
    }
    orphan.setPrevious(null);
    System.out.println("HELLO2763");
    if (!findAndRefactorFromLoop(orphan)) {}
    if (orphan.getNumberNodes() == 0) return null;
    return new ArrayList<TintWeb>(Arrays.asList(orphan));
  }
  private Boolean findAndRefactorFromLoop(TintWeb origin) { // Recursively determine the identity of the loop node
    for (TintWeb node : origin.getNodes()) {
      if (node.isLooped()) {
        System.out.println("FOUND LOOP NDE AT "+ node);
        refactorFromLoop(node, origin);
        return true;
      }
      if (findAndRefactorFromLoop(node)) {
        return true;
      }
    }
    return false;
  }
  private void refactorFromLoop(TintWeb origin, TintWeb last) {
    System.out.println("START REFACTORING, ORIGIN HAS "+origin.getLoopNodes().size()+" LOOP NODES");
    if (!origin.isLooped()) return;
    ArrayList<TintWeb> loopNodes = origin.getLoopNodes();

    // Rearange the loop node to have one less loop (to pay for the loose end)
    TintWeb prev = origin.getPrevious();
    System.out.println("\tREARANGING LOOP : "+prev+":"+origin+":"+last);
    if (prev.equals(last)) {
      TintWeb loopIn = loopNodes.remove(0);
      origin.setPrevious(loopIn);
    } else {
      prev = loopNodes.remove(loopNodes.indexOf(last));
    }

    // If there arent any loops left set isLooped to false
    if (loopNodes.size() == 0) origin.setLooped(false);

    System.out.println("\tREORDERING LOOSE ENDS, ORIGIN NOW HAS "+origin.getLoopNodes().size()+" LOOP NODES > "+origin.isLooped() );
    // Reorder every loop back untill the free end
    origin.getNodes().add(prev);
    TintWeb prevPrev = origin;
    while (prev != null) {
      TintWeb nextPrev = prev.getPrevious();
      prev.setPrevious(prevPrev);

      ArrayList<TintWeb> prevNodes = prev.getNodes();
      ArrayList<EdgeTint> prevTints = prev.getEdgeTints();
      int pIndex = prevNodes.indexOf(prevPrev);
      System.out.println("pIndex = "+ pIndex);
      prevNodes.remove(pIndex);
      prevPrev.getEdgeTints().add(prevTints.remove(pIndex));

      if (nextPrev == null) {
        prevNodes = new ArrayList<TintWeb>();
        break;
      }
      prevNodes.add(nextPrev);

      prevPrev = prev;
      prev = nextPrev;
    }
  }

  /***
   * Generate a string representitive of the current TintWeb node.
   *
   * @return          String representing the node
   */
  public String toString() {
    return toString(0);
  }
  private String toString(int depth) {
    int prevHash = (getPrevious() == null) ? -1 : getPrevious().hashCode();
    String hashSig = "["+String.format("%x",prevHash)+"->"+String.format("%x",hashCode())+"]";
    String base = "{GND:"+isGroundNode()+"," + hashSig;

    ArrayList<TintWeb> nodes = getNodes();
    ArrayList<EdgeTint> tints = getEdgeTints();
    int len = nodes.size(); // used alot here
    if (len == 0) return base + "}";

    base+=",";
    if (len != tints.size()) return "DISCONNECT ERROR";
    for (int i=0;i<len;i++) {
      TintWeb node = nodes.get(i);
      if (node.isLooped()) {
        base += "{"+tints.get(i)+"["+String.format("%x",getPrevious().hashCode())+"->"+String.format("%x",hashCode())+"]GND:"+isGroundNode()+", ... }";
      }
      base += "\n" + "\t".repeat(depth+1) + node.toString(depth+1);
    }
    return base + "}";
  }

  /*//*/// GET &* SET

  /** Check if the current node is a ground node @return boolean representing the result*/
  public Boolean isGroundNode() {return this.isGroundNode;}
  /** Check if the current node is a loop node (Does it have non-previous nodes connecting into it). @return boolean representing the result*/
  public Boolean isLooped() {return this.isLooped;}
  /** Get the nodes connected downstream from this node @return Array of downstream nodes*/
  public ArrayList<TintWeb> getNodes() {return this.nodes;}
  /** Get the tine of the edges connected downstream from this node @return Array of downstream edge tints*/
  public ArrayList<EdgeTint> getEdgeTints() {return this.edgeTints;}
  /** Get the nodes connecting in upstream from this node which arent previous nodes. @return Array of 'looping in' nodes*/
  public ArrayList<TintWeb> getLoopNodes() {return this.loopNodes;}
  /** Get the node immediately previous to this node @return The previous node*/
  public TintWeb getPrevious() {return this.previous;}

  /** Set wheather this is a ground node @param isGroundNode is this a ground node*/
  public void setGroundNode(Boolean isGroundNode) {this.isGroundNode = isGroundNode;}
  /** Set wheather this is a loop node @param isLooped is this a looped node (does it have nodes looping into it from upstream)*/
  public void setLooped(Boolean isLooped) {this.isLooped = isLooped;}

  private void setNodes(ArrayList<TintWeb> nodes) {this.nodes = nodes;}
  private void setEdgeTints(ArrayList<EdgeTint> edgeTints) {this.edgeTints = edgeTints;}
  private void setLoopNodes(ArrayList<TintWeb> loopNodes) {this.loopNodes = loopNodes;}
  private void setPrevious(TintWeb previous) {this.previous = previous;}

}

/* SHORTHAND NOTATION KEEPING FOR ARCHIVING PURPOSES
  0       > green
  +       > blue
  -       > red
  [a-z]   > identifier, allows loops & related connections
  ()      > establishes hierarchy
  [x,y]   > establishes location

  +a[0,0](+[-1,1](-[-0.5,2.5]0[0.5,2.5]+[0,2](+a[1,1]))) = a blue apple with a red stem and green leaf
*/
