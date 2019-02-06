import java.awt.image.*;
import java.awt.*;

public class LeafShell extends Shell {
    public LeafShell(Point position, Dimension dimension, BufferedImage image) {
	super(position, dimension, image, 0xFF);
    }
    public BufferedImage getImage() {
	int energy = this.getEnergy();
	if ((energy % 10 == 0) && (energy > 128)) {
	    BufferedImage transparentImage
		= Utils.imageToBufferedImage(Utils.makeImageTransparent((Image) this.image,
									energy));
	    setImage(transparentImage);
	}
	return this.image;
    }
}
