package hearthstone.util.getresource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageResource {
    private static ImageResource instance;
    public static Map<String, BufferedImage> imageMap = new HashMap<>();

    private ImageResource() {
    }

    public static ImageResource getInstance() {
        if (instance == null) {
            return instance = new ImageResource();
        } else {
            return instance;
        }
    }

    public BufferedImage getImage(String path){
        if (!imageMap.containsKey(path)) {
            try {
                imageMap.put(path, ImageIO.read(this.getClass().getResourceAsStream(path)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageMap.get(path);
    }
}
