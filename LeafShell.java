import java.awt.image.*;
import java.awt.*;

public class LeafShell extends Shell {
  public LeafShell(Point position, Dimension dimension, BufferedImage image) {
    super(position, dimension, image);
    this.energy = 100;
  }
}
