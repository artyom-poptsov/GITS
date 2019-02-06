import java.awt.image.*;
import java.awt.*;

public class LeafShell extends Shell {
    public LeafShell(Point position, Dimension dimension, BufferedImage image) {
	super(position, dimension, image);
	this.energy = 0xFF;
    }
    public BufferedImage getImage() {
	if (energy % 10 == 0) {
	    BufferedImage transparentImage
		= Utils.imageToBufferedImage(Utils.makeImageTransparent((Image) this.image,
									energy));
	    setImage(transparentImage);
	}
	return this.image;
    }
}
