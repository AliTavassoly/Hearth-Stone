package hearthstone.gui.game;

import hearthstone.gui.BaseFrame;
import hearthstone.gui.DefaultSizes;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class GameFrame extends BaseFrame {
    private MainMenuPanel mainMenuPanel;
    private static GameFrame gameFrame;
    private Clip clip;

    private GameFrame() {
        mainMenuPanel = new MainMenuPanel();

        playSound();

        configFrame();
    }

    public static GameFrame getInstance() {
        if (gameFrame == null) {
            return gameFrame = new GameFrame();
        } else {
            return gameFrame;
        }
    }

    public static GameFrame getNewInstance() {
        return gameFrame = new GameFrame();
    }

    public void playSound(){
        try {
            File file = new File(this.getClass().getResource(
                    "/sounds/menu.wav").getFile());
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopSound(){
        clip.stop();
    }

    private void configFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(DefaultSizes.gameFrameWidth, DefaultSizes.gameFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainMenuPanel);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }
}
