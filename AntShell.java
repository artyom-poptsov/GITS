import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class AntShell extends Shell {
  private World world;
  private int angle = 0;
  private BufferedImage originalImage;
  public AntShell(Point position, Dimension dimension, BufferedImage image) {
    super(position, dimension, image);
    this.originalImage = image;
    this.energy = 1000;
    this.world = World.getInstance();;
  }

  public void eat(int deltaE) {
    energy += deltaE;
  }

  public ArrayList<Shell> getObjectsInSight() {
    ArrayList<Shell> objects = world.getObjects();
    ArrayList<Shell> nearestObjects = new ArrayList<>();
    synchronized (objects) {
      for (Shell obj : objects) {
        if (obj.intersects(this)) {
          nearestObjects.add(obj);
        }
      }
    }
    return nearestObjects;
  }

  public void move(Point delta) {
    super.x = Math.abs((super.x + delta.x) % world.getWidth());
    super.y = Math.abs((super.y + delta.y) % world.getHeight());
    this.energy--;
    int newAngle = 0;
    try {
	newAngle = (int) (Math.atan2(delta.y, delta.x) * (180 / Math.PI)) + 90;
    } catch (Exception e) {

    }
    if (newAngle != this.angle) {
      this.image = Utils.rotateImage(this.originalImage, newAngle);
      angle = newAngle;
    }
  }
}
