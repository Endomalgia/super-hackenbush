import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

  // Nice colors for the gradient
  private static Color c_icee = new Color(148, 212, 247);
  private static Color c_fire = new Color(247, 183, 148);

  public void paintComponent(Graphics gfx) {
    super.paintComponent(gfx);
    Graphics2D g = (Graphics2D)gfx; // Cast to Gfx2D (to paint)

    // Paint a gradient going from blue -> red across the panel
		g.setPaint(new GradientPaint(0,0,c_icee,this.getWidth(),0,c_fire));
		g.fillRect(0,0,this.getWidth(),this.getHeight());

  }
}
