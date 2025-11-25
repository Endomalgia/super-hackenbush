import java.util.*;

/**
*
* Data structure modeled off of a river delta. There are root nodes and loop nodes (nodes which run upstream into a node), 
* otherwise everything flows downstream to terminal nodes.
* 
*/
class TintDelta<T> {

  /*//*/// VARIABLES

  private ArrayList<TintDelta<T>> nodes;       // Array of all nodes leading from this node
  private ArrayList<T> Tints;  // Tint of the corresponding outgoing node
  private ArrayList<TintDelta<T>> loopNodes;   // Number of non-previous nodes leading into this node
  private TintDelta<T> previous;               // Immediately
  private Boolean isGroundNode;           // Is this node on the ground (sourceless)?
  private Boolean isLooped;               // Does this node have non-previous incoming nodes

  /*//*/// CONSTRUCTORS

  /***
   * Constructor for a new TintDelta<T>
   *
   * @param isGroundNode   is this node going to be a ground node
   */
  public TintDelta(Boolean isGroundNode) {
    setNodes(new ArrayList<TintDelta<T>>());
    setTints(new ArrayList<T>());
    setLoopNodes(new ArrayList<TintDelta<T>>());
    setPrevious(null);
    setLooped(false);
    setGroundNode(isGroundNode);
  }

  /***
   * No arg Constructor for a new TintDelta<T>. Sets isGroundNode to false;
   */
  public TintDelta() {
    this(false);
  }

  /***
   * Copy constructor for a new TintDelta<T>. Internal ArrayLists are new but connecting nodes (loop, previous, etc....) are identical to the input node.
   *
   * @param node   Node to copy from
   */
  public TintDelta(TintDelta<T> node) {
    this(node.isGroundNode()); // Why is this here?
    setPrevious(node.getPrevious());
    setNodes(new ArrayList<TintDelta<T>>(node.getNodes()));
    setTints(new ArrayList<T>(node.getTints()));
    setLoopNodes(new ArrayList<TintDelta<T>>(node.getLoopNodes()));
    setLooped(node.isLooped());
  }

  /*//*/// METHODS

  /***
   * Add a node after the current node with an edge tint between them.
   *
   * @param node      The node to be added
   * @param Tint  Tint value of the edge between the two nodes
   */
  public void addNode(TintDelta<T> node, T Tint) {
    node.setPrevious(this);
    getNodes().add(node);
    getTints().add(Tint);
  }

  /***
   * Get the node connected to the current node by its index. Returns null if not present.
   *
   * @param index     The index of the child node
   * @return          The child node
   */
  public TintDelta<T> getNode(int index) {
    ArrayList<TintDelta<T>> nodes = getNodes();
    if (nodes.size() <= index) return null;
    return nodes.get(index);
  }

