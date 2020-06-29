package hearthstone.gui.credetials;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
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
    private static final int endIconY = SizeConfigs.credentialFrameHeight - SizeConfigs.iconHeight - 20;
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
        setSize(new Dimension(SizeConfigs.credentialFrameWidth, SizeConfigs.credentialFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void makeIcons() {
        closeButton = new CloseIcon("icons/close.png", "icons/close_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_active.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

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
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);

        registerButton = new ImageButton("register", "buttons/blue_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().switchPanelTo(
                        CredentialsFrame.getInstance(), new LoginPanel());
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().switchPanelTo(
                        CredentialsFrame.getInstance(), new RegisterPanel());
            }
        });
    }

    private void layoutComponent() {
        //
        loginButton.setBounds(SizeConfigs.credentialFrameWidth / 2 - SizeConfigs.medButtonWidth / 2,
                startButtonsY,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);
        add(loginButton);

        //
        registerButton.setBounds(SizeConfigs.credentialFrameWidth / 2 - SizeConfigs.medButtonWidth / 2,
                startButtonsY + buttonsDis,
                SizeConfigs.medButtonWidth,
                SizeConfigs.medButtonHeight);
        add(registerButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(closeButton);
    }
}