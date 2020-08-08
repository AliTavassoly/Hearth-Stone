package hearthstone.client.gui.credetials;

import hearthstone.client.gui.BaseFrame;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.SoundPlayer;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class CredentialsFrame extends BaseFrame {
    private LogisterPanel logisterPanel;
    private static CredentialsFrame credentialsFrame;
    private SoundPlayer soundPlayer;

    private CredentialsFrame() {
        logisterPanel = new LogisterPanel();

        configFrame();

        changeLocationMouseListener();
    }

    private void changeLocationMouseListener() {
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent mouseEvent) {
                CredentialsFrame.this.setLocation(
                        (int) mouseEvent.getLocationOnScreen().getX() - GUIConfigs.credentialFrameWidth / 2,
                        (int) mouseEvent.getLocationOnScreen().getY() - GUIConfigs.credentialFrameHeight / 2
                );
            }

            public void mouseMoved(MouseEvent mouseEvent) {
            }
        });
    }

    public static CredentialsFrame getInstance() {
        if (credentialsFrame == null) {
            credentialsFrame = new CredentialsFrame();
            credentialsFrame.playSound();
        }
        return credentialsFrame;
    }

    public static CredentialsFrame getNewInstance() {
        if (credentialsFrame != null)
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

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }
}