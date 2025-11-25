
import java.awt.*;
import javax.swing.*;

class GameWindow extends JFrame {

  private Dimension dimensions;

  /***
   * No arg Constructor for a new GameWindow. Creates a 800x500 window named "DEFAULT WINDOW".
   */
  public GameWindow() {
    this("DEFAULT WINDOW",800,500);
  }

  /***
   * Constructor for a new GameWindow
   *
   * @param name              name of the new window
   * @param width             width of the new window
   * @param height            height of the new window
   */
  public GameWindow(String name, int width, int height) {
    super();
    setTitle(name);
    setDimensions(new Dimension(width, height));
		setSize(getDimensions());
    setResizable(false); // Simplifies UI work :3
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /** Get a Dimension object representitive of the window size @return Dimension object for the window size*/
  public Dimension getDimensions() {return this.dimensions;}

  /** Set the Dimension object associated with the window size @param dimension Dimension to set the old Dimension to*/
  public void setDimensions(Dimension dimension) {this.dimensions = dimension;}

}
