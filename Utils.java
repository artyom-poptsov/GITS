import java.awt.image.*;
import java.awt.geom.*;

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
}
