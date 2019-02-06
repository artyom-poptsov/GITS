import java.awt.image.*;
import java.awt.*;

public class LeafShell extends Shell {
    public LeafShell(Point position, Dimension dimension, BufferedImage image) {
	super(position, dimension, image, 0xFF);
    }
    public BufferedImage getImage() {
	if (this.getEnergy() % 10 == 0) {
	    BufferedImage transparentImage
		= Utils.imageToBufferedImage(Utils.makeImageTransparent((Image) this.image,
									super.getEnergy()));
	    setImage(transparentImage);
	}
	return this.image;
    }
}
