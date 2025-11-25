import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.AffineTransform;

class FallingLeaf extends Hackenbush  {
  private TintWeb2D root;
  private float fallingLeafGravity;       // px / sec
  private float fallingLeafOpacityDelta;  // Opacity units / sec
  private AlphaComposite alphaComposite;
  private Boolean isActive;
  private float time;

  /***
   * Creates a new FallingLeaf
   *
   * @param source     The source hackenbush tree
   * @param root       The orphaned node at the start of the leaf
   * @param g          Little g constant for gravity
   * @param dopacity   Rate of change in opacity over time
   */
  public FallingLeaf(Hackenbush source, TintWeb2D root, float g, float dopacity) {
    super(source);
    super.setGroundNodes(new ArrayList<TintWeb2D>(Arrays.asList(root)));
    root.setGroundNode(true); // Make the root into a fake ground node
    setRoot(root);
    setFallingLeafGravity(g);
    setFallingLeafOpacityDelta(dopacity);
    setAlphaComposite(AlphaComposite.SrcOver);
    setActive(false);
    setTime(0.0f);
  }

  /***
   * Draws the leaf with the appropriate transforms and opacity to a graphics object
   *
   * @param gfx   The Graphics2D object to draw to
   */
  public void draw(Graphics2D gfx) {
    AffineTransform mat_transform = new AffineTransform();
    mat_transform.translate(0.0, getTime()* getTime()* getFallingLeafGravity());
    mat_transform.scale(1.0, 1.0); // SET TO TWO ON MACOS FOR SOME B
    gfx.setTransform(mat_transform);

    float tf = 1.0f / getFallingLeafOpacityDelta();
    float alpha = Math.max(0.0f, Math.min(1.0f, 1.0f - (getTime() * getFallingLeafOpacityDelta())));
    gfx.setComposite(alphaComposite.derive(alpha));

    super.draw(gfx);
  }

  /** Get the root node. @return root node*/
  public TintWeb2D getRoot() {return this.root;}
  /** Get little g for the leaves. @return g*/
  public float getFallingLeafGravity() {return this.fallingLeafGravity;}
  /** Get the rate of change of opacity over time. @return delta opacity*/
  public float getFallingLeafOpacityDelta() {return this.fallingLeafOpacityDelta;}
  /** Get AlphaComposite object associated with changing the opacity of the leaves as they fall. @return AlphaComposite object*/
  public AlphaComposite getAlphaComposite() {return this.alphaComposite;}
  /** Get little current simulation time associated with leaf falling. @return time*/
  public float getTime() {return this.time;}
  /** Check if the current leaf is falling(active) @return Boolean representing the result*/
  public Boolean isActive() {return this.isActive;}

  /** Set the root node of the leaf. @param root The root node*/
  public void setRoot(TintWeb2D root) {this.root = root;}
  /** Set little g for the leaf. @param g little g*/
  public void setFallingLeafGravity(float g) {this.fallingLeafGravity = g;}
  /** Set the rate of opacity change for the leaf. @param dopacity Rate of change in opacity.*/
  public void setFallingLeafOpacityDelta(float dopacity) {this.fallingLeafOpacityDelta = dopacity;}
  /** Set the AlphaComposite object used for changing leaf opacity. @param composite AlphaComposite object*/
  public void setAlphaComposite(AlphaComposite composite) {this.alphaComposite = composite;}
  /** Set the activity state of the current leaf (is it falling) @param isActive Is the leaf actively falling?*/
  public void setActive(Boolean isActive) {this.isActive = isActive;}
  /** Set the current simulation time. @param time The new simulation time.*/
  public void setTime(float time) {this.time = time;}
}
