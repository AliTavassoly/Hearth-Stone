package hearthstone.client.gui.credetials;

import hearthstone.client.gui.BaseFrame;
import hearthstone.client.configs.GUIConfigs;
import hearthstone.util.SoundPlayer;

import javax.swing.*;

public class CredentialsFrame extends BaseFrame {
    private LogisterPanel logisterPanel;
    private static CredentialsFrame credentialsFrame;
    private SoundPlayer soundPlayer;

    private CredentialsFrame() {
        logisterPanel = new LogisterPanel();

        configFrame();
    }

    public static CredentialsFrame getInstance() {
        if (credentialsFrame == null) {
            credentialsFrame = new CredentialsFrame();
            credentialsFrame.playSound();
        }
        return credentialsFrame;
    }

    public static CredentialsFrame getNewInstance() {
        if(credentialsFrame != null)
            credentialsFrame.stopSound();
        credentialsFrame = new CredentialsFrame();
        credentialsFrame.playSound();
        return credentialsFrame;
    }

    private void configFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(GUIConfigs.credentialFrameWidth, GUIConfigs.credentialFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(logisterPanel);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        this.setVisible(true);
    }

    public void stopSound() {
        soundPlayer.stop();
        soundPlayer.close();
    }

    public void playSound() {
        soundPlayer = new SoundPlayer("/sounds/background.wav");
        soundPlayer.loopPlay();
    }

    public SoundPlayer getSoundPlayer(){
        return soundPlayer;
    }
}