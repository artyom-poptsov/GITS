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

  public void move(int deltaX, int deltaY) {
    super.x = Math.abs((super.x + deltaX) % world.getWidth());
    super.y = Math.abs((super.y + deltaY) % world.getHeight());
    this.energy--;
    int newAngle = -1;
    if ((deltaX > 0) && (deltaY < 0)) {
      newAngle = 45;
    }  else if ((deltaX > 0) && (deltaY == 0)) {
      newAngle = 90;
    }  else if ((deltaX > 0) && (deltaY > 0)) {
      newAngle = 135;
    }  else if ((deltaX == 0) && (deltaY > 0)) {
      newAngle = 180;
    }  else if ((deltaX < 0) && (deltaY > 0)) {
      newAngle = 225;
    }  else if ((deltaX > 0) && (deltaY == 0)) {
      newAngle = 270;
    }  else if ((deltaX < 0) && (deltaY < 0)) {
      newAngle = 315;
    }
    if ((newAngle > 0) && (newAngle != this.angle)) {
      this.image = Utils.rotateImage(this.originalImage, newAngle);
      angle = newAngle;
    }
  }
}
