
import java.util.*;

class TintWeb2D extends TintWeb {

  /*//*/// VARIABLES

  Vector<Integer> position;

  /*//*/// CONSTRUCTORS

  /***
   * No arg Constructor for a new TintWeb2D
   */
  public TintWeb2D() {
    this(false, 0, 0);
  }

  /***
   * Constructor for a new TintWeb2D
   *
   * @param x             relative x-axis position
   * @param y             relative y-axis position
   */
  public TintWeb2D(Integer x, Integer y) {
    this(false, x, y);
  }

  /***
   * Constructor for a new TintWeb2D which sets isGroundNode
   *
   * @param isGroundNode  is this node going to be a ground node
   * @param x             relative x-axis position
   * @param y             relative y-axis position
   */
  public TintWeb2D(Boolean isGroundNode, Integer x, Integer y) {
    super(isGroundNode);
    position = new Vector<Integer>(Arrays.asList(x, y));
  }

  /*//*/// METHODS

  /***
   * Set the position of a TintWeb2D mode without passing in a new vector
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
   * Get the position of a TintWeb2D node
   *
   * @return a vector containing the x and y positions of the node
   */
  public Vector<Integer> getPosition() {return this.position;}

  /***
   * Set the position of a TintWeb2D node
   *
   * @param position  a vector containing the x and y positions of the node
   */
  public void setPosition(Vector<Integer> position) {this.position = position;}

}
