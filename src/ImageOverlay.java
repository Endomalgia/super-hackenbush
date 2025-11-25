import java.awt.*;
import javax.swing.*;
import java.io.*;

class ImageOverlay extends JPanel {

  private JButton overlayButton;
  private JLayeredPane layerPane;

  /***
   * Create a new ImageOverlay (including all associated child components) for installation on a JLayeredPane
   *
   * @param layerPane   The JLayeredPane to add everything to
   */
  public ImageOverlay(ImageIcon image, JLayeredPane layerPane) {
    this.layerPane = layerPane;
    overlayButton = new JButton("");
    overlayButton.setBounds(30,50,
                            image.getIconWidth(),image.getIconHeight());
    overlayButton.setIcon(image);
    overlayButton.addActionListener((e) -> {
      layerPane.remove(overlayButton);
      layerPane.revalidate();
      layerPane.repaint();
    });
  }

  /***
   * All all components associated with this object to their associated JLayeredPane
   */
  public void addToLayer() {
    if (overlayButton.getParent() != null) return;
    layerPane.add(overlayButton, JLayeredPane.DRAG_LAYER);
  }
}
