import java.awt.*;
import java.awt.image.*;

public class Shell extends Rectangle {
     protected BufferedImage image;
     private int energy;
     public Shell(Point position, Dimension dimension, BufferedImage image,
		  int energy) {
          super(position, dimension);
          this.image = image;
	  this.energy = energy;
     }
     public int getEnergy() {
          return energy;
     }
     public void changeEnergy(int delta) {
	  if (energy > 0) {
	       this.energy += delta;
	  }
     }
     public BufferedImage getImage() {
          return this.image;
     }
     public void setImage(BufferedImage image) {
          this.image = image;
     }
}
