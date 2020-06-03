package hearthstone.gui.credetials;

import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.util.SoundPlayer;

import javax.swing.*;

public class CredentialsFrame extends BaseFrame {
    private LogisterPanel logisterPanel;
    private static CredentialsFrame credentialsFrame;
    private static SoundPlayer soundPlayer;

    private CredentialsFrame() {
        logisterPanel = new LogisterPanel();

        if(soundPlayer == null) {
            soundPlayer = new SoundPlayer("/sounds/background.wav");

            soundPlayer.loopPlay();
        }

        configFrame();
    }

    public static SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    public static CredentialsFrame getInstance() {
        if (credentialsFrame == null) {
            return credentialsFrame = new CredentialsFrame();
        } else {
            return credentialsFrame;
        }
    }

    public static CredentialsFrame getNewInstance() {
        return credentialsFrame = new CredentialsFrame();
    }

    private void configFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(SizeConfigs.credentialFrameWidth, SizeConfigs.credentialFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(logisterPanel);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        this.setVisible(true);
    }
}