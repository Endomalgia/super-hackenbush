import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Main {

  /***
   * Start of execution.
   *
   * @param args    Unused array of arguments passed to the program on command line
   */
  public static void main(String[] args) {

    /*

      Menu screen with cat drinking tea + chill music(easy)
      Level select with maybe a cute cat animation graphic (could be heavily simplified)
      Levels with win screen
        - small help screen when ? is pressed and at the start of the level
        - draw a hackenbush picture
        - evaluate a hackenbush picture
        - complete a hackenbush picture (possibly against an ai, could be avoided)
        - win screen shows a threshold
      Random mode!!!! (avoidable)

    */

    GameWindow gw_main = new GameWindow("SUPER Hackenbush", 700, 500);
    /*
    gw_main.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e) {

        System.out.println("Hello!");

      }
      public void keyReleased(KeyEvent e) {System.out.println("Hello!");}
      public void keyTyped(KeyEvent e) {System.out.println("Hello!");}
    });
    */
    
    JLayeredPane lp_root = new JLayeredPane();
    gw_main.add(lp_root);

    TitleScreen ts_main = new TitleScreen(lp_root);
    ts_main.addToLayer();

    gw_main.setFocusable(true);
    gw_main.setVisible(true);
  }
}
