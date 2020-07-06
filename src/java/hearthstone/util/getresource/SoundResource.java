package hearthstone.util.getresource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundResource {
    private static SoundResource instance;
    private static Map<String, File> map = new HashMap<>();

    public static SoundResource getInstance() {
        if (instance == null) {
            return instance = new SoundResource();
        } else {
            return instance;
        }
    }

    public File getSound(String path) {
        if(!map.containsKey(path)) {
            map.put(path, new File(this.getClass().getResource(path).getFile()));
        }
        return map.get(path);
        //return new File(this.getClass().getResource(path).getFile());
    }
}
