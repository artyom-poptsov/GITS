import java.awt.image.*;
import java.awt.geom.*;

import java.awt.image.RGBImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.ImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Toolkit;

public class Utils {
     public static BufferedImage rotateImage(BufferedImage source, int degree) {
          AffineTransform trans = new AffineTransform();
          trans.rotate(Math.toRadians(degree),
                       source.getWidth() / 2,
                       source.getHeight() / 2);
          double offset = (source.getWidth() - source.getHeight()) / 2;
          trans.translate(offset, offset);
          AffineTransformOp op = new AffineTransformOp(trans,
                                                       AffineTransformOp.TYPE_BILINEAR);
          BufferedImage r = new BufferedImage(source.getHeight() * 4,
                                              source.getWidth() * 4,
                                              source.getType());
          op.filter(source, r);
          return r;
     }
     public static BufferedImage imageToBufferedImage(Image img) {
	  BufferedImage bimg = new BufferedImage(img.getWidth(null),
						 img.getHeight(null),
						 BufferedImage.TYPE_INT_ARGB);
	  Graphics2D bgr = bimg.createGraphics();
	  bgr.drawImage(img, 0, 0, null);
	  bgr.dispose();
	  return bimg;
						
     }
     public static Image makeImageTransparent(Image im, int value) {
	  ImageFilter filter = new RGBImageFilter() {
		    @Override
		    public final int filterRGB (int x, int y, int rgb) {
			 return rgb & (0x00FFFFFF | (value << 24));
		    }
	       };
	  ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	  return Toolkit.getDefaultToolkit().createImage(ip);
     }
}
