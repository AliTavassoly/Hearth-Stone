package hearthstone.gui.credetials;

import hearthstone.gui.BaseFrame;
import hearthstone.gui.DefaultSizes;

import javax.swing.*;

public class CredentialsFrame extends BaseFrame {
    private LogisterPanel logisterPanel;
    private static CredentialsFrame credentialsFrame;

    private CredentialsFrame(){
        logisterPanel = new LogisterPanel();

        configFrame();
    }

    public static CredentialsFrame getInstance(){
        if(credentialsFrame == null){
            return credentialsFrame = new CredentialsFrame();
        } else {
            return credentialsFrame;
        }
    }

    private void configFrame(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(DefaultSizes.credentialFrameWidth, DefaultSizes.credentialFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(logisterPanel);

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        this.setVisible(true);
    }
}
