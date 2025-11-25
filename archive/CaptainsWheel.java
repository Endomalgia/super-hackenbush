import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.AffineTransform;

public class CaptainsWheel extends JPanel {

  private static float SQRT_TWO_DECIMAL = 0.41421356f;

  private static Color c_wheel = new Color(120, 120, 120);

  public double time = 0;
  public int xpos = 0;
  public int ypos = 0;
  public double rot = 0;

  private int wheel_width;
  private Rectangle wheel_bounds;

  private Polygon shape;

  public CaptainsWheel(int wheel_width, Rectangle wheel_bounds) {
    this.setWheelWidth(wheel_width);
    this.setWheelBounds(wheel_bounds);
    this.xpos = wheel_bounds.width / 2;
    this.xpos = wheel_bounds.height / 2;
    recalculateMesh();
  }

  public void paintComponent(Graphics gfx) {
    //super.paintComponent(gfx);

    AffineTransform mat_transform = new AffineTransform();
    mat_transform.translate(xpos*2, ypos*2);
    mat_transform.rotate(easeInOutBack((time / 2000.0) % 1.0) * 50.0, 0.0, 0.0);
    mat_transform.scale(2,2);
    ((Graphics2D)gfx).setTransform(mat_transform);

    gfx.setColor(c_wheel);
    gfx.fillPolygon(this.getShape());
  }

  public void animate() {
    while(true) {
      time++;

      int ww = (getSize()).width;
      int hh = (getSize()).height;
      double offset = (double)ww / 2;
      double r = ((double)ww /2) - 50.0;
      double offset2 = (double)hh / 2;
      double r2 = ((double)hh /2) - 50.0;

      xpos = (int)(Math.cos(time / 100.0)*r + offset);
      ypos = (int)(Math.sin(2*time / 100.0)*r2 + offset2);

      try {Thread.sleep(10);
      } catch (Exception e) {
        System.out.println("Error");
      }

      invalidate();
      repaint();
    }
  }

  public Dimension getPreferredSize() {
    return new Dimension(getWheelBounds().width,getWheelBounds().height);
  }

  public void recalculateMesh() {
    int qw = this.getWheelBounds().width;
    int qh = this.getWheelBounds().height;
    int qwi = qw - 2*this.getWheelWidth();
    int qhi = qh - 2*this.getWheelWidth();

    int sq_qw = (int)(SQRT_TWO_DECIMAL*(float)qw);
    int sq_qwi = (int)(SQRT_TWO_DECIMAL*(float)qwi);
    int[] xpts = {
      -sq_qw,  sq_qw,  qw,  qw,
       sq_qw, -sq_qw, -qw, -qw,     -sq_qw,
      -sq_qwi, -qwi, -qwi, -sq_qwi,
       sq_qwi,  qwi,  qwi,  sq_qwi, -sq_qwi};

    int sq_qh = (int)(SQRT_TWO_DECIMAL*(float)qh);
    int sq_qhi = (int)(SQRT_TWO_DECIMAL*(float)qhi);
    int[] ypts = {
      qh, qh,  sq_qh, -sq_qh,
     -qh,-qh, -sq_qh,  sq_qh,       qh,
      qhi,  sq_qhi, -sq_qhi, -qhi,
     -qhi, -sq_qhi,  sq_qhi,  qhi,  qhi};

    this.setShape(new Polygon(xpts,ypts,18));
  }

  // Function adapted from the amazing ->
  // https://easings.net/
  private double easeInOutBack(double x) {
    double c1 = 1.70158;
    double c2 = c1 * 1.525;

    return (x < 0.5) ? ((Math.pow(2*x, 2) * ((c2 + 1) * 2 * x - c2)) / 2) : ((Math.pow(2 * x - 1, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
  }


  public Rectangle getWheelBounds() {return this.wheel_bounds;}
  public int getWheelWidth() {return this.wheel_width;}
  public Polygon getShape() {return this.shape;}

  public void setWheelWidth(int wheel_width) {this.wheel_width=wheel_width;}
  public void setWheelBounds(Rectangle wheel_bounds) {this.wheel_bounds=wheel_bounds;}


  private void setShape(Polygon shape) {this.shape=shape;};
}
