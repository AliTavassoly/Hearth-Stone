package hearthstone.client.gui.credetials;

import hearthstone.client.data.GUIConfigs;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class LogisterPanel extends JPanel {
    private ImageButton registerButton, loginButton, minimizeButton, closeButton;

    private BufferedImage backgroundImage;

    private static final int iconX = 20;
    private static final int startButtonsY = 200;
    private static final int buttonsDis = 100;
    private static final int endIconY = GUIConfigs.credentialFrameHeight - GUIConfigs.iconHeight - 20;
    private static final int iconsDis = 70;

    public LogisterPanel() {
        configPanel();

        makeIcons();

        makeButtons();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage("/images/logister_background.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void configPanel() {
        setSize(new Dimension(GUIConfigs.credentialFrameWidth, GUIConfigs.credentialFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void makeIcons() {
        closeButton = new CloseIcon("icons/close.png", "icons/close_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().setState(Frame.ICONIFIED);
                CredentialsFrame.getInstance().setState(Frame.NORMAL);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    private void makeButtons() {
        loginButton = new ImageButton("login", "buttons/green_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);

        registerButton = new ImageButton("register", "buttons/blue_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().switchPanelTo(
                        CredentialsFrame.getInstance(), LoginPanel.makeInstance());
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().switchPanelTo(
                        CredentialsFrame.getInstance(), RegisterPanel.makeInstance());
            }
        });
    }

    private void layoutComponent() {
        //
        loginButton.setBounds(GUIConfigs.credentialFrameWidth / 2 - GUIConfigs.medButtonWidth / 2,
                startButtonsY,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);
        add(loginButton);

        //
        registerButton.setBounds(GUIConfigs.credentialFrameWidth / 2 - GUIConfigs.medButtonWidth / 2,
                startButtonsY + buttonsDis,
                GUIConfigs.medButtonWidth,
                GUIConfigs.medButtonHeight);
        add(registerButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(closeButton);
    }
}