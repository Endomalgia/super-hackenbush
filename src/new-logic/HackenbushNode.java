
import java.util.*;

enum EdgeTint {
	RED,
	BLUE,
	GREEN
}

class HackenbushNode extends TintDelta<EdgeTint>{


  /*//*/// VARIABLES

  Vector<Integer> position;

  /*//*/// CONSTRUCTORS

  /***
   * No arg Constructor for a new HackenbushNode
   */
  public HackenbushNode() {
    this(false, 0, 0);
  }

  /***
   * Copy constructor for a new HackenbushNode
   *
   * @param node          Node to copy from
   */
  public HackenbushNode(HackenbushNode node) {
    super(node);
    setPosition(new Vector<Integer>(node.getPosition()));
  }

  /***
   * Constructor for a new HackenbushNode
   *
   * @param x             relative x-axis position
   * @param y             relative y-axis position
   */
  public HackenbushNode(Integer x, Integer y) {
    this(false, x, y);
  }

  /***
   * Constructor for a new HackenbushNode
   *
   * @param isGroundNode  is this node going to be a ground node
   */
  public HackenbushNode(Boolean isGroundNode) {
    this(isGroundNode, 0, 0);
  }

  /***
   * Constructor for a new HackenbushNode which sets isGroundNode
   *
   * @param isGroundNode  is this node going to be a ground node
   * @param x             relative x-axis position
   * @param y             relative y-axis position
   */
  public HackenbushNode(Boolean isGroundNode, Integer x, Integer y) {
    super(isGroundNode);
    position = new Vector<Integer>(Arrays.asList(x, y));
  }

  /*//*/// METHODS

  /***
   * Set the position of a HackenbushNode mode without passing in a new vector
   *
   * @param x          relative x-axis position
   * @param y          relative y-axis position
   */
  public void setPosition(Integer x, Integer y) {
    position.set(0, x);
    position.set(1, y);
  }

  /*//*/// GET &* SET

  /***
   * Get the position of a HackenbushNode node
   *
   * @return a vector containing the x and y positions of the node
   */
  public Vector<Integer> getPosition() {return this.position;}

  /***
   * Set the position of a HackenbushNode node
   *
   * @param position  a vector containing the x and y positions of the node
   */
  public void setPosition(Vector<Integer> position) {this.position = position;}

}