  /***
   * Get the loop node connected to the current node by its index. Returns null if not present.
   *
   * @param index     The index of the node merging into the loop node
   * @return          The node merging into the current loop node
   */
  public TintDelta<T> getLoopNode(int index) {
    ArrayList<TintDelta<T>> loopNodes = getLoopNodes();
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
   * @param Tint  Tint value of the edge between the two nodes
   */
  public void linkNode(TintDelta<T> node, T Tint) {
    getNodes().add(node);
    getTints().add(Tint);
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
  public void removeEdge(int index) {
    ArrayList<TintDelta<T>> nodes = getNodes();
    if (nodes.size() <= index) return;
    // Remove the orphaned nodes connection
    TintDelta<T> orphan = nodes.get(index);
    nodes.remove(index);
    getTints().remove(index);

    // If the orphan end is a loop node, remove it and continue, nothing is going to fall off here
    if (orphan.isLooped()) {
      ArrayList<TintDelta<T>> loopNodes = orphan.getLoopNodes();
      int ti = loopNodes.indexOf(this);
      if (ti != -1) {
        loopNodes.remove(ti);
        if (loopNodes.size() == 0) orphan.setLooped(false);
        return;
      }

      if (orphan.getPrevious().equals(this)) {
        TintDelta<T> loop = loopNodes.remove(0);
        orphan.setPrevious(loop);
        if (loopNodes.size() == 0) orphan.setLooped(false);
        return;

      }
    }

    orphan.setPrevious(null);
    findAndRefactorFromLoop(orphan);
  }
  private Boolean findAndRefactorFromLoop(TintDelta<T> origin) { // Recursively determine the identity of the loop node
    for (TintDelta<T> node : origin.getNodes()) {
      if (node.isLooped()) {
        refactorFromLoop(node, origin);
        return true;
      }
      if (findAndRefactorFromLoop(node)) {
        return true;
      }
    }
    return false;
  }
  private void refactorFromLoop(TintDelta<T> origin, TintDelta<T> last) {
    if (!origin.isLooped()) return;
    ArrayList<TintDelta<T>> loopNodes = origin.getLoopNodes();

    // Rearange the loop node to have one less loop (to pay for the loose end)
    TintDelta<T> prev = origin.getPrevious();
    if (prev.equals(last)) {
      TintDelta<T> loopIn = loopNodes.remove(0);
      origin.setPrevious(loopIn);
    } else {
      prev = loopNodes.remove(loopNodes.indexOf(last));
    }

    // If there arent any loops left set isLooped to false
    if (loopNodes.size() == 0) origin.setLooped(false);

    // Reorder every loop back untill the free end
    origin.getNodes().add(prev);
    TintDelta<T> prevPrev = origin;
    while (prev != null) {
      TintDelta<T> nextPrev = prev.getPrevious();
      prev.setPrevious(prevPrev);

      ArrayList<TintDelta<T>> prevNodes = prev.getNodes();
      ArrayList<T> prevTints = prev.getTints();
      int pIndex = prevNodes.indexOf(prevPrev);
      prevNodes.remove(pIndex);
      prevPrev.getTints().add(prevTints.remove(pIndex));

      if (nextPrev == null) {
        prevNodes = new ArrayList<TintDelta<T>>();
        break;
      }
      prevNodes.add(nextPrev);

      prevPrev = prev;
      prev = nextPrev;
    }
  }

  /***
   * Generate a string representitive of the current TintDelta<T> node.
   *
   * @return          String representing the node
   */
  public String toString() {
    String out = "[" + String.format("0x%08X",hashCode());
    if (isLooped()) out += "*";
    if (isGroundNode()) out += "(G)";
    ArrayList<T> tints = getTints();
    if (tints.size() == 0) return out + "]";
    out += " ->";
    for (T tint : tints) {
      out += " " + tint;
    }
    return out + "]";
  }

  /*//*/// GET &* SET

  /** Check if the current node is a ground node @return boolean representing the result*/
  public Boolean isGroundNode() {return this.isGroundNode;}
  /** Check if the current node is a loop node (Does it have non-previous nodes connecting into it). @return boolean representing the result*/
  public Boolean isLooped() {return this.isLooped;}
  /** Get the nodes connected downstream from this node @return Array of downstream nodes*/
  public ArrayList<TintDelta<T>> getNodes() {return this.nodes;}
  /** Get the tine of the edges connected downstream from this node @return Array of downstream edge tints*/
  public ArrayList<T> getTints() {return this.Tints;}
  /** Get the nodes connecting in upstream from this node which arent previous nodes. @return Array of 'looping in' nodes*/
  public ArrayList<TintDelta<T>> getLoopNodes() {return this.loopNodes;}
  /** Get the node immediately previous to this node @return The previous node*/
  public TintDelta<T> getPrevious() {return this.previous;}

  /** Set wheather this is a ground node @param isGroundNode is this a ground node*/
  public void setGroundNode(Boolean isGroundNode) {this.isGroundNode = isGroundNode;}
  /** Set wheather this is a loop node @param isLooped is this a looped node (does it have nodes looping into it from upstream)*/
  public void setLooped(Boolean isLooped) {this.isLooped = isLooped;}
  /** Set the node immediately previous to this node @param The node to set previous to*/
  public void setPrevious(TintDelta<T> previous) {this.previous = previous;}

  private void setNodes(ArrayList<TintDelta<T>> nodes) {this.nodes = nodes;}
  private void setTints(ArrayList<T> Tints) {this.Tints = Tints;}
  private void setLoopNodes(ArrayList<TintDelta<T>> loopNodes) {this.loopNodes = loopNodes;}

}