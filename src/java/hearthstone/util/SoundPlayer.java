package hearthstone.util;

import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.util.getresource.SoundResource;

import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {
    private Clip clip;
    private static float soundValue;
    private AudioInputStream audioInputStream;
    private FloatControl gainControl;

    public SoundPlayer(String path) {
        try {
            File file = SoundResource.getInstance().getSound(path);
            audioInputStream =
                    AudioSystem.getAudioInputStream(file.getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }

                if (!clip.isRunning())
                    clip.close();
            });

            gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FloatControl getGainControl() {
        return gainControl;
    }

    public static void changeVolume(int x) {
        soundValue += x;
        CredentialsFrame.getSoundPlayer().getGainControl().setValue(soundValue);
    }

    public void playOnce() {
        try {
            clip.open();
            gainControl.setValue(soundValue);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loopPlay() {
        try {
            clip.open();
            gainControl.setValue(soundValue);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
