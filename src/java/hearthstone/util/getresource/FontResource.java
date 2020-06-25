package hearthstone.util.getresource;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontResource {
    private static FontResource instance;
    public static Map<String, Font> fontMap = new HashMap<>();

    private FontResource() {
    }

    public static FontResource getInstance() {
        if (instance == null) {
            return instance = new FontResource();
        } else {
            return instance;
        }
    }

    public Font getFont(String path){
        if (!fontMap.containsKey(path)) {
            try (InputStream in = new BufferedInputStream(
                    this.getClass().getResourceAsStream(path))) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, in);
                fontMap.put(path, font);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fontMap.get(path);
    }
}
