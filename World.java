import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.lang.reflect.Constructor;

public class World extends JPanel {
     private static World instance   = null;

     private ArrayList<Shell> objects = null;
     private Random randomness        = null;
     private final static int LEAVES = 5;

     private World() {
          this.objects    = new ArrayList<>();
          this.randomness =  new Random();
     }

     public Random getRandomness() {
          return randomness;
     }

     public void generateLeaves(int count) throws Exception {
          BufferedImage leafImage
               = ImageIO.read(new File("./images/LeafShell.png"));
          for (int n = 0; n < count; n++) {
               BufferedImage rotatedImage
                    = Utils.rotateImage(leafImage,
                                        randomness.nextInt(90));
               this.add(makeShell("LeafShell",
                                  new Point(randomness.nextInt(getWidth()
							       - leafImage.getWidth()),
                                            randomness.nextInt(getHeight()
							       - leafImage.getHeight())),
                                  new Dimension(40, 40),
                                  rotatedImage));
          }
     }

     private Shell makeShell(String name, Point position, Dimension dimension,
                             BufferedImage image) throws Exception {
          Class<?> clazz = Class.forName(name);
          Constructor<?> ctor = clazz.getConstructor(Point.class, Dimension.class,
                                                     BufferedImage.class);
          Object object = ctor.newInstance(position, dimension, image);

          return (Shell) object;
     }

     public Shell makeShell(String name, Point position, Dimension dimension) throws Exception {
          BufferedImage image = ImageIO.read(new File("./images/" + name + ".png"));
          return makeShell(name, position, dimension, image);

     }

     public static World getInstance() {
          if (instance == null) {
               instance = new World();
          }
          return instance;
     }

     public void add(Shell object) {
          synchronized (objects) {
               objects.add(object);
          }
     }
     public void remove(Shell object) throws Exception {
          synchronized (objects) {
               objects.remove(object);
          }
     }

     public final ArrayList<Shell> getObjects() {
          synchronized (objects) {
               return objects;
          }
     }

     public void paint(Graphics g) {
          super.paint(g);
	  int leavesCount = 0;
          synchronized(objects) {
               for (int idx = objects.size() - 1; idx >= 0; idx--) {
                    Shell obj = objects.get(idx);
		    if (obj instanceof LeafShell) {
			 ++leavesCount;
		    }
		    if (randomness.nextInt(3) == 0) {
			 obj.changeEnergy(randomness.nextInt(3) - 2);
		    }
		    if (obj.getEnergy() <= 0) {
			 objects.remove(obj);
			 paint(g);
			 return;
		    }
                    g.drawImage(obj.getImage(), (int) obj.getX(), (int) obj.getY(),
                                null);
                    g.drawString("" + obj.getEnergy(),
                                 (int) (obj.getX() + (obj.getWidth() / 2)),
                                 (int) (obj.getY() + (obj.getHeight() / 2)));
               }
	       if (leavesCount < LEAVES) {
		    try {
			 generateLeaves(1);
		    } catch (Exception e) {
			 System.out.println(e);
		    }
	       }
          }
     }
}
