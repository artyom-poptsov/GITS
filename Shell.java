import java.awt.*;
import java.awt.image.*;

public class Shell extends Rectangle {
     protected BufferedImage image;
     protected int energy;
     public Shell(Point position, Dimension dimension, BufferedImage image) {
          super(position, dimension);
          this.image = image;
     }
     public int getEnergy() {
          return energy;
     }
     public BufferedImage getImage() {
          return this.image;
     }
     public void setImage(BufferedImage image) {
          this.image = image;
     }
}
