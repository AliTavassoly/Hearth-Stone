package hearthstone.gui.credetials;

import hearthstone.gui.DefaultSizes;
import hearthstone.gui.ImageButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class LogisterPanel extends JPanel {
    private ImageButton registerButton, loginButton, minimizeButton, closeButton;

    private static final int iconX = 20;
    private static final int startButtonsY = 200;
    private static final int buttonsDis = 100;
    private static final int endIconY = DefaultSizes.credentialFrameHeight - DefaultSizes.iconHeight - 20;
    private static final int iconsDis = 70;

    public LogisterPanel() {
        configPanel();

        loginButton = new ImageButton("login.png", "login_active.png",
                DefaultSizes.logisterButtonWidth,     DefaultSizes.logisterButtonHeight);

        registerButton = new ImageButton("register.png", "register_active.png",
                DefaultSizes.logisterButtonWidth,  DefaultSizes.logisterButtonHeight);

        closeButton = new ImageButton("close.png", "close_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

        minimizeButton = new ImageButton("minimize.png", "minimize_active.png",
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);

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

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().getContentPane().setVisible(false);
                CredentialsFrame.getInstance().setContentPane(new LoginPanel());
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CredentialsFrame.getInstance().getContentPane().setVisible(false);
                CredentialsFrame.getInstance().setContentPane(new RegisterPanel());
            }
        });

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/logister_background.jpg"));
        } catch (Exception e){
            System.out.println(e);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void configPanel(){
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent(){
        //
        loginButton.setBounds(DefaultSizes.credentialFrameWidth / 2 - DefaultSizes.logisterButtonWidth / 2,
                startButtonsY,
                DefaultSizes.logisterButtonWidth,
                DefaultSizes.logisterButtonHeight);
        add(loginButton);

        //
        registerButton.setBounds(DefaultSizes.credentialFrameWidth / 2 - DefaultSizes.logisterButtonWidth / 2,
                startButtonsY + buttonsDis,
                DefaultSizes.logisterButtonWidth,
                DefaultSizes.logisterButtonHeight);
        add(registerButton);

        //
        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                DefaultSizes.iconWidth,
                DefaultSizes.iconHeight);
        add(closeButton);
    }
}
