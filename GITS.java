import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.*;
import java.io.*;
import java.awt.geom.*;

public class GITS {
     private static World world = World.getInstance();

     private static void sleep(int time) {
          try {
               Thread.sleep(time);
          } catch (Exception e) {
          }
     }

     public static void main(String[] args) throws Exception {
          JFrame window = new JFrame("E02");
          AntShell antShell = (AntShell) world.makeShell("AntShell",
                                                         new Point(50, 50),
                                                         new Dimension(40, 40));
          AntGhost antGhost = new AntGhost(antShell);

          world.add(antShell);

          world.generateLeaves(5);

          window.setContentPane(world);
          window.setSize(500, 500);
          window.show();

          while (true) {
               antGhost.run();
               world.repaint();
               sleep(50);
          }
     }
}
