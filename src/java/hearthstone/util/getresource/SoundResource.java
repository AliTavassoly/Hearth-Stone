package hearthstone.util.getresource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundResource {
    private static SoundResource instance;
    public static Map<String, File> soundMap = new HashMap<>();

    private SoundResource() {
    }

    public static SoundResource getInstance() {
        if (instance == null) {
            return instance = new SoundResource();
        } else {
            return instance;
        }
    }

    public File getSound(String path) {
        if (!soundMap.containsKey(path)) {
            try {
                soundMap.put(path, new File(this.getClass().getResource(path).getFile()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return soundMap.get(path);
    }
}
