import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class BoxButton extends JButton {

  // Nice colors for the button
  private static Color c_idle = new Color(240,240,240);
  private static Color c_pressed = new Color(200,200,200);
  private static Color c_outline = new Color(50,50,50);

  public BoxButton(String name) {
    Border comp = new CompoundBorder(new LineBorder(c_outline),
                                     new EmptyBorder(5,5,5,5));
		this.setBorder(comp); // 5px box-ish border with c_outline color
    this.setText(name);

    // You need to make buttons opaque to set the background....
    // even tho the default background is opaque.... For some reason....
    // Thanks: https://stackoverflow.com/questions/4990952/why-does-setbackground-to-jbutton-does-not-work
    this.setOpaque(true);
  }

  public void paintComponent(Graphics gfx) {
    // If the button is pressed set the background color to c_pressed
    boolean isp = this.getModel().isPressed();
    if (isp) {
      this.setBackground(c_pressed);
      super.paintComponent(gfx);
      return;
    }

    // Set to c_idle otherwise
    this.setBackground(c_idle);
    super.paintComponent(gfx);
  }
}
