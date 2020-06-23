package hearthstone.util.getresource;

import java.io.File;

public class SoundResource {
    private static SoundResource instance;

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
        return new File(this.getClass().getResource(path).getFile());
    }
}
